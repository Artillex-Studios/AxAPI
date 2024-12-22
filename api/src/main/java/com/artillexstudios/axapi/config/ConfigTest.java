package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.config.annotation.Header;
import com.artillexstudios.axapi.config.annotation.Named;
import com.artillexstudios.axapi.config.annotation.PostProcess;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.attribute.AttributeModifier;

@Header("This is a configuration file!")
public class ConfigTest implements ConfigurationPart {

    public static void main(String[] args) {
        YamlConfiguration.of(null, null)
                .addUpdater(1, 2, config -> {

                }).build();
    }
    @Named("setting")
    public static double value = 0;

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


    public static int configVersion = 1;
}
