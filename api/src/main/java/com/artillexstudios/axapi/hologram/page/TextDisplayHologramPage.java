package com.artillexstudios.axapi.hologram.page;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramType;
import com.artillexstudios.axapi.hologram.HologramTypes;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.TextDisplayMeta;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextDisplayHologramPage extends HologramPage<String, HologramType<String>> {
    private static final Pattern NEW_LINE = Pattern.compile("\n");
    private final PacketEntity textDisplay;
    private boolean containsPlaceholders;
    private String content;
    private boolean spawned = false;
    private LineData[] data;

    public TextDisplayHologramPage(Hologram hologram, boolean firstPage, Location location) {
        super(hologram, firstPage, location);
        this.textDisplay = NMSHandlers.getNmsHandler().createEntity(EntityType.TEXT_DISPLAY, this.getLocation());
    }

    @Override
    public void setContent(String content) {
        this.content = content;
        this.containsPlaceholders = false;

        String[] split = NEW_LINE.split(content);
        this.data = new LineData[split.length];
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            boolean containsPlaceholders = false;
            // Add a newline that we removed
            Component formatted = StringUtils.format(line + (i == split.length - 1 ? "" : "\n"));
            // Remove all MiniMessage tags from the component so they don't interfere with the placeholder checking
            String legacy = PlainTextComponentSerializer.plainText().serialize(formatted);
            for (Pattern pattern : FeatureFlags.PLACEHOLDER_PATTERNS.get()) {
                Matcher matcher = pattern.matcher(legacy);
                if (matcher.find()) {
                    containsPlaceholders = true;
                    this.containsPlaceholders = true;
                    break;
                }
            }

            if (legacy.isBlank()) {
                formatted = Component.newline();
            }

            // Cache the formatted component so we can just slap it on
            this.data[i] = new LineData(line, ComponentSerializer.INSTANCE.toVanilla(containsPlaceholders ? Component.empty() : formatted), containsPlaceholders);
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
        this.setLocation(location);
        if (this.textDisplay == null) {
            return;
        }

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

    public LineData[] getData() {
        return this.data;
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
