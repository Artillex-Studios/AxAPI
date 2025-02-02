package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.Version;
import org.bukkit.plugin.java.JavaPlugin;

public class NMSHandlers {
    private static NMSHandler nmsHandler;

    // Do not mix with innit, bruv!
    private static boolean init(AxPlugin plugin) {
        Version version = Version.getServerVersion();
        if (version == Version.UNKNOWN) {
            LogUtils.warn("Could not load plugin {} due to version mismatch! Found protocol version {} which is unsupported!", plugin.getName(), Version.protocolVersion());
            return false;
        }

        if (nmsHandler != null) {
            LogUtils.warn("NMS support has already been enabled!");
            return false;
        }

        try {
            nmsHandler = (NMSHandler) Class.forName("com.artillexstudios.axapi.nms.%s.NMSHandler".formatted(version.nmsVersion)).getConstructor(AxPlugin.class).newInstance(plugin);
        } catch (Exception exception) {
            LogUtils.warn("Could not enable NMSHandler due to an internal exception while loading NMSHandler!", exception);
            return false;
        }

        return true;
    }

    public static NMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public static class British {

        public static boolean initialise(AxPlugin plugin) {
            return init(plugin);
        }
    }

    public static class American {

        public static boolean initialize(AxPlugin plugin) {
            return init(plugin);
        }
    }
}
