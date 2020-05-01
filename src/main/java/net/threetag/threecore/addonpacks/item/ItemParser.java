package net.threetag.threecore.addonpacks.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.AbilityGenerator;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.addonpacks.AddonPackManager;
import net.threetag.threecore.item.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ItemParser {

    public static final int resourcePrefix = "items/".length();
    public static final int resourceSuffix = ".json".length();
    private static Map<ResourceLocation, BiFunction<JsonObject, Item.Properties, Item>> itemFunctions = Maps.newHashMap();
    private static List<Pair<EventPriority, ISpecialItemParser>> specialItemParsers = Lists.newArrayList();
    public static final Map<String, List<String>> LOADING_ORDER = Maps.newHashMap();

    static {

        // Item Groups
        registerSpecialItemParser(new ItemGroupParser(), EventPriority.HIGH);

        // Armor Material
        registerSpecialItemParser(new ArmorMaterialParser(), EventPriority.HIGH);

        // Item Tiers
        registerSpecialItemParser(new ItemTierParser(), EventPriority.HIGH);

        // Suit Sets
        registerSpecialItemParser(new SuitSetItemParser(), EventPriority.LOWEST);

        // Item Order
        registerSpecialItemParser(new LoadingOrderParser(), EventPriority.LOWEST);

        // --------------------------------------------------------------

        // Default
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "default"), AbilityItem::new);

        // Armor
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "armor"), AbilityArmorItem::parse);

        // Shield
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "shield"), ShieldAbilityItem::parse);

        // Tools
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "tool"), (j, p) -> {
            JsonElement tierJson = j.get("item_tier");
            IItemTier tier = tierJson.isJsonPrimitive() ? ItemTierRegistry.getItemTier(tierJson.getAsString()) : ItemParser.parseItemTier(tierJson.getAsJsonObject());
            if (tier == null)
                throw new JsonParseException("The item tier '" + tierJson.getAsString() + "' can not be found!");
            String type = JSONUtils.getString(j, "tool_type");
            int attackDamage = type.equalsIgnoreCase("hoe") ? 0 : JSONUtils.getInt(j, "attack_damage");
            float attackSpeed = JSONUtils.getFloat(j, "attack_speed");
            List<AbilityGenerator> abilityGenerators = JSONUtils.hasField(j, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(j, "abilities"), true) : null;
            List<ITextComponent> description = JSONUtils.hasField(j, "description") ? ItemParser.parseDescriptionLines(j.get("description")) : null;
            if (type.equalsIgnoreCase("hoe"))
                return new HoeAbilityItem(tier, attackSpeed, p).setDescription(description).setAbilities(abilityGenerators);
            else if (type.equalsIgnoreCase("shovel"))
                return new ShovelAbilityItem(tier, attackDamage, attackSpeed, p).setDescription(description).setAbilities(abilityGenerators);
            else if (type.equalsIgnoreCase("axe"))
                return new AxeAbilityItem(tier, attackDamage, attackSpeed, p).setDescription(description).setAbilities(abilityGenerators);
            else if (type.equalsIgnoreCase("pickaxe"))
                return new PickaxeAbilityItem(tier, attackDamage, attackSpeed, p).setDescription(description).setAbilities(abilityGenerators);
            else if (type.equalsIgnoreCase("sword"))
                return new SwordAbilityItem(tier, attackDamage, attackSpeed, p).setDescription(description).setAbilities(abilityGenerators);
            else
                throw new JsonParseException("Tool type '" + type + "' does not exist!");
        });
    }

    public static void registerItemParser(ResourceLocation resourceLocation, BiFunction<JsonObject, Item.Properties, Item> function) {
        Preconditions.checkNotNull(resourceLocation);
        Preconditions.checkNotNull(function);
        itemFunctions.put(resourceLocation, function);
    }

    public static void registerSpecialItemParser(ISpecialItemParser parser) {
        registerSpecialItemParser(parser, EventPriority.NORMAL);
    }

    public static void registerSpecialItemParser(ISpecialItemParser parser, EventPriority priority) {
        Preconditions.checkNotNull(parser);
        Preconditions.checkNotNull(priority);
        specialItemParsers.add(Pair.of(priority, parser));
    }

    // Set to lowest priority so that most items are already registered and can be referenced
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerItems(RegistryEvent.Register<Item> e) {
        IResourceManager resourceManager = AddonPackManager.getInstance().getResourceManager();
        specialItemParsers.sort(Comparator.comparingInt(o -> o.getFirst().ordinal()));
        List<ResourceLocation> used = Lists.newArrayList();

        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("items", (name) -> name.endsWith(".json"))) {
            for (Pair<EventPriority, ISpecialItemParser> pair : specialItemParsers) {
                if (pair.getSecond().applies(resourcelocation)) {
                    pair.getSecond().process(resourceManager, resourcelocation, e.getRegistry());
                    used.add(resourcelocation);
                }
            }
        }

        LinkedHashMap<ResourceLocation, JsonObject> items = Maps.newLinkedHashMap();

        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("items", (name) -> name.endsWith(".json") && !name.startsWith("_"))) {
            String s = resourcelocation.getPath();
            ResourceLocation id = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));

            if (used.contains(resourcelocation)) {
                continue;
            }

            try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                items.put(id, JSONUtils.fromJson(AddonPackManager.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't read addonpack item {} from {}", id, resourcelocation, throwable);
            }
        }

        Map<ResourceLocation, JsonObject> sorted = new TreeMap<ResourceLocation, JsonObject>((id1, id2) -> {
            if (id1.getNamespace().equals(id2.getNamespace())) {
                List<String> order = LOADING_ORDER.get(id1.getNamespace());

                if (order == null) {
                    return id1.compareTo(id2);
                }

                if (order.contains(id1.getPath()) && !order.contains(id2.getPath())) {
                    return -1;
                } else if (!order.contains(id1.getPath()) && order.contains(id2.getPath())) {
                    return 1;
                } else if (!order.contains(id1.getPath()) && !order.contains(id2.getPath())) {
                    return id1.compareTo(id2);
                } else {
                    return order.indexOf(id1.getPath()) - order.indexOf(id2.getPath());
                }
            } else {
                return id1.compareTo(id2);
            }
        });
        sorted.putAll(items);

        for (Map.Entry<ResourceLocation, JsonObject> entry : sorted.entrySet()) {
            try {
                Item item = parse(entry.getValue());
                if (item != null) {
                    item.setRegistryName(entry.getKey());
                    e.getRegistry().register(item);
                    ThreeCore.LOGGER.info("Registered addonpack item {}!", entry.getKey());
                }
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't read addonpack item {}", entry.getKey(), throwable);
            }
        }

        LOADING_ORDER.clear();
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

    public static List<ITextComponent> parseDescriptionLines(JsonElement jsonElement) {
        List<ITextComponent> lines = Lists.newArrayList();

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                lines.addAll(parseDescriptionLines(jsonArray.get(i)));
            }
        } else if (jsonElement.isJsonObject()) {
            lines.add(ITextComponent.Serializer.fromJson(jsonElement));
        } else if (jsonElement.isJsonPrimitive()) {
            lines.add(new StringTextComponent(jsonElement.getAsString()));
        }

        return lines;
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

    public interface ISpecialItemParser {

        boolean applies(ResourceLocation resourceLocation);

        void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry);

    }

}
