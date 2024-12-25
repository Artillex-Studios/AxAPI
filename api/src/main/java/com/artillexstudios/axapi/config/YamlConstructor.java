package com.artillexstudios.axapi.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

public class YamlConstructor extends SafeConstructor {

    public YamlConstructor(LoaderOptions loaderOptions) {
        super(loaderOptions);
    }

    public void flatten(MappingNode node) {
        this.flattenMapping(node);
    }

    public Object construct(Node node) {
        return this.constructObject(node);
    }
}
