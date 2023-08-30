package net.threetag.palladium.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used for getting material in addonpack block jsons
 */
public class BlockMaterialRegistry {

    private static final Map<ResourceLocation, MapColor> MATERIAL_COLORS = new HashMap<>();
    private static final Map<ResourceLocation, SoundType> SOUND_TYPES = new HashMap<>();

    public static void registerColor(ResourceLocation id, MapColor materialColor) {
        MATERIAL_COLORS.put(id, materialColor);
    }

    @Nullable
    public static MapColor getColor(ResourceLocation id) {
        return MATERIAL_COLORS.get(id);
    }

    public static Set<ResourceLocation> getAllColorIds() {
        return MATERIAL_COLORS.keySet();
    }

    public static Collection<MapColor> getAllColors() {
        return MATERIAL_COLORS.values();
    }

    public static void registerSoundType(ResourceLocation id, SoundType soundType) {
        SOUND_TYPES.put(id, soundType);
    }

    @Nullable
    public static SoundType getSoundType(ResourceLocation id) {
        return SOUND_TYPES.get(id);
    }

    public static Set<ResourceLocation> getAllSoundTypeIds() {
        return SOUND_TYPES.keySet();
    }

    public static Collection<SoundType> getAllSoundTypes() {
        return SOUND_TYPES.values();
    }

    static {
        registerColor(new ResourceLocation("none"), MapColor.NONE);
        registerColor(new ResourceLocation("grass"), MapColor.GRASS);
        registerColor(new ResourceLocation("sand"), MapColor.SAND);
        registerColor(new ResourceLocation("wool"), MapColor.WOOL);
        registerColor(new ResourceLocation("fire"), MapColor.FIRE);
        registerColor(new ResourceLocation("ice"), MapColor.ICE);
        registerColor(new ResourceLocation("metal"), MapColor.METAL);
        registerColor(new ResourceLocation("plant"), MapColor.PLANT);
        registerColor(new ResourceLocation("snow"), MapColor.SNOW);
        registerColor(new ResourceLocation("clay"), MapColor.CLAY);
        registerColor(new ResourceLocation("dirt"), MapColor.DIRT);
        registerColor(new ResourceLocation("stone"), MapColor.STONE);
        registerColor(new ResourceLocation("water"), MapColor.WATER);
        registerColor(new ResourceLocation("wood"), MapColor.WOOD);
        registerColor(new ResourceLocation("quartz"), MapColor.QUARTZ);
        registerColor(new ResourceLocation("color_orange"), MapColor.COLOR_ORANGE);
        registerColor(new ResourceLocation("color_magenta"), MapColor.COLOR_MAGENTA);
        registerColor(new ResourceLocation("color_light_blue"), MapColor.COLOR_LIGHT_BLUE);
        registerColor(new ResourceLocation("color_yellow"), MapColor.COLOR_YELLOW);
        registerColor(new ResourceLocation("color_light_green"), MapColor.COLOR_LIGHT_GREEN);
        registerColor(new ResourceLocation("color_pink"), MapColor.COLOR_PINK);
        registerColor(new ResourceLocation("color_gray"), MapColor.COLOR_GRAY);
        registerColor(new ResourceLocation("color_light_gray"), MapColor.COLOR_LIGHT_GRAY);
        registerColor(new ResourceLocation("color_cyan"), MapColor.COLOR_CYAN);
        registerColor(new ResourceLocation("color_purple"), MapColor.COLOR_PURPLE);
        registerColor(new ResourceLocation("color_blue"), MapColor.COLOR_BLUE);
        registerColor(new ResourceLocation("color_brown"), MapColor.COLOR_BROWN);
        registerColor(new ResourceLocation("color_green"), MapColor.COLOR_GREEN);
        registerColor(new ResourceLocation("color_red"), MapColor.COLOR_RED);
        registerColor(new ResourceLocation("color_black"), MapColor.COLOR_BLACK);
        registerColor(new ResourceLocation("gold"), MapColor.GOLD);
        registerColor(new ResourceLocation("diamond"), MapColor.DIAMOND);
        registerColor(new ResourceLocation("lapis"), MapColor.LAPIS);
        registerColor(new ResourceLocation("emerald"), MapColor.EMERALD);
        registerColor(new ResourceLocation("podzol"), MapColor.PODZOL);
        registerColor(new ResourceLocation("nether"), MapColor.NETHER);
        registerColor(new ResourceLocation("terracotta_orange"), MapColor.TERRACOTTA_ORANGE);
        registerColor(new ResourceLocation("terracotta_magenta"), MapColor.TERRACOTTA_MAGENTA);
        registerColor(new ResourceLocation("terracotta_light_blue"), MapColor.TERRACOTTA_LIGHT_BLUE);
        registerColor(new ResourceLocation("terracotta_yellow"), MapColor.TERRACOTTA_YELLOW);
        registerColor(new ResourceLocation("terracotta_light_green"), MapColor.TERRACOTTA_LIGHT_GREEN);
        registerColor(new ResourceLocation("terracotta_pink"), MapColor.TERRACOTTA_PINK);
        registerColor(new ResourceLocation("terracotta_gray"), MapColor.TERRACOTTA_GRAY);
        registerColor(new ResourceLocation("terracotta_light_gray"), MapColor.TERRACOTTA_LIGHT_GRAY);
        registerColor(new ResourceLocation("terracotta_cyan"), MapColor.TERRACOTTA_CYAN);
        registerColor(new ResourceLocation("terracotta_purple"), MapColor.TERRACOTTA_PURPLE);
        registerColor(new ResourceLocation("terracotta_blue"), MapColor.TERRACOTTA_BLUE);
        registerColor(new ResourceLocation("terracotta_brown"), MapColor.TERRACOTTA_BROWN);
        registerColor(new ResourceLocation("terracotta_green"), MapColor.TERRACOTTA_GREEN);
        registerColor(new ResourceLocation("terracotta_red"), MapColor.TERRACOTTA_RED);
        registerColor(new ResourceLocation("terracotta_black"), MapColor.TERRACOTTA_BLACK);
        registerColor(new ResourceLocation("crimson_nylium"), MapColor.CRIMSON_NYLIUM);
        registerColor(new ResourceLocation("crimson_stem"), MapColor.CRIMSON_STEM);
        registerColor(new ResourceLocation("crimson_hyphae"), MapColor.CRIMSON_HYPHAE);
        registerColor(new ResourceLocation("warped_nylium"), MapColor.WARPED_NYLIUM);
        registerColor(new ResourceLocation("warped_stem"), MapColor.WARPED_STEM);
        registerColor(new ResourceLocation("warped_hyphae"), MapColor.WARPED_HYPHAE);
        registerColor(new ResourceLocation("warped_wart_block"), MapColor.WARPED_WART_BLOCK);
        registerColor(new ResourceLocation("deepslate"), MapColor.DEEPSLATE);
        registerColor(new ResourceLocation("raw_iron"), MapColor.RAW_IRON);
        registerColor(new ResourceLocation("glow_lichen"), MapColor.GLOW_LICHEN);

        registerSoundType(new ResourceLocation("wood"), SoundType.WOOD);
        registerSoundType(new ResourceLocation("gravel"), SoundType.GRAVEL);
        registerSoundType(new ResourceLocation("grass"), SoundType.GRASS);
        registerSoundType(new ResourceLocation("lily_pad"), SoundType.LILY_PAD);
        registerSoundType(new ResourceLocation("stone"), SoundType.STONE);
        registerSoundType(new ResourceLocation("metal"), SoundType.METAL);
        registerSoundType(new ResourceLocation("glass"), SoundType.GLASS);
        registerSoundType(new ResourceLocation("wool"), SoundType.WOOL);
        registerSoundType(new ResourceLocation("sand"), SoundType.SAND);
        registerSoundType(new ResourceLocation("snow"), SoundType.SNOW);
        registerSoundType(new ResourceLocation("powder_snow"), SoundType.POWDER_SNOW);
        registerSoundType(new ResourceLocation("ladder"), SoundType.LADDER);
        registerSoundType(new ResourceLocation("anvil"), SoundType.ANVIL);
        registerSoundType(new ResourceLocation("slime_block"), SoundType.SLIME_BLOCK);
        registerSoundType(new ResourceLocation("honey_block"), SoundType.HONEY_BLOCK);
        registerSoundType(new ResourceLocation("wet_grass"), SoundType.WET_GRASS);
        registerSoundType(new ResourceLocation("coral_block"), SoundType.CORAL_BLOCK);
        registerSoundType(new ResourceLocation("bamboo"), SoundType.BAMBOO);
        registerSoundType(new ResourceLocation("bamboo_sapling"), SoundType.BAMBOO_SAPLING);
        registerSoundType(new ResourceLocation("scaffolding"), SoundType.SCAFFOLDING);
        registerSoundType(new ResourceLocation("sweet_berry_bush"), SoundType.SWEET_BERRY_BUSH);
        registerSoundType(new ResourceLocation("crop"), SoundType.CROP);
        registerSoundType(new ResourceLocation("hard_crop"), SoundType.HARD_CROP);
        registerSoundType(new ResourceLocation("vine"), SoundType.VINE);
        registerSoundType(new ResourceLocation("nether_wart"), SoundType.NETHER_WART);
        registerSoundType(new ResourceLocation("lantern"), SoundType.LANTERN);
        registerSoundType(new ResourceLocation("stem"), SoundType.STEM);
        registerSoundType(new ResourceLocation("nylium"), SoundType.NYLIUM);
        registerSoundType(new ResourceLocation("fungus"), SoundType.FUNGUS);
        registerSoundType(new ResourceLocation("root"), SoundType.ROOTS);
        registerSoundType(new ResourceLocation("shroomlight"), SoundType.SHROOMLIGHT);
        registerSoundType(new ResourceLocation("weeping_vines"), SoundType.WEEPING_VINES);
        registerSoundType(new ResourceLocation("twisting_vines"), SoundType.TWISTING_VINES);
        registerSoundType(new ResourceLocation("soul_sand"), SoundType.SOUL_SAND);
        registerSoundType(new ResourceLocation("soul_soil"), SoundType.SOUL_SOIL);
        registerSoundType(new ResourceLocation("basalt"), SoundType.BASALT);
        registerSoundType(new ResourceLocation("wart_block"), SoundType.WART_BLOCK);
        registerSoundType(new ResourceLocation("netherrack"), SoundType.NETHERRACK);
        registerSoundType(new ResourceLocation("nether_bricks"), SoundType.NETHER_BRICKS);
        registerSoundType(new ResourceLocation("nether_sprouts"), SoundType.NETHER_SPROUTS);
        registerSoundType(new ResourceLocation("nether_ore"), SoundType.NETHER_ORE);
        registerSoundType(new ResourceLocation("bone_block"), SoundType.BONE_BLOCK);
        registerSoundType(new ResourceLocation("netherite_block"), SoundType.NETHERITE_BLOCK);
        registerSoundType(new ResourceLocation("ancient_debris"), SoundType.ANCIENT_DEBRIS);
        registerSoundType(new ResourceLocation("lodestone"), SoundType.LODESTONE);
        registerSoundType(new ResourceLocation("chain"), SoundType.CHAIN);
        registerSoundType(new ResourceLocation("nether_gold_ore"), SoundType.NETHER_GOLD_ORE);
        registerSoundType(new ResourceLocation("gilded_blackstone"), SoundType.GILDED_BLACKSTONE);
        registerSoundType(new ResourceLocation("candle"), SoundType.CANDLE);
        registerSoundType(new ResourceLocation("amethyst"), SoundType.AMETHYST);
        registerSoundType(new ResourceLocation("amethyst_cluster"), SoundType.AMETHYST_CLUSTER);
        registerSoundType(new ResourceLocation("small_amethyst_bud"), SoundType.SMALL_AMETHYST_BUD);
        registerSoundType(new ResourceLocation("medium_amethyst_bud"), SoundType.MEDIUM_AMETHYST_BUD);
        registerSoundType(new ResourceLocation("large_amethyst_bud"), SoundType.LARGE_AMETHYST_BUD);
        registerSoundType(new ResourceLocation("tuff"), SoundType.TUFF);
        registerSoundType(new ResourceLocation("calcite"), SoundType.CALCITE);
        registerSoundType(new ResourceLocation("dripstone_block"), SoundType.DRIPSTONE_BLOCK);
        registerSoundType(new ResourceLocation("pointed_dripstone"), SoundType.POINTED_DRIPSTONE);
        registerSoundType(new ResourceLocation("copper"), SoundType.COPPER);
        registerSoundType(new ResourceLocation("cave_vines"), SoundType.CAVE_VINES);
        registerSoundType(new ResourceLocation("spore_blossom"), SoundType.SPORE_BLOSSOM);
        registerSoundType(new ResourceLocation("azalea"), SoundType.AZALEA);
        registerSoundType(new ResourceLocation("flowering_azalea"), SoundType.FLOWERING_AZALEA);
        registerSoundType(new ResourceLocation("moss_carpet"), SoundType.MOSS_CARPET);
        registerSoundType(new ResourceLocation("moss"), SoundType.MOSS);
        registerSoundType(new ResourceLocation("big_dripleaf"), SoundType.BIG_DRIPLEAF);
        registerSoundType(new ResourceLocation("small_dripleaf"), SoundType.SMALL_DRIPLEAF);
        registerSoundType(new ResourceLocation("rooted_dirt"), SoundType.ROOTED_DIRT);
        registerSoundType(new ResourceLocation("hanging_roots"), SoundType.HANGING_ROOTS);
        registerSoundType(new ResourceLocation("azelea_leaves"), SoundType.AZALEA_LEAVES);
        registerSoundType(new ResourceLocation("sculk_sensor"), SoundType.SCULK_SENSOR);
        registerSoundType(new ResourceLocation("sculk_catalyst"), SoundType.SCULK_CATALYST);
        registerSoundType(new ResourceLocation("sculk"), SoundType.SCULK);
        registerSoundType(new ResourceLocation("sculk_vein"), SoundType.SCULK_VEIN);
        registerSoundType(new ResourceLocation("sculk_shrieker"), SoundType.SCULK_SHRIEKER);
        registerSoundType(new ResourceLocation("glow_lichen"), SoundType.GLOW_LICHEN);
        registerSoundType(new ResourceLocation("deepslate"), SoundType.DEEPSLATE);
        registerSoundType(new ResourceLocation("deepslate_bricks"), SoundType.DEEPSLATE_BRICKS);
        registerSoundType(new ResourceLocation("deepslate_tiles"), SoundType.DEEPSLATE_TILES);
        registerSoundType(new ResourceLocation("polished_deepslate"), SoundType.POLISHED_DEEPSLATE);
        registerSoundType(new ResourceLocation("froglight"), SoundType.FROGLIGHT);
        registerSoundType(new ResourceLocation("frogspawn"), SoundType.FROGSPAWN);
        registerSoundType(new ResourceLocation("mangrove_roots"), SoundType.MANGROVE_ROOTS);
        registerSoundType(new ResourceLocation("muddy_mangrove_roots"), SoundType.MUDDY_MANGROVE_ROOTS);
        registerSoundType(new ResourceLocation("mud"), SoundType.MUD);
        registerSoundType(new ResourceLocation("mud_bricks"), SoundType.MUD_BRICKS);
        registerSoundType(new ResourceLocation("packed_mud"), SoundType.PACKED_MUD);
    }

}
