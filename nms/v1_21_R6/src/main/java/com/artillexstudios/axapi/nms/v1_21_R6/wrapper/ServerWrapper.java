package com.artillexstudios.axapi.nms.v1_21_R6.wrapper;

import com.artillexstudios.axapi.nms.v1_21_R6.packet.PacketTransformer;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;

import java.util.concurrent.atomic.AtomicInteger;

public final class ServerWrapper implements com.artillexstudios.axapi.nms.wrapper.ServerWrapper {
    public static final ServerWrapper INSTANCE = new ServerWrapper();
    private static final MinecraftServer server = MinecraftServer.getServer();
    private static final FieldAccessor entityCounterAccessor = FieldAccessor.builder()
            .withClass(Entity.class)
            .withField("c")
            .build();
    private static final AtomicInteger entityCounter = entityCounterAccessor.get(null, AtomicInteger.class);

    @Override
    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }

    @Override
    public OfflinePlayer getCachedOfflinePlayer(String name) {
        OfflinePlayer result = Bukkit.getPlayerExact(name);
        if (result != null) {
            return result;
        }

        UserNameToIdResolver profileCache = server.services().nameToIdCache();

        NameAndId profile = profileCache.getIfCached(name);
        if (profile == null) {
            return null;
        }

        return ((CraftServer) Bukkit.getServer()).getOfflinePlayer(profile);
    }

    @Override
    public Object transformPacket(FriendlyByteBuf buf) {
        return PacketTransformer.transformClientbound(buf);
    }

    @Override
    public void update(boolean force) {

    }

    @Override
    public Server wrapped() {
        return server.server;
    }

    @Override
    public MinecraftServer asMinecraft() {
        return server;
    }
}
