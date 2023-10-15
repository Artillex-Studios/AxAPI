package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.hologram.HologramFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NMSHandlers {
    private static PacketEntityFactory packetEntityFactory;
    private static HologramFactory hologramFactory;
    private static NMSHandler nmsHandler;
    private static String version;

    public static void initialise(JavaPlugin plugin) {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf('.') + 1);

        if (packetEntityFactory == null) {
            try {
                packetEntityFactory = (PacketEntityFactory) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.PacketEntityFactory", version)).getConstructor().newInstance();
            } catch (Exception exception) {

            }
        }

        if (hologramFactory == null) {
            try {
                hologramFactory = (HologramFactory) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.HologramFactory", version)).getConstructor().newInstance();
            } catch (Exception exception) {

            }
        }

        if (nmsHandler == null) {
            try {
                nmsHandler = (NMSHandler) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.NMSHandler", version)).getConstructor(JavaPlugin.class).newInstance(plugin);
            } catch (Exception exception) {

            }
        }
    }

    public static PacketEntityFactory getPacketEntityFactory() {
        if (packetEntityFactory == null) {
            throw new RuntimeException("PacketEntityFactory has not been initialised! This could be due to incorrect usage, or unsupported NMS version!");
        }

        return packetEntityFactory;
    }

    public static HologramFactory getHologramFactory() {
        if (packetEntityFactory == null) {
            throw new RuntimeException("HologramFactory has not been initialised! This could be due to incorrect usage, or unsupported NMS version!");
        }

        return hologramFactory;
    }

    public static NMSHandler getNmsHandler() {
        if (packetEntityFactory == null) {
            throw new RuntimeException("NMSHandler has not been initialised! This could be due to incorrect usage, or unsupported NMS version!");
        }

        return nmsHandler;
    }
}
