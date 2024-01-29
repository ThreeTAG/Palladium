package net.threetag.palladium.addonpack.parser;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ItemBuilder;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.*;
import net.threetag.palladium.power.ability.AttributeModifierAbility;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.util.Platform;

import java.util.*;

public class ItemParser extends AddonParser<Item> {

    public static final ResourceLocation FALLBACK_SERIALIZER = Palladium.id("default");
    private static final Map<ResourceLocation, ItemTypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();
    public final Map<ResourceLocation, List<PlacedTabPlacement>> autoRegisteredBlockItems = new HashMap<>();

    public ItemParser() {
        super(GSON, "items", Registries.ITEM);
    }

    @Override
    public void injectJsons(Map<ResourceLocation, JsonElement> map) {
        for (ResourceLocation id : autoRegisteredBlockItems.keySet()) {
            var json = new JsonObject();
            json.addProperty("type", "palladium:block_item");
            json.addProperty("block", id.toString());
            JsonArray jsonArray = new JsonArray();
            if(this.autoRegisteredBlockItems.get(id) != null) {
                for (PlacedTabPlacement tabPlacement : this.autoRegisteredBlockItems.get(id)) {
                    jsonArray.add(tabPlacement.toJson());
                }
            }
            json.add("creative_mode_tab", jsonArray);
            map.put(id, json);
        }
    }

    @Override
    public AddonBuilder<Item> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        ItemBuilder builder = new ItemBuilder(id, json);

        builder.type(GsonUtil.getAsResourceLocation(json, "type", null))
                .maxStackSize(GsonUtil.getAsIntRanged(json, "max_stack_size", 1, 64, 64))
                .maxDamage(GsonUtil.getAsIntMin(json, "max_damage", 1, 0))
                .rarity(getRarity(GsonHelper.getAsString(json, "rarity", null)))
                .fireResistant(GsonHelper.getAsBoolean(json, "is_fire_resistant", false))
                .tooltipLines(GsonUtil.getAsComponentList(json, "tooltip", null));

        GsonUtil.ifHasKey(json, "creative_mode_tab", je -> {
            for (PlacedTabPlacement placedTabPlacement : GsonUtil.fromListOrPrimitive(je, PlacedTabPlacement::fromJson)) {
                builder.creativeModeTab(placedTabPlacement);
            }
        });

        GsonUtil.ifHasKey(json, "attribute_modifiers", je -> parseAttributeModifiers(builder, je));

        if (Platform.isClient()) {
            GsonUtil.ifHasObject(json, "render_layers", jsonObject -> {
                IAddonItem.RenderLayerContainer container = new IAddonItem.RenderLayerContainer();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    GsonUtil.forEachInListOrPrimitive(entry.getValue(), idElement -> {
                        container.addLayer(key, new ResourceLocation(idElement.getAsString()));
                    });
                }
                builder.setRenderLayerContainer(container);
            });
        }

        GsonUtil.ifHasObject(json, "food", foodJson -> {
            FoodProperties.Builder properties = new FoodProperties.Builder();

            GsonUtil.ifHasKey(foodJson, "nutrition", el -> properties.nutrition(GsonHelper.convertToInt(el, "$.food.nutrition")));
            GsonUtil.ifHasKey(foodJson, "saturation_modifier", el -> properties.saturationMod(GsonHelper.convertToFloat(el, "$.food.saturation_modifier")));

            if (GsonHelper.getAsBoolean(foodJson, "meat", false)) {
                properties.meat();
            }

            if (GsonHelper.getAsBoolean(foodJson, "can_always_eat", false)) {
                properties.alwaysEat();
            }

            if (GsonHelper.getAsBoolean(foodJson, "fast", false)) {
                properties.fast();
            }

            GsonUtil.ifHasArray(foodJson, "effects", effectEl -> {
                JsonObject effect = GsonHelper.convertToJsonObject(effectEl, "$.food.effects");
                ResourceLocation mobEffect = GsonUtil.getAsResourceLocation(effect, "mob_effect");

                if (!BuiltInRegistries.MOB_EFFECT.containsKey(mobEffect)) {
                    throw new JsonParseException("Mob effect type '" + mobEffect.toString() + "' does not exist");
                }

                int duration = GsonHelper.getAsInt(effect, "duration", 0);
                int amplifier = GsonHelper.getAsInt(effect, "amplifier", 0);
                boolean ambient = GsonHelper.getAsBoolean(effect, "ambient", false);
                boolean visible = GsonHelper.getAsBoolean(effect, "visible", true);
                boolean showIcon = GsonHelper.getAsBoolean(effect, "show_icon", true);
                float probability = GsonHelper.getAsFloat(effect, "probability", 1F);

                properties.effect(new MobEffectInstance(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.get(mobEffect)), duration, amplifier, ambient, visible, showIcon), probability);
            });

            builder.food(properties.build());
        });

        return builder;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "items"), "Items")
                .add(HTMLBuilder.heading("Items"))
                .add(HTMLBuilder.subHeading("Global Settings"))
                .addDocumentation(getDefaultDocumentationBuilder())
                .addDocumentationSettings(new ArrayList<>(TYPE_SERIALIZERS.values()));
    }

    static {
        registerTypeSerializer(new AddonItem.Parser());
        registerTypeSerializer(new AddonBlockItem.Parser());
        registerTypeSerializer(new AddonArmorItem.Parser());
        registerTypeSerializer(new AddonSwordItem.Parser());
        registerTypeSerializer(new AddonPickaxeItem.Parser());
        registerTypeSerializer(new AddonAxeItem.Parser());
        registerTypeSerializer(new AddonShovelItem.Parser());
        registerTypeSerializer(new AddonHoeItem.Parser());
        registerTypeSerializer(new AddonShieldItem.Parser());
        registerTypeSerializer(new AddonBowItem.Parser());
        registerTypeSerializer(new AddonCrossbowItem.Parser());
        registerTypeSerializer(new FluxCapacitorItem.Parser());
    }

    public static Rarity getRarity(String name) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.name().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }

    public static void parseAttributeModifiers(ItemBuilder builder, JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            for (String key : object.keySet()) {
                PlayerSlot slot = PlayerSlot.get(key);
                JsonElement mods = object.get(key);

                if (mods.isJsonArray()) {
                    JsonArray modsList = mods.getAsJsonArray();

                    for (JsonElement mod : modsList) {
                        builder.addAttributeModifier(slot, GsonUtil.getAsResourceLocation(mod.getAsJsonObject(), "attribute"), parseAttributeModifier(mod.getAsJsonObject()));
                    }
                } else if (mods.isJsonObject()) {
                    builder.addAttributeModifier(slot, GsonUtil.getAsResourceLocation(mods.getAsJsonObject(), "attribute"), parseAttributeModifier(mods.getAsJsonObject()));
                } else {
                    throw new JsonSyntaxException("Attribute modifiers definitions need to be either an object or an array");
                }
            }
        } else {
            throw new JsonSyntaxException("The attribute modifier definition needs to be an object");
        }
    }

    public static AttributeModifier parseAttributeModifier(JsonObject json) {
        UUID uuid = GsonUtil.getAsUUID(json, "uuid");
        String name = GsonHelper.getAsString(json, "name", "Addonpack Item Modifier");
        double amount = GsonHelper.getAsDouble(json, "amount");
        AttributeModifier.Operation operation = AttributeModifier.Operation.fromValue(GsonUtil.getAsIntRanged(json, "operation", AttributeModifier.Operation.ADDITION.toValue(), AttributeModifier.Operation.MULTIPLY_TOTAL.toValue()));
        return new AttributeModifier(uuid, name, amount, operation);
    }

    public static void registerTypeSerializer(ItemTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    public static ItemTypeSerializer getTypeSerializer(ResourceLocation id) {
        return TYPE_SERIALIZERS.get(id);
    }

    public static JsonDocumentationBuilder getDefaultDocumentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder();

        builder.setDescription("These settings apply to ALL item types. Keep in mind that if fields are not required, you do NOT need to write them into your json.");

        builder.addProperty("type", ResourceLocation.class)
                .description("Item Type, each come with new different settings. Listed below on this page.")
                .fallback(new ResourceLocation("palladium:default"));
        builder.addProperty("max_stack_size", Integer.class)
                .description("Max stack size for an itemstack. Range: 1-64")
                .fallback(64)
                .exampleJson(new JsonPrimitive(64));
        builder.addProperty("max_damage", Integer.class)
                .description("Max damage for an item. Must be greater then or equal 0.")
                .fallback(0);
        builder.addProperty("creative_mode_tab", ResourceLocation.class)
                .description("ID of the creative mode tab the item is supposed to appear in. Fore more precise placements, check the \"Custom Items\" page on the wiki. Possible values: " + Arrays.toString(BuiltInRegistries.CREATIVE_MODE_TAB.keySet().stream().sorted(Comparator.comparing(ResourceLocation::toString)).toArray()))
                .fallback(null)
                .exampleJson(new JsonPrimitive("minecraft:decorations"));
        builder.addProperty("rarity", String.class)
                .description("Rarity of the item, influences the item name's color. Possible values: " + Arrays.toString(Arrays.stream(Rarity.values()).map(r -> r.toString().toLowerCase(Locale.ROOT)).toArray()))
                .fallback(null)
                .exampleJson(new JsonPrimitive("epic"));
        builder.addProperty("is_fire_resistant", Boolean.class)
                .description("Whether or not the item will survive being thrown into fire/lava.")
                .fallback(false)
                .exampleJson(new JsonPrimitive(false));

        JsonArray tooltipExample = new JsonArray();
        tooltipExample.add("Line 1");
        JsonObject line2 = new JsonObject();
        line2.addProperty("translate", "example.line2.translation_key");
        line2.addProperty("color", "#BCD42A");
        line2.addProperty("underlined", true);
        tooltipExample.add(line2);

        builder.addProperty("tooltip", Component[].class)
                .description("Tooltip lines. Can be array of primitive strings or more complex text component")
                .fallback(null)
                .exampleJson(tooltipExample);

        JsonObject attributeModifiers = GsonHelper.fromJson(GSON, "{ \"all\": [ { \"attribute\": \"minecraft:generic.max_health\", \"amount\": 2, \"operation\": 0, \"uuid\": \"f98db25e-91cb-45ca-ba40-5526ff2cd180\" } ]," +
                "\"chest\": [ { \"attribute\": \"minecraft:generic.movement_speed\", \"amount\": 4, \"operation\": 1, \"uuid\": \"3a4df804-2be2-4002-a829-eaf29a629cac\" } ] }", JsonObject.class);

        builder.addProperty("attribute_modifiers", AttributeModifier[].class)
                .description("Attribute modifiers when having the item equipped. You first specify the slot (\"all\" for every slot, other options: " + Arrays.toString(Arrays.stream(EquipmentSlot.values()).map(EquipmentSlot::getName).toArray()) + "), then an array for different modifiers. Possible attributes: " + AttributeModifierAbility.getAttributeList())
                .fallback(null)
                .exampleJson(attributeModifiers);

        JsonObject foodExample = new JsonObject();
        foodExample.addProperty("nutrition", 5);
        foodExample.addProperty("saturation_modifier", 0.6F);
        foodExample.addProperty("meat", false);
        foodExample.addProperty("can_always_eat", false);
        foodExample.addProperty("fast", false);
        JsonArray effectsExample = new JsonArray();
        JsonObject effectExample = new JsonObject();
        effectExample.addProperty("mob_effect", Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(MobEffects.DAMAGE_BOOST)).toString());
        effectExample.addProperty("duration", 40);
        effectExample.addProperty("amplifier", 1);
        effectExample.addProperty("ambient", false);
        effectExample.addProperty("visible", true);
        effectExample.addProperty("show_icon", true);
        effectExample.addProperty("probability", 1F);
        effectsExample.add(effectExample);
        foodExample.add("effects", effectsExample);

        builder.addProperty("food", FoodProperties.class)
                .description("Settings to make this item edible. The only required field in this json part is the mob_effect IF you add any effect")
                .fallback(null)
                .exampleJson(foodExample);

        return builder;
    }

    public interface ItemTypeSerializer extends IDocumentedConfigurable {

        IAddonItem parse(JsonObject json, Item.Properties properties);
    }

    public static class PlacedTabPlacement {

        // 0 = add, 1 = addAfter, 2 = addBefore
        private final int type;
        private final ResourceLocation referencedItem;
        private final ResourceLocation tab;

        private PlacedTabPlacement(int type, ResourceLocation referencedItem, ResourceLocation tab) {
            this.type = type;
            this.referencedItem = referencedItem;
            this.tab = tab;
        }

        public ResourceLocation getTab() {
            return this.tab;
        }

        public static PlacedTabPlacement add(ResourceLocation tab) {
            return new PlacedTabPlacement(0, null, tab);
        }

        public static PlacedTabPlacement addAfter(ResourceLocation afterItem, ResourceLocation tab) {
            return new PlacedTabPlacement(1, afterItem, tab);
        }

        public static PlacedTabPlacement addBefore(ResourceLocation beforeItem, ResourceLocation tab) {
            return new PlacedTabPlacement(2, beforeItem, tab);
        }

        public void addToTab(CreativeModeTabRegistry.ItemGroupEntries entries, Item item) {
            if (this.type == 0) {
                entries.add(item);
            } else if (type == 1) {
                var addAfter = BuiltInRegistries.ITEM.get(this.referencedItem);

                if (addAfter == Items.AIR) {
                    AddonPackLog.warning("Tried to add '" + BuiltInRegistries.ITEM.getKey(item) + "' after unknown item '" + this.referencedItem + "' in creative mode tab");
                    entries.add(item);
                } else {
                    entries.addAfter(addAfter, item);
                }
            } else if (type == 2) {
                var addBefore = BuiltInRegistries.ITEM.get(this.referencedItem);

                if (addBefore == Items.AIR) {
                    AddonPackLog.warning("Tried to add '" + BuiltInRegistries.ITEM.getKey(item) + "' before unknown item '" + this.referencedItem + "' in creative mode tab");
                    entries.add(item);
                } else {
                    entries.addBefore(addBefore, item);
                }
            }
        }

        public static PlacedTabPlacement fromJson(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                return add(GsonUtil.convertToResourceLocation(jsonElement, "creative_mode_tab"));
            } else {
                var json = GsonHelper.convertToJsonObject(jsonElement, "creative_mode_tab");

                if (GsonHelper.isValidNode(json, "after")) {
                    return addAfter(GsonUtil.convertToResourceLocation(json.get("after"), "creative_mode_tab.after"), GsonUtil.getAsResourceLocation(json, "tab"));
                } else if (GsonHelper.isValidNode(json, "before")) {
                    return addBefore(GsonUtil.convertToResourceLocation(json.get("before"), "creative_mode_tab.after"), GsonUtil.getAsResourceLocation(json, "tab"));
                } else {
                    return add(GsonUtil.getAsResourceLocation(json, "tab"));
                }
            }
        }

        public JsonElement toJson() {
            if (this.type == 0) {
                return new JsonPrimitive(this.tab.toString());
            } else {
                var json = new JsonObject();
                json.addProperty(this.type == 1 ? "after" : "before", this.referencedItem.toString());
                json.addProperty("tab", this.tab.toString());
                return json;
            }
        }

    }
}
