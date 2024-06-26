package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.utils.Version;
import org.bukkit.plugin.java.JavaPlugin;

public class NMSHandlers {
    private static NMSHandler nmsHandler;

    // Do not mix with innit, bruv!
    private static boolean init(JavaPlugin plugin) {
        Version version = Version.getServerVersion();

        if (nmsHandler == null) {
            try {
                nmsHandler = (NMSHandler) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.NMSHandler", version.nmsVersion)).getConstructor(JavaPlugin.class).newInstance(plugin);
            } catch (Exception exception) {
                return false;
            }
        }

        return true;
    }

    public static NMSHandler getNmsHandler() {
        if (nmsHandler == null) {
            throw new RuntimeException("NMSHandler has not been initialised! This could be due to incorrect usage, or unsupported NMS version!");
        }

        return nmsHandler;
    }

    public static class British {

        public static boolean initialise(JavaPlugin plugin) {
            return init(plugin);
        }
    }

    public static class American {

        public static boolean initialize(JavaPlugin plugin) {
            return init(plugin);
        }
    }
}
