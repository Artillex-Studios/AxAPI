package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.BlockPosition;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public final class ServerboundSignUpdateWrapper extends PacketWrapper {
    private BlockPosition position;
    private boolean frontText;
    private Component[] lines;

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

    public Component[] lines() {
        return this.lines;
    }

    public void lines(Component[] lines) {
        this.lines = lines;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeBlockPos(this.position);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_1)) {
            out.writeBoolean(this.frontText);
        }

        for (int i = 0; i < 4; i++) {
            out.writeUTF(LegacyComponentSerializer.legacySection().serialize(this.lines[i]));
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

        List<String> lines = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            lines.add(buf.readUTF(384));
        }
        this.lines = StringUtils.formatList(lines).toArray(new net.kyori.adventure.text.Component[0]);
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.SIGN_UPDATE;
    }
}
