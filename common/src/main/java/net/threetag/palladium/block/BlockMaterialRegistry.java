package net.threetag.palladium.block;

import net.minecraft.resources.ResourceLocation;
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
    }
    
}
