package net.threetag.palladium.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.threetag.palladium.addonpack.AddonObjectLoader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class BlockPropertiesCodec {

    private static final Map<Identifier, SoundType> SOUND_TYPE_MAP = new HashMap<>();
    public static final Codec<SoundType> SOUND_TYPE_CODEC = Identifier.CODEC.xmap(Identifier -> SOUND_TYPE_MAP.getOrDefault(Identifier, SoundType.EMPTY), BlockPropertiesCodec::getIdForSoundType);
    public static final Codec<PushReaction> PUSH_REACTION_CODEC = Codec.STRING.xmap(s -> PushReaction.valueOf(s.toUpperCase(Locale.ROOT)), pushReaction -> pushReaction.name().toLowerCase(Locale.ROOT));
    public static final Codec<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT_CODEC = StringRepresentable.fromEnum(NoteBlockInstrument::values);

    public static final Codec<BlockBehaviour.Properties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DyeColor.CODEC.optionalFieldOf("map_color").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("no_collission").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("no_occlusion").forGetter(p -> Optional.empty()),
            Codec.FLOAT.optionalFieldOf("friction").forGetter(p -> Optional.empty()),
            Codec.FLOAT.optionalFieldOf("speed_factor").forGetter(p -> Optional.empty()),
            Codec.FLOAT.optionalFieldOf("jump_factor").forGetter(p -> Optional.empty()),
            SOUND_TYPE_CODEC.optionalFieldOf("sound").forGetter(p -> Optional.empty()),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_level").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("random_ticks").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("ignited_by_lava").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("liquid").forGetter(p -> Optional.empty()),
            PUSH_REACTION_CODEC.optionalFieldOf("push_reaction").forGetter(p -> Optional.empty()),
            Codec.FLOAT.optionalFieldOf("destroy_time").forGetter(p -> Optional.empty()),
            Codec.FLOAT.optionalFieldOf("explosion_resistance").forGetter(p -> Optional.empty()),
            Codec.BOOL.optionalFieldOf("requires_correct_tool_for_drops").forGetter(p -> Optional.empty()),
            NOTE_BLOCK_INSTRUMENT_CODEC.optionalFieldOf("instrument").forGetter(p -> Optional.empty())
    ).apply(instance, (mapColor, noCollission, noOcclusion,
                       friction, speedFactor, jumpFactor,
                       sound, lightLevel, randomTicks,
                       ignitedByLava, liquid,
                       pushReaction, destroyTime, explosionResistance,
                       requiresCorrectToolForDrops, instrument) -> {
        var properties = BlockBehaviour.Properties.of();

        if (AddonObjectLoader.ID_TO_SET != null) {
            properties.setId(AddonObjectLoader.resourceId(Registries.BLOCK, AddonObjectLoader.ID_TO_SET));
            AddonObjectLoader.ID_TO_SET = null;
        }

        mapColor.ifPresent(properties::mapColor);
        noCollission.filter(b -> b).ifPresent(p -> properties.noCollision());
        noOcclusion.filter(b -> b).ifPresent(p -> properties.noOcclusion());
        friction.ifPresent(properties::friction);
        speedFactor.ifPresent(properties::speedFactor);
        jumpFactor.ifPresent(properties::jumpFactor);
        sound.ifPresent(properties::sound);
        lightLevel.ifPresent(l -> properties.lightLevel(value -> l));
        randomTicks.filter(b -> b).ifPresent(p -> properties.randomTicks());
        ignitedByLava.filter(b -> b).ifPresent(p -> properties.ignitedByLava());
        liquid.filter(b -> b).ifPresent(p -> properties.liquid());
        pushReaction.ifPresent(properties::pushReaction);
        destroyTime.ifPresent(properties::destroyTime);
        explosionResistance.ifPresent(properties::explosionResistance);
        requiresCorrectToolForDrops.filter(b -> b).ifPresent(p -> properties.requiresCorrectToolForDrops());
        instrument.ifPresent(properties::instrument);

        return properties;
    }));

    public static void registerSoundType(Identifier id, SoundType soundType) {
        SOUND_TYPE_MAP.put(id, soundType);
    }

    public static Identifier getIdForSoundType(SoundType soundType) {
        return SOUND_TYPE_MAP.entrySet().stream().filter(entry -> entry.getValue() == soundType).findFirst().map(Map.Entry::getKey).orElse(Identifier.withDefaultNamespace("empty"));
    }

    static {
        registerSoundType(Identifier.withDefaultNamespace("empty"), SoundType.EMPTY);
        registerSoundType(Identifier.withDefaultNamespace("wood"), SoundType.WOOD);
        registerSoundType(Identifier.withDefaultNamespace("gravel"), SoundType.GRAVEL);
        registerSoundType(Identifier.withDefaultNamespace("grass"), SoundType.GRASS);
        registerSoundType(Identifier.withDefaultNamespace("lily_pad"), SoundType.LILY_PAD);
        registerSoundType(Identifier.withDefaultNamespace("stone"), SoundType.STONE);
        registerSoundType(Identifier.withDefaultNamespace("metal"), SoundType.METAL);
        registerSoundType(Identifier.withDefaultNamespace("glass"), SoundType.GLASS);
        registerSoundType(Identifier.withDefaultNamespace("wool"), SoundType.WOOL);
        registerSoundType(Identifier.withDefaultNamespace("sand"), SoundType.SAND);
        registerSoundType(Identifier.withDefaultNamespace("snow"), SoundType.SNOW);
        registerSoundType(Identifier.withDefaultNamespace("powder_snow"), SoundType.POWDER_SNOW);
        registerSoundType(Identifier.withDefaultNamespace("ladder"), SoundType.LADDER);
        registerSoundType(Identifier.withDefaultNamespace("anvil"), SoundType.ANVIL);
        registerSoundType(Identifier.withDefaultNamespace("slime_block"), SoundType.SLIME_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("honey_block"), SoundType.HONEY_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("wet_grass"), SoundType.WET_GRASS);
        registerSoundType(Identifier.withDefaultNamespace("coral_block"), SoundType.CORAL_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("bamboo"), SoundType.BAMBOO);
        registerSoundType(Identifier.withDefaultNamespace("bamboo_sapling"), SoundType.BAMBOO_SAPLING);
        registerSoundType(Identifier.withDefaultNamespace("scaffolding"), SoundType.SCAFFOLDING);
        registerSoundType(Identifier.withDefaultNamespace("sweet_berry_bush"), SoundType.SWEET_BERRY_BUSH);
        registerSoundType(Identifier.withDefaultNamespace("crop"), SoundType.CROP);
        registerSoundType(Identifier.withDefaultNamespace("hard_crop"), SoundType.HARD_CROP);
        registerSoundType(Identifier.withDefaultNamespace("vine"), SoundType.VINE);
        registerSoundType(Identifier.withDefaultNamespace("nether_wart"), SoundType.NETHER_WART);
        registerSoundType(Identifier.withDefaultNamespace("lantern"), SoundType.LANTERN);
        registerSoundType(Identifier.withDefaultNamespace("stem"), SoundType.STEM);
        registerSoundType(Identifier.withDefaultNamespace("nylium"), SoundType.NYLIUM);
        registerSoundType(Identifier.withDefaultNamespace("fungus"), SoundType.FUNGUS);
        registerSoundType(Identifier.withDefaultNamespace("roots"), SoundType.ROOTS);
        registerSoundType(Identifier.withDefaultNamespace("shroomlight"), SoundType.SHROOMLIGHT);
        registerSoundType(Identifier.withDefaultNamespace("weeping_vines"), SoundType.WEEPING_VINES);
        registerSoundType(Identifier.withDefaultNamespace("twisting_vines"), SoundType.TWISTING_VINES);
        registerSoundType(Identifier.withDefaultNamespace("soul_sand"), SoundType.SOUL_SAND);
        registerSoundType(Identifier.withDefaultNamespace("soul_soil"), SoundType.SOUL_SOIL);
        registerSoundType(Identifier.withDefaultNamespace("basalt"), SoundType.BASALT);
        registerSoundType(Identifier.withDefaultNamespace("wart_block"), SoundType.WART_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("netherrack"), SoundType.NETHERRACK);
        registerSoundType(Identifier.withDefaultNamespace("nether_bricks"), SoundType.NETHER_BRICKS);
        registerSoundType(Identifier.withDefaultNamespace("nether_sprouts"), SoundType.NETHER_SPROUTS);
        registerSoundType(Identifier.withDefaultNamespace("nether_ore"), SoundType.NETHER_ORE);
        registerSoundType(Identifier.withDefaultNamespace("bone_block"), SoundType.BONE_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("netherite_block"), SoundType.NETHERITE_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("ancient_debris"), SoundType.ANCIENT_DEBRIS);
        registerSoundType(Identifier.withDefaultNamespace("lodestone"), SoundType.LODESTONE);
        registerSoundType(Identifier.withDefaultNamespace("chain"), SoundType.CHAIN);
        registerSoundType(Identifier.withDefaultNamespace("nether_gold_ore"), SoundType.NETHER_GOLD_ORE);
        registerSoundType(Identifier.withDefaultNamespace("gilded_blackstone"), SoundType.GILDED_BLACKSTONE);
        registerSoundType(Identifier.withDefaultNamespace("candle"), SoundType.CANDLE);
        registerSoundType(Identifier.withDefaultNamespace("amethyst"), SoundType.AMETHYST);
        registerSoundType(Identifier.withDefaultNamespace("amethyst_cluster"), SoundType.AMETHYST_CLUSTER);
        registerSoundType(Identifier.withDefaultNamespace("small_amethyst_bud"), SoundType.SMALL_AMETHYST_BUD);
        registerSoundType(Identifier.withDefaultNamespace("medium_amethyst_bud"), SoundType.MEDIUM_AMETHYST_BUD);
        registerSoundType(Identifier.withDefaultNamespace("large_amethyst_bud"), SoundType.LARGE_AMETHYST_BUD);
        registerSoundType(Identifier.withDefaultNamespace("tuff"), SoundType.TUFF);
        registerSoundType(Identifier.withDefaultNamespace("tuff_bricks"), SoundType.TUFF_BRICKS);
        registerSoundType(Identifier.withDefaultNamespace("polished_tuff"), SoundType.POLISHED_TUFF);
        registerSoundType(Identifier.withDefaultNamespace("calcite"), SoundType.CALCITE);
        registerSoundType(Identifier.withDefaultNamespace("dripstone_block"), SoundType.DRIPSTONE_BLOCK);
        registerSoundType(Identifier.withDefaultNamespace("pointed_dripstone"), SoundType.POINTED_DRIPSTONE);
        registerSoundType(Identifier.withDefaultNamespace("copper"), SoundType.COPPER);
        registerSoundType(Identifier.withDefaultNamespace("copper_bulb"), SoundType.COPPER_BULB);
        registerSoundType(Identifier.withDefaultNamespace("copper_grate"), SoundType.COPPER_GRATE);
        registerSoundType(Identifier.withDefaultNamespace("cave_vines"), SoundType.CAVE_VINES);
        registerSoundType(Identifier.withDefaultNamespace("spore_blossom"), SoundType.SPORE_BLOSSOM);
        registerSoundType(Identifier.withDefaultNamespace("azalea"), SoundType.AZALEA);
        registerSoundType(Identifier.withDefaultNamespace("flowering_azalea"), SoundType.FLOWERING_AZALEA);
        registerSoundType(Identifier.withDefaultNamespace("moss_carpet"), SoundType.MOSS_CARPET);
        registerSoundType(Identifier.withDefaultNamespace("pink_petals"), SoundType.PINK_PETALS);
        registerSoundType(Identifier.withDefaultNamespace("moss"), SoundType.MOSS);
        registerSoundType(Identifier.withDefaultNamespace("big_dripleaf"), SoundType.BIG_DRIPLEAF);
        registerSoundType(Identifier.withDefaultNamespace("small_dripleaf"), SoundType.SMALL_DRIPLEAF);
        registerSoundType(Identifier.withDefaultNamespace("rooted_dirt"), SoundType.ROOTED_DIRT);
        registerSoundType(Identifier.withDefaultNamespace("hanging_roots"), SoundType.HANGING_ROOTS);
        registerSoundType(Identifier.withDefaultNamespace("azalea_leaves"), SoundType.AZALEA_LEAVES);
        registerSoundType(Identifier.withDefaultNamespace("sculk_sensor"), SoundType.SCULK_SENSOR);
        registerSoundType(Identifier.withDefaultNamespace("sculk_catalyst"), SoundType.SCULK_CATALYST);
        registerSoundType(Identifier.withDefaultNamespace("sculk"), SoundType.SCULK);
        registerSoundType(Identifier.withDefaultNamespace("sculk_vein"), SoundType.SCULK_VEIN);
        registerSoundType(Identifier.withDefaultNamespace("sculk_shrieker"), SoundType.SCULK_SHRIEKER);
        registerSoundType(Identifier.withDefaultNamespace("glow_lichen"), SoundType.GLOW_LICHEN);
        registerSoundType(Identifier.withDefaultNamespace("deepslate"), SoundType.DEEPSLATE);
        registerSoundType(Identifier.withDefaultNamespace("deepslate_bricks"), SoundType.DEEPSLATE_BRICKS);
        registerSoundType(Identifier.withDefaultNamespace("deepslate_tiles"), SoundType.DEEPSLATE_TILES);
        registerSoundType(Identifier.withDefaultNamespace("polished_deepslate"), SoundType.POLISHED_DEEPSLATE);
        registerSoundType(Identifier.withDefaultNamespace("froglight"), SoundType.FROGLIGHT);
        registerSoundType(Identifier.withDefaultNamespace("frogspawn"), SoundType.FROGSPAWN);
        registerSoundType(Identifier.withDefaultNamespace("mangrove_roots"), SoundType.MANGROVE_ROOTS);
        registerSoundType(Identifier.withDefaultNamespace("muddy_mangrove_roots"), SoundType.MUDDY_MANGROVE_ROOTS);
        registerSoundType(Identifier.withDefaultNamespace("mud"), SoundType.MUD);
        registerSoundType(Identifier.withDefaultNamespace("mud_bricks"), SoundType.MUD_BRICKS);
        registerSoundType(Identifier.withDefaultNamespace("packed_mud"), SoundType.PACKED_MUD);
        registerSoundType(Identifier.withDefaultNamespace("hanging_sign"), SoundType.HANGING_SIGN);
        registerSoundType(Identifier.withDefaultNamespace("nether_wood_hanging_sign"), SoundType.NETHER_WOOD_HANGING_SIGN);
        registerSoundType(Identifier.withDefaultNamespace("bamboo_wood_hanging_sign"), SoundType.BAMBOO_WOOD_HANGING_SIGN);
        registerSoundType(Identifier.withDefaultNamespace("bamboo_wood"), SoundType.BAMBOO_WOOD);
        registerSoundType(Identifier.withDefaultNamespace("nether_wood"), SoundType.NETHER_WOOD);
        registerSoundType(Identifier.withDefaultNamespace("cherry_wood"), SoundType.CHERRY_WOOD);
        registerSoundType(Identifier.withDefaultNamespace("cherry_sapling"), SoundType.CHERRY_SAPLING);
        registerSoundType(Identifier.withDefaultNamespace("cherry_leaves"), SoundType.CHERRY_LEAVES);
        registerSoundType(Identifier.withDefaultNamespace("cherry_wood_hanging_sign"), SoundType.CHERRY_WOOD_HANGING_SIGN);
        registerSoundType(Identifier.withDefaultNamespace("chiseled_bookshelf"), SoundType.CHISELED_BOOKSHELF);
        registerSoundType(Identifier.withDefaultNamespace("suspicious_sand"), SoundType.SUSPICIOUS_SAND);
        registerSoundType(Identifier.withDefaultNamespace("suspicious_gravel"), SoundType.SUSPICIOUS_GRAVEL);
        registerSoundType(Identifier.withDefaultNamespace("decorated_pot"), SoundType.DECORATED_POT);
        registerSoundType(Identifier.withDefaultNamespace("decorated_pot_cracked"), SoundType.DECORATED_POT_CRACKED);
        registerSoundType(Identifier.withDefaultNamespace("trial_spawner"), SoundType.TRIAL_SPAWNER);
        registerSoundType(Identifier.withDefaultNamespace("sponge"), SoundType.SPONGE);
        registerSoundType(Identifier.withDefaultNamespace("wet_sponge"), SoundType.WET_SPONGE);
        registerSoundType(Identifier.withDefaultNamespace("vault"), SoundType.VAULT);
        registerSoundType(Identifier.withDefaultNamespace("creaking_heart"), SoundType.CREAKING_HEART);
        registerSoundType(Identifier.withDefaultNamespace("heavy_core"), SoundType.HEAVY_CORE);
        registerSoundType(Identifier.withDefaultNamespace("cobweb"), SoundType.COBWEB);
        registerSoundType(Identifier.withDefaultNamespace("spawner"), SoundType.SPAWNER);
        registerSoundType(Identifier.withDefaultNamespace("resin"), SoundType.RESIN);
        registerSoundType(Identifier.withDefaultNamespace("resin_bricks"), SoundType.RESIN_BRICKS);
    }

    public static void replaceBlockPropertiesCodec() {
        BlockBehaviour.Properties.CODEC = CODEC;
    }

}
