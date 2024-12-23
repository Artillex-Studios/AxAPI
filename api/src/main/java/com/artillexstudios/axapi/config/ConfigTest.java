package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.annotation.Comment;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.config.annotation.Header;
import com.artillexstudios.axapi.config.annotation.Named;
import com.artillexstudios.axapi.config.annotation.PostProcess;
import com.artillexstudios.axapi.config.renamer.LowerKebabCaseRenamer;
import org.bukkit.attribute.AttributeModifier;
import org.yaml.snakeyaml.DumperOptions;

@Header("This is a configuration file!")
public class ConfigTest implements ConfigurationPart {

    @Named("setting")
    @Comment("oreo")
    public static double value = 0;
    public static int configVersion = 1;

    public static void main(String[] args) {
        YamlConfiguration.of(null, ConfigTest.class)
                .withKeyRenamer(new LowerKebabCaseRenamer())
                .configVersion(3, "config-version")
                .addUpdater(1, 2, config -> {
                    config.set("oreoman", true);
                }).addUpdater(2, 3, config -> {
                    config.move("red", "oreo");
                }).withDumperOptions((dumperOptions) -> {
                    dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                }).withLoaderOptions(loaderOptions -> {
                    loaderOptions.setAllowDuplicateKeys(false);
                })
                .build();
    }

    @Named("wow")
    static class Option implements ConfigurationPart {
        public static int breakDanceThreshold = 10;
        public static AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;

        @PostProcess
        public void postProcess() {
            if (operation == null) {
                // The valueOf failed!
            }
        }
    }
}
