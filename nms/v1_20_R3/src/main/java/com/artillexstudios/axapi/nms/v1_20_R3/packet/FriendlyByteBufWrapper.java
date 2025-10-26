package com.artillexstudios.axapi.nms.v1_20_R3.packet;

import com.artillexstudios.axapi.items.HashedStack;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.Vector3d;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;

import java.util.Arrays;
import java.util.Optional;

public record FriendlyByteBufWrapper(net.minecraft.network.FriendlyByteBuf buf) implements FriendlyByteBuf {

    @Override
    public WrappedItemStack readItemStack(com.artillexstudios.axapi.items.WrappedItemStack.CodecData codecData) {
        return switch (codecData) {
            case OPTIONAL_UNTRUSTED_STREAM_CODEC, OPTIONAL_STREAM_CODEC, STREAM_CODEC ->
                    new com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack(this.buf.readItem());
        };
    }

    @Override
    public void writeItemStack(com.artillexstudios.axapi.items.WrappedItemStack wrappedItemStack, WrappedItemStack.CodecData codecData) {
        switch (codecData) {
            case OPTIONAL_UNTRUSTED_STREAM_CODEC, OPTIONAL_STREAM_CODEC, STREAM_CODEC ->
                    this.buf.writeItem(((com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack) wrappedItemStack).asMinecraft());
        }
    }

    @Override
    public byte[] readByteArray() {
        return this.buf.readByteArray();
    }

    @Override
    public byte[] readByteArray(int maxSize) {
        return this.buf.readByteArray(maxSize);
    }

    @Override
    public void writeByteArray(byte[] bytes) {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Writing byteArray: {}", Arrays.toString(bytes));
        }
        this.buf.writeByteArray(bytes);
    }

    @Override
    public Component readComponent() {
        return ComponentSerializer.INSTANCE.fromVanilla(this.buf.readComponentTrusted());
    }

    @Override
    public void writeComponent(Component component) {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Writing component: {}", component);
        }

        this.buf.writeComponent(ComponentSerializer.INSTANCE.<net.minecraft.network.chat.Component>toVanilla(component));
    }

    @Override
    public int readVarInt() {
        return this.buf.readVarInt();
    }

    @Override
    public void writeVarInt(int varInt) {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Writing varint: {}", varInt);
        }

        this.buf.writeVarInt(varInt);
    }

    @Override
    public CompoundTag readNBT() {
        return new com.artillexstudios.axapi.nms.v1_20_R3.items.nbt.CompoundTag(this.buf.readNbt());
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        this.buf.writeNbt((net.minecraft.nbt.CompoundTag) tag.getParent());
    }

    @Override
    public String readUTF() {
        return this.buf.readUtf();
    }

    @Override
    public String readUTF(int length) {
        return this.buf.readUtf(length);
    }

    @Override
    public void writeUTF(String utf) {
        this.buf.writeUtf(utf);
    }

    @Override
    public Key readResourceLocation() {
        ResourceLocation resourceLocation = this.buf.readResourceLocation();
        return Key.key(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    @Override
    public void writeResourceLocation(Key key) {
        this.buf.writeResourceLocation(ResourceLocation.tryBuild(key.namespace(), key.value()));
    }

    @Override
    public void writeFloat(float value) {
        this.buf.writeFloat(value);
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public void writeShort(int value) {
        this.buf.writeShort(value);
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public void writeDouble(double value) {
        this.buf.writeDouble(value);
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public void writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public void writeVarLong(long value) {
        this.buf.writeVarLong(value);
    }

    @Override
    public long readVarLong() {
        return this.buf.readVarLong();
    }

    @Override
    public void writeLong(long value) {
        this.buf.writeLong(value);
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public void writeByte(int value) {
        this.buf.writeByte(value);
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public void readerIndex(int index) {
        this.buf.readerIndex(index);
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public void writerIndex(int index) {
        this.buf.writerIndex(index);
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }


    @Override
    public WrappedItemStack readItemCost() {
        return new com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack(this.buf.readItem());
    }

    @Override
    public void writeItemCost(WrappedItemStack itemCost) {
        this.buf.writeItem(((com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack) itemCost).itemStack);
    }

    @Override
    public Optional<WrappedItemStack> readOptionalItemCost() {
        return Optional.of(this.buf.readItem()).map(com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack::new);
    }

    @Override
    public void writeOptionalItemCost(Optional<WrappedItemStack> itemCost) {
        WrappedItemStack item = itemCost.orElse(WrappedItemStack.wrap(new org.bukkit.inventory.ItemStack(Material.AIR)));
        this.buf.writeItem(((com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack) item).itemStack);
    }

    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public void writeBlockData(BlockData blockData) {
        this.buf.writeVarInt(Block.getId(((CraftBlockData) blockData).getState()));
    }

    @Override
    public BlockData readBlockData() {
        return CraftBlockData.fromData(Block.BLOCK_STATE_REGISTRY.byId(this.readVarInt()));
    }

    @Override
    public Vector3d readLpVec3() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeLpVec3(Vector3d vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HashedStack readHashedStack() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeHashedStack(HashedStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(FriendlyByteBuf buf) {
        this.buf.writeBytes(((FriendlyByteBufWrapper) buf).buf());
    }

    @Override
    public FriendlyByteBuf readBytes(int length) {
        return PacketTransformer.wrap(this.buf.readBytes(length));
    }

    @Override
    public FriendlyByteBuf copy() {
        return PacketTransformer.copy(this);
    }

    @Override
    public FriendlyByteBuf slice(int beginIndex, int length) {
        return PacketTransformer.wrap(this.buf.slice(beginIndex, length));
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public void release() {
        this.buf.release();
    }
}
