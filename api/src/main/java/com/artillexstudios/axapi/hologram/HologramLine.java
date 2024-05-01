package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.entity.impl.PacketArmorStand;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.entity.impl.PacketItem;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.FeatureFlags;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramLine {
    private final Type type;
    private final ThreadSafeList<Placeholder> placeholders = new ThreadSafeList<>();
    private final HologramPage page;
    private PacketEntity packetEntity = null;
    private String content;
    private Location location;
    private boolean hasPlaceholders = false;

    public HologramLine(HologramPage page, Location location, String content, Type type) {
        this.page = page;
        this.location = location;
        this.type = type;
        setContent(content);
    }

    public void remove() {
        Holograms.remove(packetEntity.getEntityId());
        packetEntity.remove();
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        // Reparse the placeholders
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
                    PacketItem item = (PacketItem) packetEntity;
                    item.setItemStack(NMSHandlers.getNmsHandler().wrapItem(content).toBukkit());
                    break;
                }
                case HEAD:
                case SMALL_HEAD: {
                    packetEntity.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(content).toBukkit());
                    break;
                }
                case TEXT: {
                    hasPlaceholders = false;

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

                        packetEntity.setName(StringUtils.format(content));
                    } else {
                        packetEntity.setName(null);
                    }
                    break;
                }
                case ENTITY: {
                    Holograms.remove(packetEntity.getEntityId());
                    packetEntity.remove();
                    packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.valueOf(content.toUpperCase(Locale.ENGLISH)));
                    Holograms.put(packetEntity.getEntityId(), this);
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
                packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.DROPPED_ITEM);
                PacketItem item = (PacketItem) packetEntity;
                item.setItemStack(NMSHandlers.getNmsHandler().wrapItem(this.content).toBukkit());
                item.setGravity(false);
                break;
            }
            case HEAD: {
                packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.ARMOR_STAND);
                packetEntity.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(this.content).toBukkit());
                packetEntity.setInvisible(true);
                break;
            }
            case TEXT: {
                packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.ARMOR_STAND);

                hasPlaceholders = false;
                if (!this.content.isEmpty()) {
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

                    packetEntity.setName(StringUtils.format(content));
                } else {
                    packetEntity.setName(null);
                }

                packetEntity.setInvisible(true);
                break;
            }
            case ENTITY: {
                packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.valueOf(content.toUpperCase(Locale.ENGLISH)));
                break;
            }
            case SMALL_HEAD: {
                packetEntity = PacketEntityFactory.get().spawnEntity(location, EntityType.ARMOR_STAND);

                PacketArmorStand armorStand = (PacketArmorStand) packetEntity;
                armorStand.setItem(EquipmentSlot.HELMET, NMSHandlers.getNmsHandler().wrapItem(this.content).toBukkit());
                armorStand.setInvisible(true);
                armorStand.setSmall(true);
                break;
            }
            case UNKNOWN: {
                break;
            }
        }

        if (packetEntity != null) {
            packetEntity.onClick(event -> {
                System.out.println("CLICK!");
                page.hologram().changePage(event.getPlayer(), event.isAttack() ? Hologram.PageChangeDirection.BACK : Hologram.PageChangeDirection.FORWARD);
            });
            if (page.isFirstPage()) {
                System.out.println("FIRST PAGE!");
                for (int i = 0; i < page.lines().size(); i++) {
                    HologramLine line = page.lines().get(i);
                    line.packetEntity.setVisibleByDefault(true);
                }
            } else {
                for (int i = 0; i < page.lines().size(); i++) {
                    HologramLine line = page.lines().get(i);
                    line.packetEntity.setVisibleByDefault(false);
                }
            }

            Holograms.put(packetEntity.getEntityId(), this);
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
        packetEntity.sendMetaUpdate();
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
        UNKNOWN
    }
}
