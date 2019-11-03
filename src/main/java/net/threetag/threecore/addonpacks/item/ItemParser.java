package net.threetag.threecore.addonpacks.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.util.item.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ItemParser {

    public static final int resourcePrefix = "items/".length();
    public static final int resourceSuffix = ".json".length();
    private static Map<ResourceLocation, BiFunction<JsonObject, Item.Properties, Item>> itemFunctions = Maps.newHashMap();

    static {
        // Default
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "default"), (j, p) -> new AbilityItem(p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null));

        // Armor
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "armor"), (j, p) -> AbilityArmorItem.parse(j, p));

        // Shield
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "shield"), (j, p) ->
                new ShieldAbilityItem(p,
                        JSONUtils.getInt(j, "use_duration", 72000),
                        () -> (JSONUtils.hasField(j, "repair_material") ? Ingredient.deserialize(j.get("repair_material")) : Ingredient.EMPTY))
                        .setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null));

        // Tools
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "tool"), (j, p) -> {
            JsonElement tierJson = j.get("item_tier");
            IItemTier tier = tierJson.isJsonPrimitive() ? ItemTierRegistry.getItemTier(tierJson.getAsString()) : ItemParser.parseItemTier(tierJson.getAsJsonObject());
            if (tier == null)
                throw new JsonParseException("The item tier '" + tierJson.getAsString() + "' can not be found!");
            String type = JSONUtils.getString(j, "tool_type");
            int attackDamage = type.equalsIgnoreCase("hoe") ? 0 : JSONUtils.getInt(j, "attack_damage");
            float attackSpeed = JSONUtils.getFloat(j, "attack_speed");
            if (type.equalsIgnoreCase("hoe"))
                return new HoeAbilityItem(tier, attackSpeed, p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null);
            else if (type.equalsIgnoreCase("shovel"))
                return new ShovelAbilityItem(tier, attackDamage, attackSpeed, p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null);
            else if (type.equalsIgnoreCase("axe"))
                return new AxeAbilityItem(tier, attackDamage, attackSpeed, p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null);
            else if (type.equalsIgnoreCase("pickaxe"))
                return new PickaxeAbilityItem(tier, attackDamage, attackSpeed, p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null);
            else if (type.equalsIgnoreCase("sword"))
                return new SwordAbilityItem(tier, attackDamage, attackSpeed, p).setAbilities(JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null);
            else
                throw new JsonParseException("Tool type '" + type + "' does not exist!");
        });
    }

    public static void registerItemParser(ResourceLocation resourceLocation, BiFunction<JsonObject, Item.Properties, Item> function) {
        Preconditions.checkNotNull(resourceLocation);
        Preconditions.checkNotNull(function);
        itemFunctions.put(resourceLocation, function);
    }

    // Set to lowest priority so that most items are already registered and can be referenced
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerItems(RegistryEvent.Register<Item> e) {
        IResourceManager resourceManager = ThreeCoreAddonPacks.getInstance().getResourceManager();

        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("items", (name) -> name.endsWith(".json"))) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));

            // Armor materials
            if (resourcelocation.getPath().startsWith("items/_armor_materials")) {
                try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                    JsonArray jsonArray = JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        IArmorMaterial material = parseArmorMaterial(jsonArray.get(i).getAsJsonObject());
                        ArmorMaterialRegistry.addArmorMaterial(material.getName(), material);
                        ThreeCore.LOGGER.info("Registered addonpack armor material {} from {}!", material.getName(), resourcelocation);
                    }
                } catch (Throwable throwable) {
                    ThreeCore.LOGGER.error("Couldn't read addonpack armor materials from {}", resourcelocation, throwable);
                }
            } else if (resourcelocation.getPath().startsWith("items/_item_tiers")) {
                try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                    JsonArray jsonArray = JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String name = JSONUtils.getString(jsonArray.get(i).getAsJsonObject(), "name");
                        IItemTier tier = parseItemTier(jsonArray.get(i).getAsJsonObject());
                        ItemTierRegistry.addItemTier(name, tier);
                        ThreeCore.LOGGER.info("Registered addonpack item tier {} from {}!", name, resourcelocation);
                    }
                } catch (Throwable throwable) {
                    ThreeCore.LOGGER.error("Couldn't read addonpack item tier from {}", resourcelocation, throwable);
                }
            } else {
                try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                    Item item = parse(JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
                    if (item != null) {
                        item.setRegistryName(resourcelocation1);
                        e.getRegistry().register(item);
                        ThreeCore.LOGGER.info("Registered addonpack item {}!", resourcelocation1);
                    }
                } catch (Throwable throwable) {
                    ThreeCore.LOGGER.error("Couldn't read addonpack item {} from {}", resourcelocation1, resourcelocation, throwable);
                }
            }
        }
    }

    public static Item parse(JsonObject json) throws JsonParseException {
        BiFunction<JsonObject, Item.Properties, Item> function = itemFunctions.get(new ResourceLocation(JSONUtils.getString(json, "type")));

        if (function == null)
            throw new JsonParseException("The item type '" + JSONUtils.getString(json, "type") + "' does not exist!");

        Item item = function.apply(json, JSONUtils.hasField(json, "properties") ? parseProperties(JSONUtils.getJsonObject(json, "properties")) : new Item.Properties());
        return Objects.requireNonNull(item);
    }

    public static Item.Properties parseProperties(JsonObject json) {
        Item.Properties properties = new Item.Properties();

        if (JSONUtils.hasField(json, "max_stack_size"))
            properties.maxStackSize(JSONUtils.getInt(json, "max_stack_size", 64));

        if (JSONUtils.hasField(json, "max_damage"))
            properties.maxDamage(JSONUtils.getInt(json, "max_damage"));

        if (JSONUtils.hasField(json, "container_item"))
            properties.containerItem(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "container_item"))));

        if (JSONUtils.hasField(json, "group"))
            properties.group(ItemGroupRegistry.getItemGroup(JSONUtils.getString(json, "group")));

        if (JSONUtils.hasField(json, "rarity"))
            properties.rarity(Rarity.valueOf(JSONUtils.getString(json, "rarity").toUpperCase()));

        if (JSONUtils.hasField(json, "tool_types")) {
            JsonArray array = JSONUtils.getJsonArray(json, "tool_types");

            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject = array.get(i).getAsJsonObject();
                properties.addToolType(getToolType(JSONUtils.getString(jsonObject, "tool")), JSONUtils.getInt(jsonObject, "level"));
            }
        }

        if (JSONUtils.hasField(json, "food")) {
            JsonObject foodJson = JSONUtils.getJsonObject(json, "food");
            Food.Builder food = new Food.Builder();

            if (JSONUtils.hasField(foodJson, "value"))
                food.hunger(JSONUtils.getInt(foodJson, "value"));

            if (JSONUtils.hasField(foodJson, "saturation"))
                food.saturation(JSONUtils.getFloat(foodJson, "saturation"));

            if (JSONUtils.hasField(foodJson, "meat") && JSONUtils.getBoolean(foodJson, "meat"))
                food.meat();

            if (JSONUtils.hasField(foodJson, "always_edible") && JSONUtils.getBoolean(foodJson, "always_edible"))
                food.setAlwaysEdible();

            if (JSONUtils.hasField(foodJson, "fast_to_eat") && JSONUtils.getBoolean(foodJson, "fast_to_eat"))
                food.fastToEat();

            if (JSONUtils.hasField(foodJson, "effects")) {
                JsonArray effectArray = JSONUtils.getJsonArray(foodJson, "effects");
                for (int i = 0; i < effectArray.size(); i++) {
                    JsonObject effect = effectArray.get(i).getAsJsonObject();
                    food.effect(new EffectInstance(ForgeRegistries.POTIONS.getValue(new ResourceLocation(JSONUtils.getString(effect, "effect"))),
                            JSONUtils.getInt(effect, "duration", 0), JSONUtils.getInt(effect, "amplifier", 0),
                            JSONUtils.getBoolean(effect, "ambient", false), JSONUtils.getBoolean(effect, "particles", true),
                            JSONUtils.getBoolean(effect, "show_icon", true)
                    ), JSONUtils.getFloat(effect, "probability"));
                }
            }

            properties.food(food.build());
        }

        return properties;
    }

    public static ToolType getToolType(String name) {
        Map<String, ToolType> values = ObfuscationReflectionHelper.getPrivateValue(ToolType.class, null, "values");
        return values.get(name);
    }

    public static IArmorMaterial parseArmorMaterial(JsonObject json) {
        return parseArmorMaterial(json, true);
    }

    public static IArmorMaterial parseArmorMaterial(JsonObject json, boolean requireName) {
        String name = requireName ? JSONUtils.getString(json, "name") : "";
        int maxDamageFactor = JSONUtils.getInt(json, "max_damage_factor");
        int[] damageReductionAmountArray = new int[4];
        JsonArray dmgReduction = JSONUtils.getJsonArray(json, "damage_reduction");
        if (dmgReduction.size() != 4)
            throw new JsonParseException("The damage_reduction array must contain 4 entries, one for each armor part!");
        for (int i = 0; i < dmgReduction.size(); i++)
            damageReductionAmountArray[i] = dmgReduction.get(i).getAsInt();
        int enchantibility = JSONUtils.getInt(json, "enchantibility", 0);
        LazyLoadBase soundEvent = new LazyLoadBase(() -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(JSONUtils.getString(json, "equip_sound", ""))));
        float toughness = JSONUtils.getFloat(json, "toughness", 0F);
        Supplier<Ingredient> repairMaterial = () -> JSONUtils.hasField(json, "repair_material") ? Ingredient.deserialize(json.get("repair_material")) : Ingredient.EMPTY;
        return new SimpleArmorMaterial(name, maxDamageFactor, damageReductionAmountArray, enchantibility, soundEvent, toughness, repairMaterial);
    }

    public static IItemTier parseItemTier(JsonObject jsonObject) {
        int maxUses = JSONUtils.getInt(jsonObject, "max_uses");
        float efficiency = JSONUtils.getFloat(jsonObject, "efficiency");
        float attackDamage = JSONUtils.getFloat(jsonObject, "attack_damage");
        int harvestLevel = JSONUtils.getInt(jsonObject, "harvest_level");
        int enchantibility = JSONUtils.getInt(jsonObject, "enchantibility");
        Supplier<Ingredient> repairMaterial = () -> JSONUtils.hasField(jsonObject, "repair_material") ? Ingredient.deserialize(jsonObject.get("repair_material")) : Ingredient.EMPTY;
        return new SimpleItemTier(maxUses, efficiency, attackDamage, harvestLevel, enchantibility, repairMaterial);
    }

}
