package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataSerializer;
import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataSerializers;

import java.util.ArrayList;
import java.util.List;

public class ClientboundEntityMetadataWrapper extends PacketWrapper {
    private int entityId;
    private List<Metadata.DataItem<?>> items;

    public ClientboundEntityMetadataWrapper(PacketEvent event) {
        super(event);
    }

    public int entityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public List<Metadata.DataItem<?>> items() {
        return this.items;
    }

    public void setItems(List<Metadata.DataItem<?>> items) {
        this.items = items;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        for (Metadata.DataItem item : this.items) {
            int serializer = EntityDataSerializers.getId(item.serializer());
            out.writeByte(item.id());
            out.writeVarInt(serializer);
            item.serializer().write(out, item.getValue());
        }

        out.writeByte(255);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        items = new ArrayList<>();
        int i;
        while ((i = buf.readUnsignedByte()) != 255) {
            int serializerId = buf.readVarInt();
            EntityDataSerializer<?> serializer = EntityDataSerializers.byId(serializerId);
            items.add(new Metadata.DataItem(i, serializer, serializer.read(buf)));
        }
    }
}
