package net.threetag.palladium.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used for getting material in addonpack block jsons
 */
public class BlockMaterialRegistry {

    private static final Map<ResourceLocation, Material> MATERIALS = new HashMap<>();
    private static final Map<ResourceLocation, MaterialColor> MATERIAL_COLORS = new HashMap<>();
    private static final Map<ResourceLocation, SoundType> SOUND_TYPES = new HashMap<>();

    public static void register(ResourceLocation id, Material material) {
        MATERIALS.put(id, material);
    }

    @Nullable
    public static Material get(ResourceLocation id) {
        return MATERIALS.get(id);
    }

    public static Set<ResourceLocation> getAllIds() {
        return MATERIALS.keySet();
    }

    public static Collection<Material> getAll() {
        return MATERIALS.values();
    }

    public static void registerColor(ResourceLocation id, MaterialColor materialColor) {
        MATERIAL_COLORS.put(id, materialColor);
    }

    @Nullable
    public static MaterialColor getColor(ResourceLocation id) {
        return MATERIAL_COLORS.get(id);
    }

    public static Set<ResourceLocation> getAllColorIds() {
        return MATERIAL_COLORS.keySet();
    }

    public static Collection<MaterialColor> getAllColors() {
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
        register(new ResourceLocation("air"), Material.AIR);
        register(new ResourceLocation("structural_air"), Material.STRUCTURAL_AIR);
        register(new ResourceLocation("portal"), Material.PORTAL);
        register(new ResourceLocation("cloth_decoration"), Material.CLOTH_DECORATION);
        register(new ResourceLocation("plant"), Material.PLANT);
        register(new ResourceLocation("water_plant"), Material.WATER_PLANT);
        register(new ResourceLocation("replaceable_plant"), Material.REPLACEABLE_PLANT);
        register(new ResourceLocation("replaceable_fireproof_plant"), Material.REPLACEABLE_FIREPROOF_PLANT);
        register(new ResourceLocation("replaceable_water_plant"), Material.REPLACEABLE_WATER_PLANT);
        register(new ResourceLocation("water"), Material.WATER);
        register(new ResourceLocation("bubble_column"), Material.BUBBLE_COLUMN);
        register(new ResourceLocation("lava"), Material.LAVA);
        register(new ResourceLocation("top_snow"), Material.TOP_SNOW);
        register(new ResourceLocation("fire"), Material.FIRE);
        register(new ResourceLocation("decoration"), Material.DECORATION);
        register(new ResourceLocation("web"), Material.WEB);
        register(new ResourceLocation("sculk"), Material.SCULK);
        register(new ResourceLocation("buildable_glass"), Material.BUILDABLE_GLASS);
        register(new ResourceLocation("clay"), Material.CLAY);
        register(new ResourceLocation("dirt"), Material.DIRT);
        register(new ResourceLocation("grass"), Material.GRASS);
        register(new ResourceLocation("ice_solid"), Material.ICE_SOLID);
        register(new ResourceLocation("sand"), Material.SAND);
        register(new ResourceLocation("sponge"), Material.SPONGE);
        register(new ResourceLocation("shulker_shell"), Material.SHULKER_SHELL);
        register(new ResourceLocation("wood"), Material.WOOD);
        register(new ResourceLocation("nether_wood"), Material.NETHER_WOOD);
        register(new ResourceLocation("bamboo_sapling"), Material.BAMBOO_SAPLING);
        register(new ResourceLocation("bamboo"), Material.BAMBOO);
        register(new ResourceLocation("wool"), Material.WOOL);
        register(new ResourceLocation("explosive"), Material.EXPLOSIVE);
        register(new ResourceLocation("leaves"), Material.LEAVES);
        register(new ResourceLocation("glass"), Material.GLASS);
        register(new ResourceLocation("ice"), Material.ICE);
        register(new ResourceLocation("cactus"), Material.CACTUS);
        register(new ResourceLocation("stone"), Material.STONE);
        register(new ResourceLocation("metal"), Material.METAL);
        register(new ResourceLocation("snow"), Material.SNOW);
        register(new ResourceLocation("heavy_metal"), Material.HEAVY_METAL);
        register(new ResourceLocation("barrier"), Material.BARRIER);
        register(new ResourceLocation("piston"), Material.PISTON);
        register(new ResourceLocation("moss"), Material.MOSS);
        register(new ResourceLocation("vegetable"), Material.VEGETABLE);
        register(new ResourceLocation("egg"), Material.EGG);
        register(new ResourceLocation("cake"), Material.CAKE);
        register(new ResourceLocation("amethyst"), Material.AMETHYST);
        register(new ResourceLocation("powder_snow"), Material.POWDER_SNOW);
        register(new ResourceLocation("frogspawn"), Material.FROGSPAWN);
        register(new ResourceLocation("frog_light"), Material.FROGLIGHT);

        registerColor(new ResourceLocation("none"), MaterialColor.NONE);
        registerColor(new ResourceLocation("grass"), MaterialColor.GRASS);
        registerColor(new ResourceLocation("sand"), MaterialColor.SAND);
        registerColor(new ResourceLocation("wool"), MaterialColor.WOOL);
        registerColor(new ResourceLocation("fire"), MaterialColor.FIRE);
        registerColor(new ResourceLocation("ice"), MaterialColor.ICE);
        registerColor(new ResourceLocation("metal"), MaterialColor.METAL);
        registerColor(new ResourceLocation("plant"), MaterialColor.PLANT);
        registerColor(new ResourceLocation("snow"), MaterialColor.SNOW);
        registerColor(new ResourceLocation("clay"), MaterialColor.CLAY);
        registerColor(new ResourceLocation("dirt"), MaterialColor.DIRT);
        registerColor(new ResourceLocation("stone"), MaterialColor.STONE);
        registerColor(new ResourceLocation("water"), MaterialColor.WATER);
        registerColor(new ResourceLocation("wood"), MaterialColor.WOOD);
        registerColor(new ResourceLocation("quartz"), MaterialColor.QUARTZ);
        registerColor(new ResourceLocation("color_orange"), MaterialColor.COLOR_ORANGE);
        registerColor(new ResourceLocation("color_magenta"), MaterialColor.COLOR_MAGENTA);
        registerColor(new ResourceLocation("color_light_blue"), MaterialColor.COLOR_LIGHT_BLUE);
        registerColor(new ResourceLocation("color_yellow"), MaterialColor.COLOR_YELLOW);
        registerColor(new ResourceLocation("color_light_green"), MaterialColor.COLOR_LIGHT_GREEN);
        registerColor(new ResourceLocation("color_pink"), MaterialColor.COLOR_PINK);
        registerColor(new ResourceLocation("color_gray"), MaterialColor.COLOR_GRAY);
        registerColor(new ResourceLocation("color_light_gray"), MaterialColor.COLOR_LIGHT_GRAY);
        registerColor(new ResourceLocation("color_cyan"), MaterialColor.COLOR_CYAN);
        registerColor(new ResourceLocation("color_purple"), MaterialColor.COLOR_PURPLE);
        registerColor(new ResourceLocation("color_blue"), MaterialColor.COLOR_BLUE);
        registerColor(new ResourceLocation("color_brown"), MaterialColor.COLOR_BROWN);
        registerColor(new ResourceLocation("color_green"), MaterialColor.COLOR_GREEN);
        registerColor(new ResourceLocation("color_red"), MaterialColor.COLOR_RED);
        registerColor(new ResourceLocation("color_black"), MaterialColor.COLOR_BLACK);
        registerColor(new ResourceLocation("gold"), MaterialColor.GOLD);
        registerColor(new ResourceLocation("diamond"), MaterialColor.DIAMOND);
        registerColor(new ResourceLocation("lapis"), MaterialColor.LAPIS);
        registerColor(new ResourceLocation("emerald"), MaterialColor.EMERALD);
        registerColor(new ResourceLocation("podzol"), MaterialColor.PODZOL);
        registerColor(new ResourceLocation("nether"), MaterialColor.NETHER);
        registerColor(new ResourceLocation("terracotta_orange"), MaterialColor.TERRACOTTA_ORANGE);
        registerColor(new ResourceLocation("terracotta_magenta"), MaterialColor.TERRACOTTA_MAGENTA);
        registerColor(new ResourceLocation("terracotta_light_blue"), MaterialColor.TERRACOTTA_LIGHT_BLUE);
        registerColor(new ResourceLocation("terracotta_yellow"), MaterialColor.TERRACOTTA_YELLOW);
        registerColor(new ResourceLocation("terracotta_light_green"), MaterialColor.TERRACOTTA_LIGHT_GREEN);
        registerColor(new ResourceLocation("terracotta_pink"), MaterialColor.TERRACOTTA_PINK);
        registerColor(new ResourceLocation("terracotta_gray"), MaterialColor.TERRACOTTA_GRAY);
        registerColor(new ResourceLocation("terracotta_light_gray"), MaterialColor.TERRACOTTA_LIGHT_GRAY);
        registerColor(new ResourceLocation("terracotta_cyan"), MaterialColor.TERRACOTTA_CYAN);
        registerColor(new ResourceLocation("terracotta_purple"), MaterialColor.TERRACOTTA_PURPLE);
        registerColor(new ResourceLocation("terracotta_blue"), MaterialColor.TERRACOTTA_BLUE);
        registerColor(new ResourceLocation("terracotta_brown"), MaterialColor.TERRACOTTA_BROWN);
        registerColor(new ResourceLocation("terracotta_green"), MaterialColor.TERRACOTTA_GREEN);
        registerColor(new ResourceLocation("terracotta_red"), MaterialColor.TERRACOTTA_RED);
        registerColor(new ResourceLocation("terracotta_black"), MaterialColor.TERRACOTTA_BLACK);
        registerColor(new ResourceLocation("crimson_nylium"), MaterialColor.CRIMSON_NYLIUM);
        registerColor(new ResourceLocation("crimson_stem"), MaterialColor.CRIMSON_STEM);
        registerColor(new ResourceLocation("crimson_hyphae"), MaterialColor.CRIMSON_HYPHAE);
        registerColor(new ResourceLocation("warped_nylium"), MaterialColor.WARPED_NYLIUM);
        registerColor(new ResourceLocation("warped_stem"), MaterialColor.WARPED_STEM);
        registerColor(new ResourceLocation("warped_hyphae"), MaterialColor.WARPED_HYPHAE);
        registerColor(new ResourceLocation("warped_wart_block"), MaterialColor.WARPED_WART_BLOCK);
        registerColor(new ResourceLocation("deepslate"), MaterialColor.DEEPSLATE);
        registerColor(new ResourceLocation("raw_iron"), MaterialColor.RAW_IRON);
        registerColor(new ResourceLocation("glow_lichen"), MaterialColor.GLOW_LICHEN);

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
