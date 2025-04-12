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
import com.artillexstudios.axapi.utils.Version;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

public final class ParticleTypes {
    private static final Int2ObjectArrayMap<ParticleType<?>> registry = new Int2ObjectArrayMap<>();
    private static final ParticleType<?> AMBIENT_ENTITY_EFFECT = new SimpleParticleType();
    private static final ParticleType<?> DRIPPING_CHERRY_LEAVES = new SimpleParticleType();
    private static final ParticleType<?> FALLING_CHERRY_LEAVES = new SimpleParticleType();
    private static final ParticleType<?> LANDING_CHERRY_LEAVES = new SimpleParticleType();
    private static final ParticleType<?> GUST_DUST = new SimpleParticleType();
    private static final ParticleType<?> GUST_EMITTER = new SimpleParticleType();
    private static final ParticleType<?> GUST_EMITTER_SMALL = new SimpleParticleType();
    private static final ParticleType<ColorParticleOption> TINTED_LEAVES = new ColorParticleType();
    public static ParticleType<?> ANGRY_VILLAGER = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> BLOCK = new VarIntParticleType();
    public static ParticleType<IntegerParticleOption> BLOCK_MARKER = new VarIntParticleType();
    public static ParticleType<?> BUBBLE = new SimpleParticleType();
    public static ParticleType<?> CLOUD = new SimpleParticleType();
    public static ParticleType<?> CRIT = new SimpleParticleType();
    public static ParticleType<?> DAMAGE_INDICATOR = new SimpleParticleType();
    public static ParticleType<?> DRAGON_BREATH = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_LAVA = new SimpleParticleType();
    public static ParticleType<?> FALLING_LAVA = new SimpleParticleType();
    public static ParticleType<?> LANDING_LAVA = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_WATER = new SimpleParticleType();
    public static ParticleType<?> FALLING_WATER = new SimpleParticleType();
    public static ParticleType<DustParticleOption> DUST = new DustParticleType();
    public static ParticleType<DustColorTransitionParticleOption> DUST_COLOR_TRANSITION = new DustColorTransitionParticleType();
    public static ParticleType<?> EFFECT = new SimpleParticleType();
    public static ParticleType<?> ELDER_GUARDIAN = new SimpleParticleType();
    public static ParticleType<?> ENCHANTED_HIT = new SimpleParticleType();
    public static ParticleType<?> ENCHANT = new SimpleParticleType();
    public static ParticleType<?> END_ROD = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> ENTITY_EFFECT = new IntegerParticleType();
    public static ParticleType<?> EXPLOSION_EMITTER = new SimpleParticleType();
    public static ParticleType<?> EXPLOSION = new SimpleParticleType();
    public static ParticleType<?> GUST = new SimpleParticleType();
    public static ParticleType<?> SMALL_GUST = new SimpleParticleType();
    public static ParticleType<?> GUST_EMITTER_LARGE = new SimpleParticleType();
    public static ParticleType<?> SONIC_BOOM = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> FALLING_DUST = new VarIntParticleType();
    public static ParticleType<?> FIREWORK = new SimpleParticleType();
    public static ParticleType<?> FISHING = new SimpleParticleType();
    public static ParticleType<?> FLAME = new SimpleParticleType();
    public static ParticleType<?> INFESTED = new SimpleParticleType();
    public static ParticleType<?> CHERRY_LEAVES = new SimpleParticleType();
    public static ParticleType<?> PALE_OAK_LEAVES = new SimpleParticleType();
    public static ParticleType<?> SCULK_SOUL = new SimpleParticleType();
    public static ParticleType<FloatParticleOption> SCULK_CHARGE = new FloatParticleType();
    public static ParticleType<?> SCULK_CHARGE_POP = new SimpleParticleType();
    public static ParticleType<?> SOUL_FIRE_FLAME = new SimpleParticleType();
    public static ParticleType<?> SOUL = new SimpleParticleType();
    public static ParticleType<?> FLASH = new SimpleParticleType();
    public static ParticleType<?> HAPPY_VILLAGER = new SimpleParticleType();
    public static ParticleType<?> COMPOSTER = new SimpleParticleType();
    public static ParticleType<?> HEART = new SimpleParticleType();
    public static ParticleType<?> INSTANT_EFFECT = new SimpleParticleType();
    public static ParticleType<ItemStackParticleOption> ITEM = new ItemStackParticleType();
    public static ParticleType<VibrationParticleOption> VIBRATION = new VibrationParticleType();
    public static ParticleType<TrailParticleOption> TRAIL = new TrailParticleType();
    public static ParticleType<?> ITEM_SLIME = new SimpleParticleType();
    public static ParticleType<?> ITEM_COBWEB = new SimpleParticleType();
    public static ParticleType<?> ITEM_SNOWBALL = new SimpleParticleType();
    public static ParticleType<?> LARGE_SMOKE = new SimpleParticleType();
    public static ParticleType<?> LAVA = new SimpleParticleType();
    public static ParticleType<?> MYCELIUM = new SimpleParticleType();
    public static ParticleType<?> NOTE = new SimpleParticleType();
    public static ParticleType<?> POOF = new SimpleParticleType();
    public static ParticleType<?> PORTAL = new SimpleParticleType();
    public static ParticleType<?> RAIN = new SimpleParticleType();
    public static ParticleType<?> SMOKE = new SimpleParticleType();
    public static ParticleType<?> WHITE_SMOKE = new SimpleParticleType();
    public static ParticleType<?> SNEEZE = new SimpleParticleType();
    public static ParticleType<?> SPIT = new SimpleParticleType();
    public static ParticleType<?> SQUID_INK = new SimpleParticleType();
    public static ParticleType<?> SWEEP_ATTACK = new SimpleParticleType();
    public static ParticleType<?> TOTEM_OF_UNDYING = new SimpleParticleType();
    public static ParticleType<?> UNDERWATER = new SimpleParticleType();
    public static ParticleType<?> SPLASH = new SimpleParticleType();
    public static ParticleType<?> WITCH = new SimpleParticleType();
    public static ParticleType<?> BUBBLE_POP = new SimpleParticleType();
    public static ParticleType<?> CURRENT_DOWN = new SimpleParticleType();
    public static ParticleType<?> BUBBLE_COLUMN_UP = new SimpleParticleType();
    public static ParticleType<?> NAUTILUS = new SimpleParticleType();
    public static ParticleType<?> DOLPHIN = new SimpleParticleType();
    public static ParticleType<?> CAMPFIRE_COSY_SMOKE = new SimpleParticleType();
    public static ParticleType<?> CAMPFIRE_SIGNAL_SMOKE = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_HONEY = new SimpleParticleType();
    public static ParticleType<?> FALLING_HONEY = new SimpleParticleType();
    public static ParticleType<?> LANDING_HONEY = new SimpleParticleType();
    public static ParticleType<?> FALLING_NECTAR = new SimpleParticleType();
    public static ParticleType<?> FALLING_SPORE_BLOSSOM = new SimpleParticleType();
    public static ParticleType<?> ASH = new SimpleParticleType();
    public static ParticleType<?> CRIMSON_SPORE = new SimpleParticleType();
    public static ParticleType<?> WARPED_SPORE = new SimpleParticleType();
    public static ParticleType<?> SPORE_BLOSSOM_AIR = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static ParticleType<?> FALLING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static ParticleType<?> LANDING_OBSIDIAN_TEAR = new SimpleParticleType();
    public static ParticleType<?> REVERSE_PORTAL = new SimpleParticleType();
    public static ParticleType<?> WHITE_ASH = new SimpleParticleType();
    public static ParticleType<?> SMALL_FLAME = new SimpleParticleType();
    public static ParticleType<?> SNOWFLAKE = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_DRIPSTONE_LAVA = new SimpleParticleType();
    public static ParticleType<?> FALLING_DRIPSTONE_LAVA = new SimpleParticleType();
    public static ParticleType<?> DRIPPING_DRIPSTONE_WATER = new SimpleParticleType();
    public static ParticleType<?> FALLING_DRIPSTONE_WATER = new SimpleParticleType();
    public static ParticleType<?> GLOW_SQUID_INK = new SimpleParticleType();
    public static ParticleType<?> GLOW = new SimpleParticleType();
    public static ParticleType<?> WAX_ON = new SimpleParticleType();
    public static ParticleType<?> WAX_OFF = new SimpleParticleType();
    public static ParticleType<?> ELECTRIC_SPARK = new SimpleParticleType();
    public static ParticleType<?> SCRAPE = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> SHRIEK = new VarIntParticleType();
    public static ParticleType<?> EGG_CRACK = new SimpleParticleType();
    public static ParticleType<?> DUST_PLUME = new SimpleParticleType();
    public static ParticleType<?> TRIAL_SPAWNER_DETECTION = new SimpleParticleType();
    public static ParticleType<?> TRIAL_SPAWNER_DETECTION_OMINOUS = new SimpleParticleType();
    public static ParticleType<?> VAULT_CONNECTION = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> DUST_PILLAR = new VarIntParticleType();
    public static ParticleType<?> OMINOUS_SPAWNING = new SimpleParticleType();
    public static ParticleType<?> RAID_OMEN = new SimpleParticleType();
    public static ParticleType<?> TRIAL_OMEN = new SimpleParticleType();
    public static ParticleType<IntegerParticleOption> BLOCK_CRUMBLE = new VarIntParticleType();
    public static ParticleType<IntegerParticleOption> FIREFLY = new VarIntParticleType();

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
            if (Version.getServerVersion().isOlderThan(Version.v1_20_4)) {
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
        return new ParticleData<>(type, type.read(buf));
    }

    public static <T extends ParticleOption> ParticleType<T> register(ParticleType<T> type) {
        registry.put(registry.size(), type);
        return type;
    }
}
