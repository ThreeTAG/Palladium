package net.threetag.threecore.addonpacks.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.util.item.ArmorMaterialRegistry;
import net.threetag.threecore.util.item.ItemGroupRegistry;

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

    public static IArmorMaterial parseArmorMaterial(JsonObject jsonObject) {
        String name = JSONUtils.getString(jsonObject, "name");
        if (ArmorMaterialRegistry.getArmorMaterial(name) != null) {
            return ArmorMaterialRegistry.getArmorMaterial(name);
        } else {
            int maxDamageFactor = JSONUtils.getInt(jsonObject, "max_damage_factor");
            int[] damageReductionAmountArray = new int[4];
            JsonArray dmgReduction = JSONUtils.getJsonArray(jsonObject, "damage_reduction");
            if (dmgReduction.size() != 4)
                throw new JsonParseException("The damage_reduction array must contain 4 entries, one for each armor part!");
            for (int i = 0; i < dmgReduction.size(); i++)
                damageReductionAmountArray[i] = dmgReduction.get(i).getAsInt();
            int enchantibility = JSONUtils.getInt(jsonObject, "enchantibility", 0);
            LazyLoadBase soundEvent = new LazyLoadBase(() -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, "equip_sound", ""))));
            float toughness = JSONUtils.getFloat(jsonObject, "toughness", 0F);
            Supplier<Ingredient> repairMaterial = () -> JSONUtils.hasField(jsonObject, "repair_material") ? Ingredient.deserialize(jsonObject.get("repair_material")) : Ingredient.EMPTY;
            return ArmorMaterialRegistry.addArmorMaterial(name, new ArmorMaterial(name, maxDamageFactor, damageReductionAmountArray, enchantibility, soundEvent, toughness, repairMaterial));
        }
    }

    public static class ArmorMaterial implements IArmorMaterial {

        private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final LazyLoadBase<SoundEvent> soundEvent;
        private final float toughness;
        private final LazyLoadBase<Ingredient> repairMaterial;

        private ArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, LazyLoadBase<SoundEvent> equipSoundIn, float toughness, Supplier<Ingredient> repairMaterialSupplier) {
            this.name = nameIn;
            this.maxDamageFactor = maxDamageFactorIn;
            this.damageReductionAmountArray = damageReductionAmountsIn;
            this.enchantability = enchantabilityIn;
            this.soundEvent = equipSoundIn;
            this.toughness = toughness;
            this.repairMaterial = new LazyLoadBase<>(repairMaterialSupplier);
        }

        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return this.damageReductionAmountArray[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return this.soundEvent.getValue();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return this.repairMaterial.getValue();
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }
    }

}
