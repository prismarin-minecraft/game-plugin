package in.prismar.game.web.config.file;

import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.game.Game;
import in.prismar.game.web.config.builder.ConfigNodeBuilder;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.game.web.config.model.ConfigNodeValueType;
import in.prismar.library.common.delayed.DelayedOperation;
import in.prismar.library.common.delayed.DelayedOperationExecutor;
import in.prismar.library.common.delayed.DelayedOperationTask;
import in.prismar.library.common.event.EventBus;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import io.lumine.mythic.utils.reflections.util.ConfigurationBuilder;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class ConfigNodeFile extends GsonFileWrapper<Map<String, ConfigNode>> implements ConfigNodeProvider<ConfigNode> {

    @Inject
    private ConfigTemplateFile templateFile;

    private final Game game;
    private Map<String, ConfigNode> nodeIds;

    private final EventBus eventBus;

    private final DelayedOperationExecutor<DelayedOperation> executor;
    public ConfigNodeFile(Game game) {
        super(game.getDefaultDirectory().concat("config_nodes.json"), new TypeToken<Map<String, ConfigNode>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
            save();
        }
        this.game = game;
        this.nodeIds = new HashMap<>();
        this.eventBus = new EventBus();

        this.executor = new DelayedOperationExecutor<>("config_nodes");

        refreshNodeIds();
    }
    @SafeInitialize
    private void initialize() {
        for(ConfigNode node : getEntity().values()) {
            syncWithTemplate(node);
        }
    }

    public String getString(ConfigNode category, String name) {
        if(category.getChildren().containsKey(name)) {
            return category.getChildren().get(name).valueAsString();
        }
        return "";
    }

    public int getInteger(ConfigNode category, String name) {
        if(category.getChildren().containsKey(name)) {
            return category.getChildren().get(name).valueAsInteger();
        }
        return 0;
    }

    public double getDouble(ConfigNode category, String name) {
        if(category.getChildren().containsKey(name)) {
            return category.getChildren().get(name).valueAsDouble();
        }
        return 0.0;
    }

    public void updateNode(ConfigNode node) {
        if (existsNodeId(node.getId())) {
            ConfigNode current = getNode(node.getId());
            if(node.getType() == ConfigNodeType.DELETED) {
                deleteNode(current);
                return;
            }
            if (current.getType() == ConfigNodeType.CATEGORY && node.getChildren() != null) {
                for (ConfigNode child : node.getChildren().values()) {
                    updateNode(child);
                }
            } else if (current.getType() == ConfigNodeType.ENTRY) {
                current.setValue(node.getValue());
                save();
            }
        } else {
            insert(node);
        }
    }

    public Map<String, ConfigNode> refreshNodeIds(Map<String, ConfigNode> nodeIds) {
        Map<String, ConfigNode> map = new HashMap<>();
        for(Map.Entry<String, ConfigNode> entry : nodeIds.entrySet()) {
            addNodeId(map, "", entry.getKey(), entry.getValue());
        }
        return map;
    }

    public void refreshNodeIds() {
        this.nodeIds = refreshNodeIds(getEntity());
    }

    public void syncWithTemplate(ConfigNode node) {
        ConfigNode parent = getParent(node);
        if(parent != null) {
            if(parent.getTemplate() != null) {
                ConfigNode template = templateFile.getTemplate(parent.getTemplate());
                if(template != null) {
                    for(ConfigNode child : template.getChildren().values()) {
                        if(child.getType() == ConfigNodeType.ENTRY) {
                            if(!node.getChildren().containsKey(child.getName())) {
                                node.getChildren().put(child.getName(), child.clone());
                            }
                        }
                    }
                }
            }
        }
        for(ConfigNode child : node.getChildren().values()) {
            if(child.getType() == ConfigNodeType.CATEGORY) {
                syncWithTemplate(child);
            }
        }
    }

    private void addNodeId(Map<String, ConfigNode> ids, String parentId, String name, ConfigNode node) {
        final String id = (parentId.isEmpty() ? "" : parentId.concat(".")).concat(name.toLowerCase());
        node.setId(id);

        if(node.getChildren() != null) {
            Map<String, ConfigNode> children = node.getChildren();
            for(Map.Entry<String, ConfigNode> child : children.entrySet()) {
                addNodeId(ids, id, child.getKey(), child.getValue());
            }
        }
        ids.put(id, node);
    }

    @Override
    public ConfigNode getNode(String id) {
        return nodeIds.get(id);
    }

    public ConfigNode getParent(ConfigNode node) {
        return getParent(this.nodeIds, node);
    }

    public ConfigNode getParent(Map<String, ConfigNode> nodeIds, ConfigNode node) {
        if(node.getId() != null) {
            List<String> paths = new ArrayList<>(Arrays.asList(node.getId().split("\\.")));
            if(!paths.isEmpty()) {
                paths.remove(paths.size() - 1);
                final String parentId = Joiner.on(".").join(paths);
                if(nodeIds.containsKey(parentId)) {
                    return nodeIds.get(parentId);
                }
            }

        }
        return null;
    }

    public ConfigNode getOrCreateRootNode(String name) {
        final String id = name.toLowerCase();
        if(existsNodeId(id)) {
            return getNode(id);
        }
        ConfigNodeBuilder builder = new ConfigNodeBuilder(name, ConfigNodeType.CATEGORY);
        getEntity().put(name, builder.build());
        refreshNodeIds();
        return getNode(id);
    }

    public ConfigNode insertRoot(ConfigNode node) {
        getEntity().put(node.getName(), node);
        save();
        return node;
    }

    public void insert(ConfigNode node) {
        ConfigNode parent = getParent(node);
        if(parent != null) {
            parent.getChildren().put(node.getName(), node);
            refreshNodeIds();
            save();
        }
    }

    public void deleteNode(ConfigNode node) {
        ConfigNode parent = getParent(node);
        if(parent != null) {
            parent.getChildren().remove(node.getName());
        } else {
            getEntity().remove(node.getName());
        }
        refreshNodeIds();
        save();
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    public boolean existsNodeId(String id) {
        return this.nodeIds.containsKey(id);
    }


    @Override
    public int getInteger(String id, int defaultValue) {
        ConfigNode node = getOrCreateNode(id);
        if(node.getValue() == null) {
            node.setValue(defaultValue);
            node.setValueType(ConfigNodeValueType.NUMBER);
        }
        return node.valueAsInteger();
    }

    @Override
    public double getDouble(String id, double defaultValue) {
        ConfigNode node = getOrCreateNode(id);
        if(node.getValue() == null) {
            node.setValue(defaultValue);
            node.setValueType(ConfigNodeValueType.NUMBER);
        }
        return node.valueAsDouble();
    }

    @Override
    public String getString(String id, String defaultValue) {
        ConfigNode node = getOrCreateNode(id);
        if(node.getValue() == null) {
            node.setValue(defaultValue);
            node.setValueType(ConfigNodeValueType.TEXT);
        }
        return node.valueAsString();
    }

    public ConfigNode getOrCreateNode(String id) {
        final String loweredId = id.toLowerCase();
        if(existsNodeId(loweredId)) {
            return getNode(loweredId);
        }
        final String[] paths = id.split("\\.");
        StringBuilder pathId = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            boolean last = false;
            if(i >= paths.length - 1) {
                pathId.append(".").append(path.toLowerCase());
                last = true;
            } else {
                if(i == 0) {
                    pathId.append(path.toLowerCase());
                } else {
                    pathId.append(".").append(path.toLowerCase());
                }
            }
            String resultId = pathId.toString();
            if(!existsNodeId(resultId)) {
                if(last) {
                    ConfigNode entry = new ConfigNodeBuilder(path, ConfigNodeType.ENTRY).build();
                    entry.setId(resultId);
                    ConfigNode parent = getParent(entry);
                    if(parent != null) {
                        parent.getChildren().put(path, entry);
                    }
                    nodeIds.put(resultId, entry);
                    saveDelayed();
                    return entry;
                } else {
                    ConfigNode category = new ConfigNodeBuilder(path, ConfigNodeType.CATEGORY).build();
                    category.setId(resultId);
                    ConfigNode parent = getParent(category);
                    if(parent != null) {
                        parent.getChildren().put(path, category);
                    } else {
                        getEntity().put(path, category);
                    }
                    nodeIds.put(resultId, category);
                }

            }
        }
        return null;
    }

    public void saveDelayed() {
        this.executor.addTask("save", 5000, this::save);
    }
}
