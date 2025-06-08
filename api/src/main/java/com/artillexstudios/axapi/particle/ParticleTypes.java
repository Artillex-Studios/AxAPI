package com.artillexstudios.axapi.particle;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.option.ColorParticleOption;
import com.artillexstudios.axapi.particle.option.DustColorTransitionParticleOption;
import com.artillexstudios.axapi.particle.option.DustParticleOption;
import com.artillexstudios.axapi.particle.option.FloatParticleOption;
import com.artillexstudios.axapi.particle.option.IntegerParticleOption;
import com.artillexstudios.axapi.particle.option.ItemStackParticleOption;
import com.artillexstudios.axapi.particle.option.TrailParticleOption;
import com.artillexstudios.axapi.particle.option.VibrationParticleOption;
import com.artillexstudios.axapi.particle.type.ColorParticleType;
import com.artillexstudios.axapi.particle.type.DustColorTransitionParticleType;
import com.artillexstudios.axapi.particle.type.DustParticleType;
import com.artillexstudios.axapi.particle.type.FloatParticleType;
import com.artillexstudios.axapi.particle.type.IntegerParticleType;
import com.artillexstudios.axapi.particle.type.ItemStackParticleType;
import com.artillexstudios.axapi.particle.type.SimpleParticleType;
import com.artillexstudios.axapi.particle.type.TrailParticleType;
import com.artillexstudios.axapi.particle.type.VarIntParticleType;
import com.artillexstudios.axapi.particle.type.VibrationParticleType;
import com.artillexstudios.axapi.utils.Maps;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class ParticleTypes {
    private static final Int2ObjectArrayMap<ParticleType<?>> registry = new Int2ObjectArrayMap<>();
    private static final Object2IntArrayMap<ParticleType<?>> reverseRegistry = new Object2IntArrayMap<>();
    public static final ParticleType<?> AMBIENT_ENTITY_EFFECT = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_CHERRY_LEAVES = new SimpleParticleType();
    public static final ParticleType<?> FALLING_CHERRY_LEAVES = new SimpleParticleType();
    public static final ParticleType<?> LANDING_CHERRY_LEAVES = new SimpleParticleType();
    public static final ParticleType<?> GUST_DUST = new SimpleParticleType();
    public static final ParticleType<?> GUST_EMITTER = new SimpleParticleType();
    public static final ParticleType<?> GUST_EMITTER_SMALL = new SimpleParticleType();
    public static final ParticleType<ColorParticleOption> TINTED_LEAVES = new ColorParticleType();
    public static final ParticleType<?> ANGRY_VILLAGER = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> BLOCK = new VarIntParticleType();
    public static final ParticleType<IntegerParticleOption> BLOCK_MARKER = new VarIntParticleType();
    public static final ParticleType<?> BUBBLE = new SimpleParticleType();
    public static final ParticleType<?> CLOUD = new SimpleParticleType();
    public static final ParticleType<?> CRIT = new SimpleParticleType();
    public static final ParticleType<?> DAMAGE_INDICATOR = new SimpleParticleType();
    public static final ParticleType<?> DRAGON_BREATH = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_LAVA = new SimpleParticleType();
    public static final ParticleType<?> FALLING_LAVA = new SimpleParticleType();
    public static final ParticleType<?> LANDING_LAVA = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_WATER = new SimpleParticleType();
    public static final ParticleType<?> FALLING_WATER = new SimpleParticleType();
    public static final ParticleType<DustParticleOption> DUST = new DustParticleType();
    public static final ParticleType<DustColorTransitionParticleOption> DUST_COLOR_TRANSITION = new DustColorTransitionParticleType();
    public static final ParticleType<?> EFFECT = new SimpleParticleType();
    public static final ParticleType<?> ELDER_GUARDIAN = new SimpleParticleType();
    public static final ParticleType<?> ENCHANTED_HIT = new SimpleParticleType();
    public static final ParticleType<?> ENCHANT = new SimpleParticleType();
    public static final ParticleType<?> END_ROD = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> ENTITY_EFFECT = new IntegerParticleType();
    public static final ParticleType<?> EXPLOSION_EMITTER = new SimpleParticleType();
    public static final ParticleType<?> EXPLOSION = new SimpleParticleType();
    public static final ParticleType<?> GUST = new SimpleParticleType();
    public static final ParticleType<?> SMALL_GUST = new SimpleParticleType();
    public static final ParticleType<?> GUST_EMITTER_LARGE = new SimpleParticleType();
    public static final ParticleType<?> SONIC_BOOM = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> FALLING_DUST = new VarIntParticleType();
    public static final ParticleType<?> FIREWORK = new SimpleParticleType();
    public static final ParticleType<?> FISHING = new SimpleParticleType();
    public static final ParticleType<?> FLAME = new SimpleParticleType();
    public static final ParticleType<?> INFESTED = new SimpleParticleType();
    public static final ParticleType<?> CHERRY_LEAVES = new SimpleParticleType();
    public static final ParticleType<?> PALE_OAK_LEAVES = new SimpleParticleType();
    public static final ParticleType<?> SCULK_SOUL = new SimpleParticleType();
    public static final ParticleType<FloatParticleOption> SCULK_CHARGE = new FloatParticleType();
    public static final ParticleType<?> SCULK_CHARGE_POP = new SimpleParticleType();
    public static final ParticleType<?> SOUL_FIRE_FLAME = new SimpleParticleType();
    public static final ParticleType<?> SOUL = new SimpleParticleType();
    public static final ParticleType<?> FLASH = new SimpleParticleType();
    public static final ParticleType<?> HAPPY_VILLAGER = new SimpleParticleType();
    public static final ParticleType<?> COMPOSTER = new SimpleParticleType();
    public static final ParticleType<?> HEART = new SimpleParticleType();
    public static final ParticleType<?> INSTANT_EFFECT = new SimpleParticleType();
    public static final ParticleType<ItemStackParticleOption> ITEM = new ItemStackParticleType();
    public static final ParticleType<VibrationParticleOption> VIBRATION = new VibrationParticleType();
    public static final ParticleType<TrailParticleOption> TRAIL = new TrailParticleType();
    public static final ParticleType<?> ITEM_SLIME = new SimpleParticleType();
    public static final ParticleType<?> ITEM_COBWEB = new SimpleParticleType();
    public static final ParticleType<?> ITEM_SNOWBALL = new SimpleParticleType();
    public static final ParticleType<?> LARGE_SMOKE = new SimpleParticleType();
    public static final ParticleType<?> LAVA = new SimpleParticleType();
    public static final ParticleType<?> MYCELIUM = new SimpleParticleType();
    public static final ParticleType<?> NOTE = new SimpleParticleType();
    public static final ParticleType<?> POOF = new SimpleParticleType();
    public static final ParticleType<?> PORTAL = new SimpleParticleType();
    public static final ParticleType<?> RAIN = new SimpleParticleType();
    public static final ParticleType<?> SMOKE = new SimpleParticleType();
    public static final ParticleType<?> WHITE_SMOKE = new SimpleParticleType();
    public static final ParticleType<?> SNEEZE = new SimpleParticleType();
    public static final ParticleType<?> SPIT = new SimpleParticleType();
    public static final ParticleType<?> SQUID_INK = new SimpleParticleType();
    public static final ParticleType<?> SWEEP_ATTACK = new SimpleParticleType();
    public static final ParticleType<?> TOTEM_OF_UNDYING = new SimpleParticleType();
    public static final ParticleType<?> UNDERWATER = new SimpleParticleType();
    public static final ParticleType<?> SPLASH = new SimpleParticleType();
    public static final ParticleType<?> WITCH = new SimpleParticleType();
    public static final ParticleType<?> BUBBLE_POP = new SimpleParticleType();
    public static final ParticleType<?> CURRENT_DOWN = new SimpleParticleType();
    public static final ParticleType<?> BUBBLE_COLUMN_UP = new SimpleParticleType();
    public static final ParticleType<?> NAUTILUS = new SimpleParticleType();
    public static final ParticleType<?> DOLPHIN = new SimpleParticleType();
    public static final ParticleType<?> CAMPFIRE_COSY_SMOKE = new SimpleParticleType();
    public static final ParticleType<?> CAMPFIRE_SIGNAL_SMOKE = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_HONEY = new SimpleParticleType();
    public static final ParticleType<?> FALLING_HONEY = new SimpleParticleType();
    public static final ParticleType<?> LANDING_HONEY = new SimpleParticleType();
    public static final ParticleType<?> FALLING_NECTAR = new SimpleParticleType();
    public static final ParticleType<?> FALLING_SPORE_BLOSSOM = new SimpleParticleType();
    public static final ParticleType<?> ASH = new SimpleParticleType();
    public static final ParticleType<?> CRIMSON_SPORE = new SimpleParticleType();
    public static final ParticleType<?> WARPED_SPORE = new SimpleParticleType();
    public static final ParticleType<?> SPORE_BLOSSOM_AIR = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static final ParticleType<?> FALLING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static final ParticleType<?> LANDING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static final ParticleType<?> REVERSE_PORTAL = new SimpleParticleType();
    public static final ParticleType<?> WHITE_ASH = new SimpleParticleType();
    public static final ParticleType<?> SMALL_FLAME = new SimpleParticleType();
    public static final ParticleType<?> SNOWFLAKE = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_DRIPSTONE_LAVA = new SimpleParticleType();
    public static final ParticleType<?> FALLING_DRIPSTONE_LAVA = new SimpleParticleType();
    public static final ParticleType<?> DRIPPING_DRIPSTONE_WATER = new SimpleParticleType();
    public static final ParticleType<?> FALLING_DRIPSTONE_WATER = new SimpleParticleType();
    public static final ParticleType<?> GLOW_SQUID_INK = new SimpleParticleType();
    public static final ParticleType<?> GLOW = new SimpleParticleType();
    public static final ParticleType<?> WAX_ON = new SimpleParticleType();
    public static final ParticleType<?> WAX_OFF = new SimpleParticleType();
    public static final ParticleType<?> ELECTRIC_SPARK = new SimpleParticleType();
    public static final ParticleType<?> SCRAPE = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> SHRIEK = new VarIntParticleType();
    public static final ParticleType<?> EGG_CRACK = new SimpleParticleType();
    public static final ParticleType<?> DUST_PLUME = new SimpleParticleType();
    public static final ParticleType<?> TRIAL_SPAWNER_DETECTION = new SimpleParticleType();
    public static final ParticleType<?> TRIAL_SPAWNER_DETECTION_OMINOUS = new SimpleParticleType();
    public static final ParticleType<?> VAULT_CONNECTION = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> DUST_PILLAR = new VarIntParticleType();
    public static final ParticleType<?> OMINOUS_SPAWNING = new SimpleParticleType();
    public static final ParticleType<?> RAID_OMEN = new SimpleParticleType();
    public static final ParticleType<?> TRIAL_OMEN = new SimpleParticleType();
    public static final ParticleType<IntegerParticleOption> BLOCK_CRUMBLE = new VarIntParticleType();
    public static final ParticleType<IntegerParticleOption> FIREFLY = new VarIntParticleType();

    public static void init() {
        if (Version.getServerVersion().isOlderThan(Version.v1_20_4)) {
            register(ParticleTypes.AMBIENT_ENTITY_EFFECT);
        }
        register(ParticleTypes.ANGRY_VILLAGER);
        register(ParticleTypes.BLOCK);
        register(ParticleTypes.BLOCK_MARKER);
        register(ParticleTypes.BUBBLE);
        register(ParticleTypes.CLOUD);
        register(ParticleTypes.CRIT);
        register(ParticleTypes.DAMAGE_INDICATOR);
        register(ParticleTypes.DRAGON_BREATH);
        register(ParticleTypes.DRIPPING_LAVA);
        register(ParticleTypes.FALLING_LAVA);
        register(ParticleTypes.LANDING_LAVA);
        register(ParticleTypes.DRIPPING_WATER);
        register(ParticleTypes.FALLING_WATER);
        register(ParticleTypes.DUST);
        register(ParticleTypes.DUST_COLOR_TRANSITION);
        register(ParticleTypes.EFFECT);
        register(ParticleTypes.ELDER_GUARDIAN);
        register(ParticleTypes.ENCHANTED_HIT);
        register(ParticleTypes.ENCHANT);
        register(ParticleTypes.END_ROD);
        register(ParticleTypes.ENTITY_EFFECT);
        register(ParticleTypes.EXPLOSION_EMITTER);
        register(ParticleTypes.EXPLOSION);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_3)) {
            register(ParticleTypes.GUST);
            if (Version.getServerVersion().isOlderThan(Version.v1_20_4)) {
                register(ParticleTypes.GUST_EMITTER);
            } else {
                register(ParticleTypes.SMALL_GUST);
                register(ParticleTypes.GUST_EMITTER_LARGE);
                register(ParticleTypes.GUST_EMITTER_SMALL);
            }
        }
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            register(ParticleTypes.SONIC_BOOM);
        }
        register(ParticleTypes.FALLING_DUST);
        register(ParticleTypes.FIREWORK);
        register(ParticleTypes.FISHING);
        register(ParticleTypes.FLAME);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
            register(ParticleTypes.INFESTED);
        }
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19_3) && Version.getServerVersion().isOlderThan(Version.v1_20_1)) {
            register(ParticleTypes.DRIPPING_CHERRY_LEAVES);
            register(ParticleTypes.FALLING_CHERRY_LEAVES);
            register(ParticleTypes.LANDING_CHERRY_LEAVES);
        } else if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_1)) {
            register(ParticleTypes.CHERRY_LEAVES);
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_3)) {
                register(ParticleTypes.PALE_OAK_LEAVES);
                if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                    register(ParticleTypes.TINTED_LEAVES);
                }
            }
        }
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            register(ParticleTypes.SCULK_SOUL);
            register(ParticleTypes.SCULK_CHARGE);
            register(ParticleTypes.SCULK_CHARGE_POP);
        }
        register(ParticleTypes.SOUL_FIRE_FLAME);
        register(ParticleTypes.SOUL);
        register(ParticleTypes.FLASH);
        register(ParticleTypes.HAPPY_VILLAGER);
        register(ParticleTypes.COMPOSTER);
        register(ParticleTypes.HEART);
        register(ParticleTypes.INSTANT_EFFECT);
        register(ParticleTypes.ITEM);
        register(ParticleTypes.VIBRATION);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_2)) {
            register(ParticleTypes.TRAIL);
        }
        register(ParticleTypes.ITEM_SLIME);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
            register(ParticleTypes.ITEM_COBWEB);
        }
        register(ParticleTypes.ITEM_SNOWBALL);
        register(ParticleTypes.LARGE_SMOKE);
        register(ParticleTypes.LAVA);
        register(ParticleTypes.MYCELIUM);
        register(ParticleTypes.NOTE);
        register(ParticleTypes.POOF);
        register(ParticleTypes.PORTAL);
        register(ParticleTypes.RAIN);
        register(ParticleTypes.SMOKE);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_3)) {
            register(ParticleTypes.WHITE_SMOKE);
        }
        register(ParticleTypes.SNEEZE);
        register(ParticleTypes.SPIT);
        register(ParticleTypes.SQUID_INK);
        register(ParticleTypes.SWEEP_ATTACK);
        register(ParticleTypes.TOTEM_OF_UNDYING);
        register(ParticleTypes.UNDERWATER);
        register(ParticleTypes.SPLASH);
        register(ParticleTypes.WITCH);
        register(ParticleTypes.BUBBLE_POP);
        register(ParticleTypes.CURRENT_DOWN);
        register(ParticleTypes.BUBBLE_COLUMN_UP);
        register(ParticleTypes.NAUTILUS);
        register(ParticleTypes.DOLPHIN);
        register(ParticleTypes.CAMPFIRE_COSY_SMOKE);
        register(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
        register(ParticleTypes.DRIPPING_HONEY);
        register(ParticleTypes.FALLING_HONEY);
        register(ParticleTypes.LANDING_HONEY);
        register(ParticleTypes.FALLING_NECTAR);
        register(ParticleTypes.FALLING_SPORE_BLOSSOM);
        register(ParticleTypes.ASH);
        register(ParticleTypes.CRIMSON_SPORE);
        register(ParticleTypes.WARPED_SPORE);
        register(ParticleTypes.SPORE_BLOSSOM_AIR);
        register(ParticleTypes.DRIPPING_OBSIDIAN_TEAR);
        register(ParticleTypes.FALLING_OBSIDIAN_TEAR);
        register(ParticleTypes.LANDING_OBSIDIAN_TEAR);
        register(ParticleTypes.REVERSE_PORTAL);
        register(ParticleTypes.WHITE_ASH);
        register(ParticleTypes.SMALL_FLAME);
        register(ParticleTypes.SNOWFLAKE);
        register(ParticleTypes.DRIPPING_DRIPSTONE_LAVA);
        register(ParticleTypes.FALLING_DRIPSTONE_LAVA);
        register(ParticleTypes.DRIPPING_DRIPSTONE_WATER);
        register(ParticleTypes.FALLING_DRIPSTONE_WATER);
        register(ParticleTypes.GLOW_SQUID_INK);
        register(ParticleTypes.GLOW);
        register(ParticleTypes.WAX_ON);
        register(ParticleTypes.WAX_OFF);
        register(ParticleTypes.ELECTRIC_SPARK);
        register(ParticleTypes.SCRAPE);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            register(ParticleTypes.SHRIEK);
        }
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_1)) {
            register(ParticleTypes.EGG_CRACK);
        }
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_3)) {
            register(ParticleTypes.DUST_PLUME);
            if (Version.getServerVersion().isOlderThan(Version.v1_20_4)) {
                register(ParticleTypes.GUST_DUST);
            }
            register(ParticleTypes.TRIAL_SPAWNER_DETECTION);
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
                register(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS);
                register(ParticleTypes.VAULT_CONNECTION);
                register(ParticleTypes.DUST_PILLAR);
                register(ParticleTypes.OMINOUS_SPAWNING);
                register(ParticleTypes.RAID_OMEN);
                register(ParticleTypes.TRIAL_OMEN);
                if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_2)) {
                    register(ParticleTypes.BLOCK_CRUMBLE);
                    if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                        register(ParticleTypes.FIREFLY);
                    }
                }
            }
        }
    }

    public static <T extends ParticleOption> ParticleData<T> read(FriendlyByteBuf buf) {
        int id = buf.readVarInt();
        ParticleType<T> type = (ParticleType<T>) registry.get(id);
        if (type == null) {
            LogUtils.error("Failed to find particle type with id {}! Report this on our issue tracker! Version: {} Particles: {}", id, Version.getServerVersion().nmsVersion(), Maps.orderByValue(particles()));
            return new ParticleData<>((ParticleType<T>) ParticleTypes.HEART, null);
        }

        return new ParticleData<>(type, type.read(buf));
    }

    public static void write(ParticleData<ParticleOption> data, FriendlyByteBuf buf) {
        buf.writeVarInt(reverseRegistry.getInt(data.type()));
        data.type().write(data.option(), buf);
    }

    public static int getId(ParticleType<?> type) {
        return reverseRegistry.getInt(type);
    }

    public static <T extends ParticleOption> ParticleType<T> register(ParticleType<T> type) {
        registry.put(registry.size(), type);
        reverseRegistry.put(type, reverseRegistry.size());
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Registering particle: {}", type);
        }
        return type;
    }

    private static Map<String, String> particles() {
        Map<String, String> registered = new HashMap<>();
        for (Field field : ParticleTypes.class.getFields()) {
            try {
                Object type = field.get(null);

                int id = reverseRegistry.getOrDefault(type, -1);
                if (id == -1) {
                    registered.put(field.getName(), "Unregistered");
                } else {
                    registered.put(field.getName(), String.valueOf(id));
                }
            } catch (IllegalAccessException exception) {
                continue;
            }
        }

        return registered;
    }
}
