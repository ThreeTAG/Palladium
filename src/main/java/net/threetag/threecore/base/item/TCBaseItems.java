package net.threetag.threecore.base.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.util.item.ItemGroupRegistry;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseItems {

    // Misc Items
    @ObjectHolder("hammer")
    public static final Item HAMMER = null;
    @ObjectHolder("plate_cast")
    public static final Item PLATE_CAST = null;
    @ObjectHolder("capacitor")
    public static final Item CAPACITOR = null;
    @ObjectHolder("advanced_capacitor")
    public static final Item ADVANCED_CAPACITOR = null;
    @ObjectHolder("circuit")
    public static final Item CIRCUIT = null;
    @ObjectHolder("advanced_circuit")
    public static final Item ADVANCED_CIRCUIT = null;
    @ObjectHolder("vial")
    public static final Item VIAL = null;
    @ObjectHolder("suit_stand")
    public static final Item SUIT_STAND = null;

    // Ingots
    @ObjectHolder("copper_ingot")
    public static final Item COPPER_INGOT = null;
    @ObjectHolder("tin_ingot")
    public static final Item TIN_INGOT = null;
    @ObjectHolder("lead_ingot")
    public static final Item LEAD_INGOT = null;
    @ObjectHolder("silver_ingot")
    public static final Item SILVER_INGOT = null;
    @ObjectHolder("palladium_ingot")
    public static final Item PALLADIUM_INGOT = null;
    @ObjectHolder("vibranium_ingot")
    public static final Item VIBRANIUM_INGOT = null;
    @ObjectHolder("osmium_ingot")
    public static final Item OSMIUM_INGOT = null;
    @ObjectHolder("uranium_ingot")
    public static final Item URANIUM_INGOT = null;
    @ObjectHolder("titanium_ingot")
    public static final Item TITANIUM_INGOT = null;
    @ObjectHolder("iridium_ingot")
    public static final Item IRIDIUM_INGOT = null;
    @ObjectHolder("uru_ingot")
    public static final Item URU_INGOT = null;
    @ObjectHolder("bronze_ingot")
    public static final Item BRONZE_INGOT = null;
    @ObjectHolder("intertium_ingot")
    public static final Item INTERTIUM_INGOT = null;
    @ObjectHolder("steel_ingot")
    public static final Item STEEL_INGOT = null;
    @ObjectHolder("gold_titanium_alloy_ingot")
    public static final Item GOLD_TITANIUM_ALLOY_INGOT = null;
    @ObjectHolder("adamantium_ingot")
    public static final Item ADAMANTIUM_INGOT = null;

    // Nuggets
    @ObjectHolder("copper_nugget")
    public static final Item COPPER_NUGGET = null;
    @ObjectHolder("tin_nugget")
    public static final Item TIN_NUGGET = null;
    @ObjectHolder("lead_nugget")
    public static final Item LEAD_NUGGET = null;
    @ObjectHolder("silver_nugget")
    public static final Item SILVER_NUGGET = null;
    @ObjectHolder("palladium_nugget")
    public static final Item PALLADIUM_NUGGET = null;
    @ObjectHolder("vibranium_nugget")
    public static final Item VIBRANIUM_NUGGET = null;
    @ObjectHolder("osmium_nugget")
    public static final Item OSMIUM_NUGGET = null;
    @ObjectHolder("uranium_nugget")
    public static final Item URANIUM_NUGGET = null;
    @ObjectHolder("titanium_nugget")
    public static final Item TITANIUM_NUGGET = null;
    @ObjectHolder("iridium_nugget")
    public static final Item IRIDIUM_NUGGET = null;
    @ObjectHolder("uru_nugget")
    public static final Item URU_NUGGET = null;
    @ObjectHolder("bronze_nugget")
    public static final Item BRONZE_NUGGET = null;
    @ObjectHolder("intertium_nugget")
    public static final Item INTERTIUM_NUGGET = null;
    @ObjectHolder("steel_nugget")
    public static final Item STEEL_NUGGET = null;
    @ObjectHolder("gold_titanium_alloy_nugget")
    public static final Item GOLD_TITANIUM_ALLOY_NUGGET = null;
    @ObjectHolder("adamantium_nugget")
    public static final Item ADAMANTIUM_NUGGET = null;

    // Dusts
    @ObjectHolder("coal_dust")
    public static final Item COAL_DUST = null;
    @ObjectHolder("charcoal_dust")
    public static final Item CHARCOAL_DUST = null;
    @ObjectHolder("iron_dust")
    public static final Item IRON_DUST = null;
    @ObjectHolder("gold_dust")
    public static final Item GOLD_DUST = null;
    @ObjectHolder("copper_dust")
    public static final Item COPPER_DUST = null;
    @ObjectHolder("tin_dust")
    public static final Item TIN_DUST = null;
    @ObjectHolder("lead_dust")
    public static final Item LEAD_DUST = null;
    @ObjectHolder("silver_dust")
    public static final Item SILVER_DUST = null;
    @ObjectHolder("palladium_dust")
    public static final Item PALLADIUM_DUST = null;
    @ObjectHolder("vibranium_dust")
    public static final Item VIBRANIUM_DUST = null;
    @ObjectHolder("osmium_dust")
    public static final Item OSMIUM_DUST = null;
    @ObjectHolder("uranium_dust")
    public static final Item URANIUM_DUST = null;
    @ObjectHolder("titanium_dust")
    public static final Item TITANIUM_DUST = null;
    @ObjectHolder("iridium_dust")
    public static final Item IRIDIUM_DUST = null;
    @ObjectHolder("uru_dust")
    public static final Item URU_DUST = null;
    @ObjectHolder("bronze_dust")
    public static final Item BRONZE_DUST = null;
    @ObjectHolder("intertium_dust")
    public static final Item INTERTIUM_DUST = null;
    @ObjectHolder("steel_dust")
    public static final Item STEEL_DUST = null;
    @ObjectHolder("gold_titanium_alloy_dust")
    public static final Item GOLD_TITANIUM_ALLOY_DUST = null;
    @ObjectHolder("adamantium_dust")
    public static final Item ADAMANTIUM_DUST = null;

    // Plates
    @ObjectHolder("iron_plate")
    public static final Item IRON_PLATE = null;
    @ObjectHolder("gold_plate")
    public static final Item GOLD_PLATE = null;
    @ObjectHolder("copper_plate")
    public static final Item COPPER_PLATE = null;
    @ObjectHolder("tin_plate")
    public static final Item TIN_PLATE = null;
    @ObjectHolder("lead_plate")
    public static final Item LEAD_PLATE = null;
    @ObjectHolder("silver_plate")
    public static final Item SILVER_PLATE = null;
    @ObjectHolder("palladium_plate")
    public static final Item PALLADIUM_PLATE = null;
    @ObjectHolder("vibranium_plate")
    public static final Item VIBRANIUM_PLATE = null;
    @ObjectHolder("osmium_plate")
    public static final Item OSMIUM_PLATE = null;
    @ObjectHolder("uranium_plate")
    public static final Item URANIUM_PLATE = null;
    @ObjectHolder("titanium_plate")
    public static final Item TITANIUM_PLATE = null;
    @ObjectHolder("iridium_plate")
    public static final Item IRIDIUM_PLATE = null;
    @ObjectHolder("uru_plate")
    public static final Item URU_PLATE = null;
    @ObjectHolder("bronze_plate")
    public static final Item BRONZE_PLATE = null;
    @ObjectHolder("intertium_plate")
    public static final Item INTERTIUM_PLATE = null;
    @ObjectHolder("steel_plate")
    public static final Item STEEL_PLATE = null;
    @ObjectHolder("gold_titanium_alloy_plate")
    public static final Item GOLD_TITANIUM_ALLOY_PLATE = null;
    @ObjectHolder("adamantium_plate")
    public static final Item ADAMANTIUM_PLATE = null;

    @ObjectHolder("white_fabric")
    public static final Item WHITE_FABRIC = null;
    @ObjectHolder("orange_fabric")
    public static final Item ORANGE_FABRIC = null;
    @ObjectHolder("magenta_fabric")
    public static final Item MAGENTA_FABRIC = null;
    @ObjectHolder("light_blue_fabric")
    public static final Item LIGHT_BLUE_FABRIC = null;
    @ObjectHolder("yellow_fabric")
    public static final Item YELLOW_FABRIC = null;
    @ObjectHolder("lime_fabric")
    public static final Item LIME_FABRIC = null;
    @ObjectHolder("pink_fabric")
    public static final Item PINK_FABRIC = null;
    @ObjectHolder("gray_fabric")
    public static final Item GRAY_FABRIC = null;
    @ObjectHolder("light_gray_fabric")
    public static final Item LIGHT_GRAY_FABRIC = null;
    @ObjectHolder("cyan_fabric")
    public static final Item CYAN_FABRIC = null;
    @ObjectHolder("purple_fabric")
    public static final Item PURPLE_FABRIC = null;
    @ObjectHolder("blue_fabric")
    public static final Item BLUE_FABRIC = null;
    @ObjectHolder("brown_fabric")
    public static final Item BROWN_FABRIC = null;
    @ObjectHolder("green_fabric")
    public static final Item GREEN_FABRIC = null;
    @ObjectHolder("red_fabric")
    public static final Item RED_FABRIC = null;
    @ObjectHolder("black_fabric")
    public static final Item BLACK_FABRIC = null;

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            try {
                // Item Colors
                Minecraft.getInstance().getItemColors().register((IItemColor) Class.forName("net.threetag.threecore.base.item.VialItem$ItemColor").newInstance(), VIAL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = e.getRegistry();

        registry.register(new HammerItem(4.5F, -2.75F, ItemTier.IRON, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(16)).setRegistryName(ThreeCore.MODID, "hammer"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())).setRegistryName(ThreeCore.MODID, "plate_cast"));
        registry.register(new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup()).maxStackSize(1), ThreeCoreServerConfig.ENERGY.CAPACITOR).setRegistryName(ThreeCore.MODID, "capacitor"));
        registry.register(new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup()).maxStackSize(1), ThreeCoreServerConfig.ENERGY.ADVANCED_CAPACITOR).setRegistryName(ThreeCore.MODID, "advanced_capacitor"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())).setRegistryName(ThreeCore.MODID, "circuit"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())).setRegistryName(ThreeCore.MODID, "advanced_circuit"));
        registry.register(new VialItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)).setRegistryName("vial"));
        registry.register(new SuitStandItem(new Item.Properties().group(ItemGroup.DECORATIONS).maxStackSize(16)).setRegistryName("suit_stand"));

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_ingot"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_ingot"));

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_nugget"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_nugget"));

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "coal_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "charcoal_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "iron_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_dust"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_dust"));

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "iron_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_plate"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_plate"));

        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("white_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("orange_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("magenta_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("light_blue_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("yellow_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("lime_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("pink_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("gray_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("light_gray_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("cyan_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("purple_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("blue_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("brown_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("green_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("red_fabric"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())).setRegistryName("black_fabric"));
    }

}
