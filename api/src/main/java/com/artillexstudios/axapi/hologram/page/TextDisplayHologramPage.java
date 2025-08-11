package com.artillexstudios.axapi.hologram.page;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramType;
import com.artillexstudios.axapi.hologram.HologramTypes;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.TextDisplayMeta;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextDisplayHologramPage extends HologramPage<String, HologramType<String>> {
    private final PacketEntity textDisplay;
    private boolean containsPlaceholders;
    private String content;
    private boolean spawned = false;

    public TextDisplayHologramPage(Hologram hologram, boolean firstPage, Location location) {
        super(hologram, firstPage, location);
        this.textDisplay = NMSHandlers.getNmsHandler().createEntity(EntityType.TEXT_DISPLAY, this.getLocation());
    }

    @Override
    public void setContent(String content) {
        this.content = content;

        for (Pattern pattern : FeatureFlags.PLACEHOLDER_PATTERNS.get()) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                this.containsPlaceholders = true;
                break;
            }
        }

        this.create();
    }

    private void create() {
        TextDisplayMeta meta = (TextDisplayMeta) this.textDisplay.meta();
        meta.component(StringUtils.format(this.content));
        if (this.getEntityMetaHandler() != null) {
            this.getEntityMetaHandler().accept(meta);
        }

        if (!this.isFirstPage()) {
            this.textDisplay.setVisibleByDefault(false);
        }

        // TODO: Interaction entity for these
        this.textDisplay.onInteract(event -> {
            if (this.getClickHandler() != null) {
                this.getClickHandler().accept(event);
            }

            this.getHologram().changePage(event.getPlayer(), event.isAttack() ? Hologram.PageChangeDirection.BACK : Hologram.PageChangeDirection.FORWARD);
        });
    }


    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public HologramType<String> getType() {
        return HologramTypes.TEXT;
    }

    @Override
    public boolean containsPlaceholders() {
        return this.containsPlaceholders;
    }

    @Override
    public boolean contentEquals(HologramPage<?, ?> other) {
        if (!(other instanceof TextDisplayHologramPage textPage)) {
            return false;
        }

        return textPage.getContent().equals(this.getContent());
    }

    @Override
    public void hide(Player player) {
        this.textDisplay.hide(player);
    }

    @Override
    public void show(Player player) {
        this.textDisplay.show(player);
    }

    @Override
    public void teleport(Location location) {
        if (this.textDisplay == null) {
            return;
        }

        this.setLocation(location);
        this.textDisplay.teleport(location);
    }

    @Override
    public void remove() {
        if (this.textDisplay == null) {
            return;
        }

        this.spawned = false;
        Holograms.remove(this.textDisplay.id());
        this.textDisplay.remove();
    }

    @Override
    public void update() {
        if (this.textDisplay == null) {
            return;
        }

        this.textDisplay.update();
    }

    @Override
    public EntityMeta getEntityMeta() {
        return this.textDisplay.meta();
    }

    @Override
    public synchronized boolean spawn() {
        if (this.spawned) {
            return false;
        }

        this.textDisplay.spawn();
        Holograms.put(this.textDisplay.id(), this);
        this.spawned = true;
        return true;
    }
}
