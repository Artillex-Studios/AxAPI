package com.artillexstudios.axapi.nms.v1_20_R4.packet;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.Vector3d;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import java.util.Optional;

public record FriendlyByteBufWrapper(RegistryFriendlyByteBuf buf) implements FriendlyByteBuf {

    @Override
    public WrappedItemStack readItemStack(com.artillexstudios.axapi.items.WrappedItemStack.CodecData codecData) {
        return switch (codecData) {
            case OPTIONAL_UNTRUSTED_STREAM_CODEC, OPTIONAL_STREAM_CODEC ->
                    new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(ItemStack.OPTIONAL_STREAM_CODEC.decode(this.buf));
            case STREAM_CODEC ->
                    new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(ItemStack.STREAM_CODEC.decode(this.buf));
        };
    }

    @Override
    public void writeItemStack(com.artillexstudios.axapi.items.WrappedItemStack wrappedItemStack, WrappedItemStack.CodecData codecData) {
        switch (codecData) {
            case OPTIONAL_UNTRUSTED_STREAM_CODEC, OPTIONAL_STREAM_CODEC ->
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(this.buf, ((com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack) wrappedItemStack).asMinecraft());
            case STREAM_CODEC ->
                    ItemStack.STREAM_CODEC.encode(this.buf, ((com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack) wrappedItemStack).asMinecraft());
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
        this.buf.writeByteArray(bytes);
    }

    @Override
    public Component readComponent() {
        return ComponentSerializer.INSTANCE.fromVanilla(ComponentSerialization.TRUSTED_STREAM_CODEC.decode(this.buf));
    }

    @Override
    public void writeComponent(Component component) {
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, ComponentSerializer.INSTANCE.toVanilla(component));
    }

    @Override
    public int readVarInt() {
        return this.buf.readVarInt();
    }

    @Override
    public void writeVarInt(int varInt) {
        this.buf.writeVarInt(varInt);
    }

    @Override
    public CompoundTag readNBT() {
        return new com.artillexstudios.axapi.nms.v1_20_R4.items.nbt.CompoundTag(this.buf.readNbt());
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
        return new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(ItemCost.STREAM_CODEC.decode(this.buf).itemStack());
    }

    @Override
    public void writeItemCost(WrappedItemStack itemCost) {
        ItemStack nmsItem = ((com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack) itemCost).itemStack;
        ItemCost.STREAM_CODEC.encode(this.buf, new ItemCost(nmsItem.getItemHolder(), nmsItem.getCount(), DataComponentPredicate.allOf(PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, nmsItem.getComponentsPatch()))));
    }

    @Override
    public Optional<WrappedItemStack> readOptionalItemCost() {
        return ItemCost.OPTIONAL_STREAM_CODEC.decode(this.buf).map(cost -> new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(cost.itemStack()));
    }

    @Override
    public void writeOptionalItemCost(Optional<WrappedItemStack> itemCost) {
        ItemCost.OPTIONAL_STREAM_CODEC.encode(this.buf, itemCost.map(stack -> {
            ItemStack nmsItem = ((com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack) stack).itemStack;
            return new ItemCost(nmsItem.getItemHolder(), nmsItem.getCount(), DataComponentPredicate.allOf(PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, nmsItem.getComponentsPatch())));
        }));
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
