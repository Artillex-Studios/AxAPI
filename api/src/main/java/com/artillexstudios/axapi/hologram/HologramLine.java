package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.ArmorStandMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.ItemEntityMeta;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.FeatureFlags;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramLine {
    private static final Logger log = LoggerFactory.getLogger(HologramLine.class);
    private final Type type;
    private final ThreadSafeList<Placeholder> placeholders = new ThreadSafeList<>();
    private final HologramPage page;
    private volatile PacketEntity packetEntity = null;
    private volatile boolean hasPlaceholders = false;
    private String content;
    private Location location;

    public HologramLine(HologramPage page, Location location, String content, Type type, List<Placeholder> placeholders) {
        log.info("HologramLine ctor {}", placeholders);
        this.page = page;
        this.location = location;
        this.type = type;
        this.placeholders.addAll(placeholders);

        setContent(content);
    }

    public void remove() {
        Holograms.remove(packetEntity.id());
        packetEntity.remove();
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        // Reparse the placeholders
        log.info("addPlaceholder");

        setContent(content);
    }

    public boolean hasPlaceholders() {
        return hasPlaceholders;
    }

    public void setContent(String content) {
        this.content = content;
        if (packetEntity != null) {
            switch (type) {
                case ITEM_STACK: {
                    ItemEntityMeta meta = (ItemEntityMeta) packetEntity.meta();
                    meta.itemStack(NMSHandlers.getNmsHandler().wrapItem(content));
                    break;
                }
                case HEAD:
                case SMALL_HEAD: {
                    packetEntity.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(content));
                    break;
                }
                case TEXT: {
                    hasPlaceholders = false;
                    EntityMeta meta = packetEntity.meta();
                    if (!content.isEmpty()) {
                        for (int i = 0; i < placeholders.size(); i++) {
                            Placeholder placeholder = placeholders.get(i);
                            if (placeholder instanceof StaticPlaceholder) {
                                content = placeholder.parse(null, content);
                            }
                        }

                        for (Pattern pattern : FeatureFlags.PLACEHOLDER_PATTERNS.get()) {
                            Matcher matcher = pattern.matcher(content);
                            if (matcher.find()) {
                                hasPlaceholders = true;
                                break;
                            }
                        }

                        meta.customNameVisible(true);
                        meta.name(StringUtils.format(content));
                    } else {
                        meta.name(null);
                        meta.customNameVisible(false);
                    }
                    break;
                }
                case ENTITY: {
                    Holograms.remove(packetEntity.id());
                    packetEntity.remove();
                    packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.valueOf(content.toUpperCase(Locale.ENGLISH)), location);
                    packetEntity.spawn();
                    Holograms.put(packetEntity.id(), this);
                    break;
                }
                case UNKNOWN: {
                    break;
                }
            }

            return;
        }

        switch (type) {
            case ITEM_STACK: {
                packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.DROPPED_ITEM, location);
                ItemEntityMeta meta = (ItemEntityMeta) packetEntity.meta();
                meta.itemStack(NMSHandlers.getNmsHandler().wrapItem(content));
                meta.hasNoGravity(true);
                packetEntity.spawn();
                break;
            }
            case HEAD: {
                packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.ARMOR_STAND, location);
                packetEntity.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(this.content));
                EntityMeta meta = packetEntity.meta();
                meta.invisible(true);
                packetEntity.spawn();
                break;
            }
            case TEXT: {
                packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.ARMOR_STAND, location);
                log.info("Spawn");
                AtomicReference<String> reference = new AtomicReference<>(content);
                ArmorStandMeta meta = (ArmorStandMeta) packetEntity.meta();
                hasPlaceholders = false;
                if (!this.content.isEmpty()) {
                    for (int i = 0; i < placeholders.size(); i++) {
                        Placeholder placeholder = placeholders.get(i);
                        if (placeholder instanceof StaticPlaceholder) {
                            reference.set(placeholder.parse(null, reference.get()));
                        }
                    }

                    for (Pattern pattern : FeatureFlags.PLACEHOLDER_PATTERNS.get()) {
                        Matcher matcher = pattern.matcher(reference.get());
                        if (matcher.find()) {
                            log.info("HasPlaceholders");
                            hasPlaceholders = true;
                            break;
                        }
                    }

                    meta.customNameVisible(true);
                    meta.name(StringUtils.format(reference.get()));
                } else {
                    meta.customNameVisible(false);
                    meta.name(null);
                }

                meta.marker(true);
                meta.invisible(true);
                log.info("entity spawn");
                packetEntity.spawn();
                break;
            }
            case ENTITY: {
                packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.valueOf(content.toUpperCase(Locale.ENGLISH)), location);
                packetEntity.spawn();
                break;
            }
            case SMALL_HEAD: {
                packetEntity = NMSHandlers.getNmsHandler().createEntity(EntityType.ARMOR_STAND, location);
                ArmorStandMeta meta = (ArmorStandMeta) packetEntity.meta();
                packetEntity.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(this.content));
                meta.invisible(true);
                meta.small(true);
                packetEntity.spawn();
                break;
            }
            case UNKNOWN: {
                break;
            }
        }

        if (packetEntity != null) {
            packetEntity.onInteract(event -> {
                page.hologram().changePage(event.getPlayer(), event.isAttack() ? Hologram.PageChangeDirection.BACK : Hologram.PageChangeDirection.FORWARD);
            });

            if (!page.isFirstPage()) {
                packetEntity.setVisibleByDefault(false);
            }

            Holograms.put(packetEntity.id(), this);
        }
    }

    public void teleport(Location location) {
        this.location = location;
        this.packetEntity.teleport(location);
    }

    public void hide(Player player) {
        packetEntity.hide(player);
    }

    public void show(Player player) {
        packetEntity.show(player);
    }

    public Type type() {
        return type;
    }

    public void update() {
        packetEntity.update();
    }

    public ThreadSafeList<Placeholder> placeholders() {
        return placeholders;
    }

    public String content() {
        return content;
    }

    public enum Type {
        TEXT,
        ITEM_STACK,
        HEAD,
        SMALL_HEAD,
        ENTITY,
        UNKNOWN,
        TEXT_DISPLAY // Not yet implemented
    }
}
