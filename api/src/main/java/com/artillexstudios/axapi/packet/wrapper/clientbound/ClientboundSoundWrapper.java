package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.sound.SoundEvent;
import com.artillexstudios.axapi.utils.sound.SoundSource;
import net.kyori.adventure.key.Key;

public final class ClientboundSoundWrapper extends PacketWrapper {
    private SoundEvent soundEvent;
    private SoundSource source;
    private int x;
    private int y;
    private int z;
    private float volume;
    private float pitch;
    private long seed;

    public ClientboundSoundWrapper(SoundEvent soundEvent, SoundSource source, double x, double z, double y, float volume, float pitch, long seed) {
        this.soundEvent = soundEvent;
        this.source = source;
        this.x = (int) (x * 8.0);
        this.y = (int) (y * 8.0);
        this.z = (int) (z * 8.0);
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    public ClientboundSoundWrapper(PacketEvent event) {
        super(event);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public void setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public SoundSource getSource() {
        return this.source;
    }

    public void setSource(SoundSource source) {
        this.source = source;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeResourceLocation(this.soundEvent.getResourceLocation());
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
            out.writeBoolean(this.soundEvent.isUseNewSystem());
            if (this.soundEvent.isUseNewSystem()) {
                out.writeFloat(this.soundEvent.getRange());
            }
        } else {
            out.writeVarInt(0);
        }
        out.writeEnum(this.source);
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        Key key = buf.readResourceLocation();
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
            boolean newSystem = buf.readBoolean();
            this.soundEvent = newSystem ? SoundEvent.createFixedRange(key, buf.readFloat()) : SoundEvent.createVariableRange(key);
        } else {
            buf.readVarInt();
        }
        this.source = buf.readEnum(SoundSource.class);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.seed = buf.readLong();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SOUND;
    }
}
