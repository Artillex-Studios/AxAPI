package com.artillexstudios.axapi.config;

import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

public class YamlConstructor extends SafeConstructor {

    public void flatten(MappingNode node) {
        this.flattenMapping(node);
    }

    public Object construct(Node node) {
        return this.constructObject(node);
    }
}
