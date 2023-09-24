package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.hologram.HologramFactory;
import org.bukkit.Bukkit;

public class NMSHandlers {
    private static PacketEntityFactory packetEntityFactory;
    private static HologramFactory hologramFactory;
    private static NMSHandler nmsHandler;
    private static String version;

    public static void initialise() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf('.') + 1);

        if (packetEntityFactory == null) {
            try {
                packetEntityFactory = (PacketEntityFactory) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.PacketEntityFactory", version)).getConstructor().newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (hologramFactory == null) {
            try {
                hologramFactory = (HologramFactory) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.HologramFactory", version)).getConstructor().newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (nmsHandler == null) {
            try {
                nmsHandler = (NMSHandler) Class.forName(String.format("com.artillexstudios.axapi.nms.%s.NMSHandler", version)).getConstructor().newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static PacketEntityFactory getPacketEntityFactory() {
        return packetEntityFactory;
    }

    public static HologramFactory getHologramFactory() {
        return hologramFactory;
    }

    public static NMSHandler getNmsHandler() {
        return nmsHandler;
    }
}
