package net.threetag.threecore.item;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.block.TCBlocks;

import java.util.concurrent.atomic.AtomicReference;

public class TCItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThreeCore.MODID);

    // Misc Items
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", () -> new HammerItem(4.5F, -2.75F, ItemTier.IRON, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(16)));
    public static final RegistryObject<Item> PLATE_CAST = ITEMS.register("plate_cast", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())));
    public static final RegistryObject<Item> CAPACITOR = ITEMS.register("capacitor", () -> new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup()).maxStackSize(1), ThreeCoreServerConfig.ENERGY.CAPACITOR));
    public static final RegistryObject<Item> ADVANCED_CAPACITOR = ITEMS.register("advanced_capacitor", () -> new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup()).maxStackSize(1), ThreeCoreServerConfig.ENERGY.ADVANCED_CAPACITOR));
    public static final RegistryObject<Item> CIRCUIT = ITEMS.register("circuit", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())));
    public static final RegistryObject<Item> ADVANCED_CIRCUIT = ITEMS.register("advanced_circuit", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup())));
    public static final RegistryObject<Item> VIAL = ITEMS.register("vial", () -> new VialItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));
    public static final RegistryObject<Item> SUIT_STAND = ITEMS.register("suit_stand", () -> new SuitStandItem(new Item.Properties().group(ItemGroup.DECORATIONS).maxStackSize(16)));
    public static final RegistryObject<Item> MULTIVERSAL_EXTRAPOLATOR = ITEMS.register("multiversal_extrapolator", () -> new MultiversalExtrapolatorItem(new Item.Properties().group(ItemGroupRegistry.getTechnologyGroup()).maxDamage(12)));

    // Ingots
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PALLADIUM_INGOT = ITEMS.register("palladium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> VIBRANIUM_INGOT = ITEMS.register("vibranium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> OSMIUM_INGOT = ITEMS.register("osmium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> IRIDIUM_INGOT = ITEMS.register("iridium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> URU_INGOT = ITEMS.register("uru_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> INTERTIUM_INGOT = ITEMS.register("intertium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_TITANIUM_ALLOY_INGOT = ITEMS.register("gold_titanium_alloy_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ADAMANTIUM_INGOT = ITEMS.register("adamantium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));

    // Nuggets
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TIN_NUGGET = ITEMS.register("tin_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PALLADIUM_NUGGET = ITEMS.register("palladium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> VIBRANIUM_NUGGET = ITEMS.register("vibranium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> OSMIUM_NUGGET = ITEMS.register("osmium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> URANIUM_NUGGET = ITEMS.register("uranium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TITANIUM_NUGGET = ITEMS.register("titanium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> IRIDIUM_NUGGET = ITEMS.register("iridium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> URU_NUGGET = ITEMS.register("uru_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BRONZE_NUGGET = ITEMS.register("bronze_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> INTERTIUM_NUGGET = ITEMS.register("intertium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STEEL_NUGGET = ITEMS.register("steel_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_TITANIUM_ALLOY_NUGGET = ITEMS.register("gold_titanium_alloy_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ADAMANTIUM_NUGGET = ITEMS.register("adamantium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));

    // Dusts
    public static final RegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> CHARCOAL_DUST = ITEMS.register("charcoal_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TIN_DUST = ITEMS.register("tin_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> LEAD_DUST = ITEMS.register("lead_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_DUST = ITEMS.register("silver_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PALLADIUM_DUST = ITEMS.register("palladium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> VIBRANIUM_DUST = ITEMS.register("vibranium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> OSMIUM_DUST = ITEMS.register("osmium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> URANIUM_DUST = ITEMS.register("uranium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TITANIUM_DUST = ITEMS.register("titanium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> IRIDIUM_DUST = ITEMS.register("iridium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> URU_DUST = ITEMS.register("uru_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BRONZE_DUST = ITEMS.register("bronze_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> INTERTIUM_DUST = ITEMS.register("intertium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STEEL_DUST = ITEMS.register("steel_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_TITANIUM_ALLOY_DUST = ITEMS.register("gold_titanium_alloy_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ADAMANTIUM_DUST = ITEMS.register("adamantium_dust", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));

    // Plates
    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_PLATE = ITEMS.register("gold_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TIN_PLATE = ITEMS.register("tin_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> LEAD_PLATE = ITEMS.register("lead_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_PLATE = ITEMS.register("silver_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PALLADIUM_PLATE = ITEMS.register("palladium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> VIBRANIUM_PLATE = ITEMS.register("vibranium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> OSMIUM_PLATE = ITEMS.register("osmium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> URANIUM_PLATE = ITEMS.register("uranium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> TITANIUM_PLATE = ITEMS.register("titanium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> IRIDIUM_PLATE = ITEMS.register("iridium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> URU_PLATE = ITEMS.register("uru_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BRONZE_PLATE = ITEMS.register("bronze_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> INTERTIUM_PLATE = ITEMS.register("intertium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> GOLD_TITANIUM_ALLOY_PLATE = ITEMS.register("gold_titanium_alloy_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ADAMANTIUM_PLATE = ITEMS.register("adamantium_plate", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> WHITE_FABRIC = ITEMS.register("white_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> ORANGE_FABRIC = ITEMS.register("orange_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> MAGENTA_FABRIC = ITEMS.register("magenta_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> LIGHT_BLUE_FABRIC = ITEMS.register("light_blue_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> YELLOW_FABRIC = ITEMS.register("yellow_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> LIME_FABRIC = ITEMS.register("lime_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> PINK_FABRIC = ITEMS.register("pink_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> GRAY_FABRIC = ITEMS.register("gray_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> LIGHT_GRAY_FABRIC = ITEMS.register("light_gray_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> CYAN_FABRIC = ITEMS.register("cyan_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> PURPLE_FABRIC = ITEMS.register("purple_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> BLUE_FABRIC = ITEMS.register("blue_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> BROWN_FABRIC = ITEMS.register("brown_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> GREEN_FABRIC = ITEMS.register("green_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> RED_FABRIC = ITEMS.register("red_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));
    public static final RegistryObject<Item> BLACK_FABRIC = ITEMS.register("black_fabric", () -> new Item(new Item.Properties().group(ItemGroupRegistry.getSuitsAndArmorGroup())));

    public static void loadItemColors() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
            @Override
            public void run() {
                // Item Colors
                Minecraft.getInstance().getItemColors().register(new VialItem.ItemColor(), VIAL.get());
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void initItemProperties() {
        ItemModelsProperties.func_239418_a_(TCBlocks.ADVANCED_CAPACITOR_BLOCK_ITEM.get(), new ResourceLocation(ThreeCore.MODID, "energy"), (stack, world, entity) -> {
            AtomicReference<Float> f = new AtomicReference<>((float) 0);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> f.set((float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored()));
            return f.get();
        });

        ItemModelsProperties.func_239418_a_(TCItems.MULTIVERSAL_EXTRAPOLATOR.get(), new ResourceLocation(ThreeCore.MODID, "inactive"), (stack, world, entity) -> !MultiversalExtrapolatorItem.hasValidUniverseClient(stack) ? 1.0F : 0.0F);
    }

    public static void onLootTableLoad(LootTableLoadEvent e) {
        if (e.getName().toString().toLowerCase().contains("minecraft:chests/")) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("chance", 0.5F);

            ILootCondition.IBuilder conditionBuilder = new ILootCondition.IBuilder() {
                @Override
                public ILootCondition build() {
                    return Registry.LOOT_CONDITION_TYPE.func_241873_b(new ResourceLocation("random_chance")).get().func_237408_a_().func_230423_a_(jsonObject, null);
                }
            };
            e.getTable().addPool(LootPool.builder().addEntry(ItemLootEntry.builder(TCItems.MULTIVERSAL_EXTRAPOLATOR.get()).quality(1).weight(10).acceptCondition(conditionBuilder)).acceptCondition(conditionBuilder).build());
        }

    }

}
