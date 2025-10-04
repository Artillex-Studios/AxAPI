package com.artillexstudios.axapi.utils.sound;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSoundWrapper;
import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class Sound {
    private SoundEvent soundEvent;
    private SoundSource source;
    private double x;
    private double y;
    private double z;
    private float volume;
    private float pitch;
    private long seed;
    private ClientboundSoundWrapper packet;

    public static SoundEvent createSoundEvent(String sound) {
        return SoundEvent.createVariableRange(Key.key(sound));
    }

    public static void playSound(String event, Player player) {
        create(createSoundEvent(event), player.getLocation()).send(player);
    }

    public static void playSound(SoundEvent event, Player player) {
        create(event, player.getLocation()).send(player);
    }

    public static void playSound(SoundEvent event, SoundSource source, Player player) {
        create(event, source, player.getLocation()).send(player);
    }

    public static void playSound(SoundEvent event, SoundSource source, float volume, Player player) {
        create(event, source, player.getLocation(), volume).send(player);
    }

    public static void playSound(SoundEvent event, SoundSource source, float volume, float pitch, Player player) {
        create(event, source, player.getLocation(), volume, pitch).send(player);
    }

    public static void playSound(SoundEvent event, SoundSource source, float volume, float pitch, long seed, Player player) {
        create(event, source, player.getLocation(), volume, pitch, seed).send(player);
    }

    public static Sound create(SoundEvent event, Location location) {
        return create(event, SoundSource.MASTER, location);
    }

    public static Sound create(SoundEvent event, SoundSource source, Location location) {
        return create(event, source, location, 1.0f);
    }

    public static Sound create(SoundEvent event, SoundSource source, Location location, float volume) {
        return create(event, source, location, volume, 1.0f);
    }

    public static Sound create(SoundEvent event, SoundSource source, Location location, float volume, float pitch) {
        return create(event, source, location, volume, pitch, ThreadLocalRandom.current().nextLong());
    }

    public static Sound create(SoundEvent event, SoundSource source, Location location, float volume, float pitch, long seed) {
        return new Sound(event, source, location.x(), location.y(), location.z(), volume, pitch, seed);
    }

    Sound(SoundEvent soundEvent, SoundSource source, double x, double y, double z, float volume, float pitch, long seed) {
        this.soundEvent = soundEvent;
        this.source = source;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
        this.updatePacket();
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public void setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        this.updatePacket();
    }

    public SoundSource getSource() {
        return this.source;
    }

    public void setSource(SoundSource source) {
        this.source = source;
        this.updatePacket();
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
        this.updatePacket();
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
        this.updatePacket();
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
        this.updatePacket();
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        this.updatePacket();
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.updatePacket();
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        this.updatePacket();
    }

    private void updatePacket() {
        this.packet = new ClientboundSoundWrapper(this.soundEvent, this.source, this.x, this.y, this.z, this.volume, this.pitch, this.seed);
    }

    public void send(Player player) {
        this.send(ServerPlayerWrapper.wrap(player));
    }

    public void send(ServerPlayerWrapper wrapper) {
        Preconditions.checkNotNull(this.packet, "Can't send a not yet constructed Sound!");
        wrapper.sendPacket(this.packet);
    }
}
