package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.position.BlockPosition;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.Arrays;

public final class ServerboundSignUpdateWrapper extends PacketWrapper {
    private BlockPosition position;
    private boolean frontText;
    private String[] lines;

    public ServerboundSignUpdateWrapper(PacketEvent event) {
        super(event);
    }

    public BlockPosition position() {
        return position;
    }

    public void position(BlockPosition position) {
        this.position = position;
    }

    public boolean frontText() {
        return this.frontText;
    }

    public void frontText(boolean frontText) {
        this.frontText = frontText;
    }

    public String[] lines() {
        return this.lines;
    }

    public void lines(String[] lines) {
        this.lines = lines;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeBlockPos(this.position);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_1)) {
            out.writeBoolean(this.frontText);
        }

        for (int i = 0; i < 4; i++) {
            out.writeUTF(this.lines[i]);
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.position = buf.readBlockPosition();
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_1)) {
            this.frontText = buf.readBoolean();
        } else {
            this.frontText = true;
        }

        this.lines = new String[4];
        for (int i = 0; i < 4; i++) {
            this.lines[i] = buf.readUTF(384);
        }

        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Read lines from sign: {}", Arrays.toString(this.lines));
        }
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.SIGN_UPDATE;
    }
}
