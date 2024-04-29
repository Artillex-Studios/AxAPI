package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import org.bukkit.plugin.java.JavaPlugin;
import com.artillexstudios.axapi.utils.Version;

public class NMSHandlers {
    private static PacketEntityFactory packetEntityFactory;
    private static NMSHandler nmsHandler;

    // Do not mix with innit, bruv!
    private static boolean init(JavaPlugin plugin) {
        Version version = Version.getServerVersion();

        if (packetEntityFactory == null) {
            try {
                packetEntityFactory = (PacketEntityFactory) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.PacketEntityFactory", version.nmsVersion)).getConstructor().newInstance();
            } catch (Exception exception) {
                return false;
            }
        }

        if (nmsHandler == null) {
            try {
                nmsHandler = (NMSHandler) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.NMSHandler", version.nmsVersion)).getConstructor(JavaPlugin.class).newInstance(plugin);
            } catch (Exception exception) {
                return false;
            }
        }

        return true;
    }

    public static PacketEntityFactory getPacketEntityFactory() {
        if (packetEntityFactory == null) {
            throw new RuntimeException("PacketEntityFactory has not been initialised! This could be due to incorrect usage, or unsupported NMS version!");
        }

        return packetEntityFactory;
    }

    public static NMSHandler getNmsHandler() {
        if (packetEntityFactory == null) {
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
