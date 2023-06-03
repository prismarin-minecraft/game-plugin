package in.prismar.game.web.config.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class ConfigNode implements in.prismar.api.configuration.node.ConfigNode {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String name;
    private ConfigNodeType type;
    private Object value;
    private ConfigNodeValueType valueType;

    private Map<String, ConfigNode> children;

    private String template;

    private List<String> options;

    private transient String id;


    public ConfigNode clone() {
        ConfigNode node = new ConfigNode();
        node.setName(getName());
        node.setType(getType());
        node.setValue(getValue());
        if(getChildren() != null) {
            node.setChildren(new LinkedHashMap<>());
            for(ConfigNode child : getChildren().values()) {
                node.getChildren().put(child.getName(), child.clone());
            }
        }
        node.setTemplate(getTemplate());
        node.setValueType(getValueType());
        return node;
    }

    @Override
    public int valueAsInteger() {
        try {
            return (int)value;
        }catch (ClassCastException exception) {
            return (int)valueAsDouble();
        }
    }

    public String valueAsString() {
        return (String) value;
    }

    @Override
    public double valueAsDouble() {
        return (double) value;
    }
}
