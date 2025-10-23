package net.threetag.palladium.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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

    private static final Map<ResourceLocation, SoundType> SOUND_TYPE_MAP = new HashMap<>();
    public static final Codec<SoundType> SOUND_TYPE_CODEC = ResourceLocation.CODEC.xmap(resourceLocation -> SOUND_TYPE_MAP.getOrDefault(resourceLocation, SoundType.EMPTY), BlockPropertiesCodec::getIdForSoundType);
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

    public static void registerSoundType(ResourceLocation id, SoundType soundType) {
        SOUND_TYPE_MAP.put(id, soundType);
    }

    public static ResourceLocation getIdForSoundType(SoundType soundType) {
        return SOUND_TYPE_MAP.entrySet().stream().filter(entry -> entry.getValue() == soundType).findFirst().map(Map.Entry::getKey).orElse(ResourceLocation.withDefaultNamespace("empty"));
    }

    static {
        registerSoundType(ResourceLocation.withDefaultNamespace("empty"), SoundType.EMPTY);
        registerSoundType(ResourceLocation.withDefaultNamespace("wood"), SoundType.WOOD);
        registerSoundType(ResourceLocation.withDefaultNamespace("gravel"), SoundType.GRAVEL);
        registerSoundType(ResourceLocation.withDefaultNamespace("grass"), SoundType.GRASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("lily_pad"), SoundType.LILY_PAD);
        registerSoundType(ResourceLocation.withDefaultNamespace("stone"), SoundType.STONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("metal"), SoundType.METAL);
        registerSoundType(ResourceLocation.withDefaultNamespace("glass"), SoundType.GLASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("wool"), SoundType.WOOL);
        registerSoundType(ResourceLocation.withDefaultNamespace("sand"), SoundType.SAND);
        registerSoundType(ResourceLocation.withDefaultNamespace("snow"), SoundType.SNOW);
        registerSoundType(ResourceLocation.withDefaultNamespace("powder_snow"), SoundType.POWDER_SNOW);
        registerSoundType(ResourceLocation.withDefaultNamespace("ladder"), SoundType.LADDER);
        registerSoundType(ResourceLocation.withDefaultNamespace("anvil"), SoundType.ANVIL);
        registerSoundType(ResourceLocation.withDefaultNamespace("slime_block"), SoundType.SLIME_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("honey_block"), SoundType.HONEY_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("wet_grass"), SoundType.WET_GRASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("coral_block"), SoundType.CORAL_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo"), SoundType.BAMBOO);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo_sapling"), SoundType.BAMBOO_SAPLING);
        registerSoundType(ResourceLocation.withDefaultNamespace("scaffolding"), SoundType.SCAFFOLDING);
        registerSoundType(ResourceLocation.withDefaultNamespace("sweet_berry_bush"), SoundType.SWEET_BERRY_BUSH);
        registerSoundType(ResourceLocation.withDefaultNamespace("crop"), SoundType.CROP);
        registerSoundType(ResourceLocation.withDefaultNamespace("hard_crop"), SoundType.HARD_CROP);
        registerSoundType(ResourceLocation.withDefaultNamespace("vine"), SoundType.VINE);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_wart"), SoundType.NETHER_WART);
        registerSoundType(ResourceLocation.withDefaultNamespace("lantern"), SoundType.LANTERN);
        registerSoundType(ResourceLocation.withDefaultNamespace("stem"), SoundType.STEM);
        registerSoundType(ResourceLocation.withDefaultNamespace("nylium"), SoundType.NYLIUM);
        registerSoundType(ResourceLocation.withDefaultNamespace("fungus"), SoundType.FUNGUS);
        registerSoundType(ResourceLocation.withDefaultNamespace("roots"), SoundType.ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("shroomlight"), SoundType.SHROOMLIGHT);
        registerSoundType(ResourceLocation.withDefaultNamespace("weeping_vines"), SoundType.WEEPING_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("twisting_vines"), SoundType.TWISTING_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("soul_sand"), SoundType.SOUL_SAND);
        registerSoundType(ResourceLocation.withDefaultNamespace("soul_soil"), SoundType.SOUL_SOIL);
        registerSoundType(ResourceLocation.withDefaultNamespace("basalt"), SoundType.BASALT);
        registerSoundType(ResourceLocation.withDefaultNamespace("wart_block"), SoundType.WART_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("netherrack"), SoundType.NETHERRACK);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_bricks"), SoundType.NETHER_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_sprouts"), SoundType.NETHER_SPROUTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_ore"), SoundType.NETHER_ORE);
        registerSoundType(ResourceLocation.withDefaultNamespace("bone_block"), SoundType.BONE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("netherite_block"), SoundType.NETHERITE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("ancient_debris"), SoundType.ANCIENT_DEBRIS);
        registerSoundType(ResourceLocation.withDefaultNamespace("lodestone"), SoundType.LODESTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("chain"), SoundType.CHAIN);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_gold_ore"), SoundType.NETHER_GOLD_ORE);
        registerSoundType(ResourceLocation.withDefaultNamespace("gilded_blackstone"), SoundType.GILDED_BLACKSTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("candle"), SoundType.CANDLE);
        registerSoundType(ResourceLocation.withDefaultNamespace("amethyst"), SoundType.AMETHYST);
        registerSoundType(ResourceLocation.withDefaultNamespace("amethyst_cluster"), SoundType.AMETHYST_CLUSTER);
        registerSoundType(ResourceLocation.withDefaultNamespace("small_amethyst_bud"), SoundType.SMALL_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("medium_amethyst_bud"), SoundType.MEDIUM_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("large_amethyst_bud"), SoundType.LARGE_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("tuff"), SoundType.TUFF);
        registerSoundType(ResourceLocation.withDefaultNamespace("tuff_bricks"), SoundType.TUFF_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("polished_tuff"), SoundType.POLISHED_TUFF);
        registerSoundType(ResourceLocation.withDefaultNamespace("calcite"), SoundType.CALCITE);
        registerSoundType(ResourceLocation.withDefaultNamespace("dripstone_block"), SoundType.DRIPSTONE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("pointed_dripstone"), SoundType.POINTED_DRIPSTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("copper"), SoundType.COPPER);
        registerSoundType(ResourceLocation.withDefaultNamespace("copper_bulb"), SoundType.COPPER_BULB);
        registerSoundType(ResourceLocation.withDefaultNamespace("copper_grate"), SoundType.COPPER_GRATE);
        registerSoundType(ResourceLocation.withDefaultNamespace("cave_vines"), SoundType.CAVE_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("spore_blossom"), SoundType.SPORE_BLOSSOM);
        registerSoundType(ResourceLocation.withDefaultNamespace("azalea"), SoundType.AZALEA);
        registerSoundType(ResourceLocation.withDefaultNamespace("flowering_azalea"), SoundType.FLOWERING_AZALEA);
        registerSoundType(ResourceLocation.withDefaultNamespace("moss_carpet"), SoundType.MOSS_CARPET);
        registerSoundType(ResourceLocation.withDefaultNamespace("pink_petals"), SoundType.PINK_PETALS);
        registerSoundType(ResourceLocation.withDefaultNamespace("moss"), SoundType.MOSS);
        registerSoundType(ResourceLocation.withDefaultNamespace("big_dripleaf"), SoundType.BIG_DRIPLEAF);
        registerSoundType(ResourceLocation.withDefaultNamespace("small_dripleaf"), SoundType.SMALL_DRIPLEAF);
        registerSoundType(ResourceLocation.withDefaultNamespace("rooted_dirt"), SoundType.ROOTED_DIRT);
        registerSoundType(ResourceLocation.withDefaultNamespace("hanging_roots"), SoundType.HANGING_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("azalea_leaves"), SoundType.AZALEA_LEAVES);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_sensor"), SoundType.SCULK_SENSOR);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_catalyst"), SoundType.SCULK_CATALYST);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk"), SoundType.SCULK);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_vein"), SoundType.SCULK_VEIN);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_shrieker"), SoundType.SCULK_SHRIEKER);
        registerSoundType(ResourceLocation.withDefaultNamespace("glow_lichen"), SoundType.GLOW_LICHEN);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate"), SoundType.DEEPSLATE);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate_bricks"), SoundType.DEEPSLATE_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate_tiles"), SoundType.DEEPSLATE_TILES);
        registerSoundType(ResourceLocation.withDefaultNamespace("polished_deepslate"), SoundType.POLISHED_DEEPSLATE);
        registerSoundType(ResourceLocation.withDefaultNamespace("froglight"), SoundType.FROGLIGHT);
        registerSoundType(ResourceLocation.withDefaultNamespace("frogspawn"), SoundType.FROGSPAWN);
        registerSoundType(ResourceLocation.withDefaultNamespace("mangrove_roots"), SoundType.MANGROVE_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("muddy_mangrove_roots"), SoundType.MUDDY_MANGROVE_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("mud"), SoundType.MUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("mud_bricks"), SoundType.MUD_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("packed_mud"), SoundType.PACKED_MUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("hanging_sign"), SoundType.HANGING_SIGN);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_wood_hanging_sign"), SoundType.NETHER_WOOD_HANGING_SIGN);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo_wood_hanging_sign"), SoundType.BAMBOO_WOOD_HANGING_SIGN);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo_wood"), SoundType.BAMBOO_WOOD);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_wood"), SoundType.NETHER_WOOD);
        registerSoundType(ResourceLocation.withDefaultNamespace("cherry_wood"), SoundType.CHERRY_WOOD);
        registerSoundType(ResourceLocation.withDefaultNamespace("cherry_sapling"), SoundType.CHERRY_SAPLING);
        registerSoundType(ResourceLocation.withDefaultNamespace("cherry_leaves"), SoundType.CHERRY_LEAVES);
        registerSoundType(ResourceLocation.withDefaultNamespace("cherry_wood_hanging_sign"), SoundType.CHERRY_WOOD_HANGING_SIGN);
        registerSoundType(ResourceLocation.withDefaultNamespace("chiseled_bookshelf"), SoundType.CHISELED_BOOKSHELF);
        registerSoundType(ResourceLocation.withDefaultNamespace("suspicious_sand"), SoundType.SUSPICIOUS_SAND);
        registerSoundType(ResourceLocation.withDefaultNamespace("suspicious_gravel"), SoundType.SUSPICIOUS_GRAVEL);
        registerSoundType(ResourceLocation.withDefaultNamespace("decorated_pot"), SoundType.DECORATED_POT);
        registerSoundType(ResourceLocation.withDefaultNamespace("decorated_pot_cracked"), SoundType.DECORATED_POT_CRACKED);
        registerSoundType(ResourceLocation.withDefaultNamespace("trial_spawner"), SoundType.TRIAL_SPAWNER);
        registerSoundType(ResourceLocation.withDefaultNamespace("sponge"), SoundType.SPONGE);
        registerSoundType(ResourceLocation.withDefaultNamespace("wet_sponge"), SoundType.WET_SPONGE);
        registerSoundType(ResourceLocation.withDefaultNamespace("vault"), SoundType.VAULT);
        registerSoundType(ResourceLocation.withDefaultNamespace("creaking_heart"), SoundType.CREAKING_HEART);
        registerSoundType(ResourceLocation.withDefaultNamespace("heavy_core"), SoundType.HEAVY_CORE);
        registerSoundType(ResourceLocation.withDefaultNamespace("cobweb"), SoundType.COBWEB);
        registerSoundType(ResourceLocation.withDefaultNamespace("spawner"), SoundType.SPAWNER);
        registerSoundType(ResourceLocation.withDefaultNamespace("resin"), SoundType.RESIN);
        registerSoundType(ResourceLocation.withDefaultNamespace("resin_bricks"), SoundType.RESIN_BRICKS);
    }

    public static void replaceBlockPropertiesCodec() {
        BlockBehaviour.Properties.CODEC = CODEC;
    }

}
