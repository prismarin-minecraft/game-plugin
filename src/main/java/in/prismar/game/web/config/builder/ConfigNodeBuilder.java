package in.prismar.game.web.config.builder;

import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.game.web.config.model.ConfigNodeValueType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@RequiredArgsConstructor
public class ConfigNodeBuilder {

    private String name;
    private ConfigNodeType type;

    private Object value;
    private ConfigNodeValueType valueType;

    private Map<String, ConfigNode> children;

    private String template;

    private List<String> options;

    private ConfigNode node;

    public ConfigNodeBuilder(String name, ConfigNodeType type) {
        this.name = name;
        this.type = type;

        if(type == ConfigNodeType.CATEGORY) {
            this.children = new LinkedHashMap<>();
        }
    }

    public ConfigNodeBuilder(ConfigNode node) {
        this.node = node;
    }

    public ConfigNodeBuilder addOptions(String... options) {
        if(this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.addAll(Arrays.asList(options));
        return this;
    }
    public ConfigNodeBuilder setTemplate(String id) {
        this.template = id;
        return this;
    }

    public ConfigNodeBuilder setValue(String value) {
        this.valueType = ConfigNodeValueType.TEXT;
        this.value = value;
        return this;
    }

    public ConfigNodeBuilder setValue(int value) {
        this.valueType = ConfigNodeValueType.NUMBER;
        this.value = value;
        return this;
    }

    public ConfigNodeBuilder setValue(double value) {
        this.valueType = ConfigNodeValueType.NUMBER;
        this.value = value;
        return this;
    }

    public ConfigNodeBuilder addChild(ConfigNode node) {
        if(this.node != null) {
            if(this.node.getChildren() == null) {
                this.node.setChildren(new LinkedHashMap<>());
            }
        } else {
            if(this.children == null) {
                this.children = new LinkedHashMap<>();
            }
        }
        Map<String, ConfigNode> children = (this.node != null ? this.node.getChildren() : getChildren());
        children.put(node.getName(), node);
        return this;
    }

    public ConfigNodeBuilder addChild(ConfigNodeBuilder builder) {
        return addChild(builder.build());
    }

    public ConfigNode build() {
        if(this.node != null) {
            return this.node;
        }
        ConfigNode node = new ConfigNode();
        node.setName(this.name);
        node.setType(this.type);
        if(this.valueType != null) {
            node.setValueType(valueType);
        }
        if(this.value != null) {
            node.setValue(value);
        }
        if(this.template != null) {
            node.setTemplate(template);
        }
        if(this.children != null) {
            node.setChildren(this.children);
        }
        if(this.options != null) {
            node.setOptions(options);
        }
        return node;
    }

}
