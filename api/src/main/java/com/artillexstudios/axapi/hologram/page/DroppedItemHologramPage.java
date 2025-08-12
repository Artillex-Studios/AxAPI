package com.artillexstudios.axapi.hologram.page;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramType;
import com.artillexstudios.axapi.hologram.HologramTypes;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.ItemEntityMeta;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DroppedItemHologramPage extends HologramPage<WrappedItemStack, HologramType<WrappedItemStack>> {
    private final PacketEntity droppedItem;
    private WrappedItemStack content;
    private boolean spawned = false;

    public DroppedItemHologramPage(Hologram hologram, boolean firstPage, Location location) {
        super(hologram, firstPage, location);
        this.droppedItem = NMSHandlers.getNmsHandler().createEntity(EntityType.ITEM_DISPLAY, this.getLocation());
    }

    @Override
    public void setContent(WrappedItemStack content) {
        this.content = content;

        this.create();
    }

    private void create() {
        ItemEntityMeta meta = (ItemEntityMeta) this.droppedItem.meta();
        meta.itemStack(this.content);
        meta.hasNoGravity(true);
        if (this.getEntityMetaHandler() != null) {
            this.getEntityMetaHandler().accept(meta);
        }

        if (!this.isFirstPage()) {
            this.droppedItem.setVisibleByDefault(false);
        }

        this.droppedItem.onInteract(event -> {
            if (this.getClickHandler() != null) {
                this.getClickHandler().accept(event);
            }

            this.getHologram().changePage(event.getPlayer(), event.isAttack() ? Hologram.PageChangeDirection.BACK : Hologram.PageChangeDirection.FORWARD);
        });
    }

    @Override
    public WrappedItemStack getContent() {
        return this.content;
    }

    @Override
    public HologramType<WrappedItemStack> getType() {
        return HologramTypes.ITEM_STACK;
    }

    @Override
    public boolean containsPlaceholders() {
        return false;
    }

    @Override
    public boolean contentEquals(HologramPage<?, ?> other) {
        if (!(other instanceof DroppedItemHologramPage droppedItemPage)) {
            return false;
        }

        return droppedItemPage.getContent().equals(this.getContent());
    }

    @Override
    public void hide(Player player) {
        this.droppedItem.hide(player);
    }

    @Override
    public void show(Player player) {
        this.droppedItem.show(player);
    }

    @Override
    public void teleport(Location location) {
        this.setLocation(location);
        if (this.droppedItem == null) {
            return;
        }

        this.droppedItem.teleport(location);
    }

    @Override
    public void remove() {
        if (this.droppedItem == null) {
            return;
        }

        this.spawned = false;
        Holograms.remove(this.droppedItem.id());
        this.droppedItem.remove();
    }

    @Override
    public void update() {
        if (this.droppedItem == null) {
            return;
        }

        this.droppedItem.update();
    }

    @Override
    public EntityMeta getEntityMeta() {
        return this.droppedItem.meta();
    }

    @Override
    public synchronized boolean spawn() {
        if (this.spawned) {
            return false;
        }

        this.droppedItem.spawn();
        Holograms.put(this.droppedItem.id(), this);
        this.spawned = true;
        return true;
    }
}
