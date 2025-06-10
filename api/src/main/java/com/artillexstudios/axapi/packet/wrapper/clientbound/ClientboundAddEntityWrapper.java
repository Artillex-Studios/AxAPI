package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

import java.util.UUID;

public final class ClientboundAddEntityWrapper extends PacketWrapper {
    private int entityId;
    private UUID uuid;
    private int entityType;
    private double x;
    private double y;
    private double z;
    private byte pitch;
    private byte yaw;
    private byte headYaw;
    private int data;
    private short velocityX;
    private short velocityY;
    private short velocityZ;

    public ClientboundAddEntityWrapper(PacketEvent event) {
        super(event);
    }

    public ClientboundAddEntityWrapper(int entityId, UUID uuid, int entityType, double x, double y, double z, byte pitch, byte yaw, byte headYaw, int data, short velocityX, short velocityY, short velocityZ) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.entityType = entityType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.data = data;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public int entityId() {
        return this.entityId;
    }

    public void entityId(int entityId) {
        this.entityId = entityId;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public void uuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double x() {
        return this.x;
    }

    public void x(double x) {
        this.x = x;
    }

    public double y() {
        return this.y;
    }

    public void y(double y) {
        this.y = y;
    }

    public double z() {
        return this.z;
    }

    public void z(double z) {
        this.z = z;
    }

    public byte pitch() {
        return this.pitch;
    }

    public void pitch(byte pitch) {
        this.pitch = pitch;
    }

    public byte yaw() {
        return this.yaw;
    }

    public void yaw(byte yaw) {
        this.yaw = yaw;
    }

    public byte headYaw() {
        return this.headYaw;
    }

    public void headYaw(byte headYaw) {
        this.headYaw = headYaw;
    }

    public int data() {
        return this.data;
    }

    public void data(int data) {
        this.data = data;
    }

    public short velocityX() {
        return this.velocityX;
    }

    public void velocityX(short velocityX) {
        this.velocityX = velocityX;
    }

    public short velocityY() {
        return this.velocityY;
    }

    public void velocityY(short velocityY) {
        this.velocityY = velocityY;
    }

    public short velocityZ() {
        return this.velocityZ;
    }

    public void velocityZ(short velocityZ) {
        this.velocityZ = velocityZ;
    }

    public int entityType() {
        return this.entityType;
    }

    public void entityType(int entityType) {
        this.entityType = entityType;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeVarInt(this.entityType);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte(this.pitch);
        out.writeByte(this.yaw);
        out.writeByte(this.headYaw);
        out.writeVarInt(this.data);
        out.writeShort(this.velocityX);
        out.writeShort(this.velocityY);
        out.writeShort(this.velocityZ);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.uuid = buf.readUUID();
        this.entityType = buf.readVarInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.headYaw = buf.readByte();
        this.data = buf.readVarInt();
        this.velocityX = buf.readShort();
        this.velocityY = buf.readShort();
        this.velocityZ = buf.readShort();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.ADD_ENTITY;
    }
}
