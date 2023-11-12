package net.threetag.palladium.addonpack.parser;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ItemBuilder;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.*;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.util.Platform;

import java.util.*;

public class ItemParser extends AddonParser<Item> {

    public static final ResourceLocation FALLBACK_SERIALIZER = Palladium.id("default");
    private static final Map<ResourceLocation, ItemTypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();
    public final Map<ResourceLocation, ResourceLocation> autoRegisteredBlockItems = new HashMap<>();

    public ItemParser() {
        super(GSON, "items", Registry.ITEM_REGISTRY);
    }

    @Override
    public void injectJsons(Map<ResourceLocation, JsonElement> map) {
        for (ResourceLocation id : autoRegisteredBlockItems.keySet()) {
            var json = new JsonObject();
            json.addProperty("type", "palladium:block_item");
            json.addProperty("block", id.toString());
            json.addProperty("creative_mode_tab", autoRegisteredBlockItems.get(id).toString());
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
                .creativeModeTab(GsonUtil.getAsResourceLocation(json, "creative_mode_tab", null))
                .rarity(getRarity(GsonHelper.getAsString(json, "rarity", null)))
                .fireResistant(GsonHelper.getAsBoolean(json, "is_fire_resistant", false))
                .tooltipLines(GsonUtil.getAsComponentList(json, "tooltip", null));

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

                if (!Registry.MOB_EFFECT.containsKey(mobEffect)) {
                    throw new JsonParseException("Mob effect type '" + mobEffect.toString() + "' does not exist");
                }

                int duration = GsonHelper.getAsInt(effect, "duration", 0);
                int amplifier = GsonHelper.getAsInt(effect, "amplifier", 0);
                boolean ambient = GsonHelper.getAsBoolean(effect, "ambient", false);
                boolean visible = GsonHelper.getAsBoolean(effect, "visible", true);
                boolean showIcon = GsonHelper.getAsBoolean(effect, "show_icon", true);
                float probability = GsonHelper.getAsFloat(effect, "probability", 1F);

                properties.effect(new MobEffectInstance(Objects.requireNonNull(Registry.MOB_EFFECT.get(mobEffect)), duration, amplifier, ambient, visible, showIcon), probability);
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
                .description("ID of the creative mode tab the item is supposed to appear in. Possible values: " + Arrays.toString(PalladiumCreativeModeTabs.getTabs().stream().sorted(Comparator.comparing(ResourceLocation::toString)).toArray()))
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
                .description("Attribute modifiers when having the item equipped. You first specify the slot (\"all\" for every slot, other options: " + Arrays.toString(Arrays.stream(EquipmentSlot.values()).map(EquipmentSlot::getName).toArray()) + "), then an array for different modifiers. Possible attributes: " + Arrays.toString(Registry.ATTRIBUTE.stream().map(attribute -> Objects.requireNonNull(Registry.ATTRIBUTE.getKey(attribute)).toString()).toArray()))
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
        effectExample.addProperty("mob_effect", Objects.requireNonNull(Registry.MOB_EFFECT.getKey(MobEffects.DAMAGE_BOOST)).toString());
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
}
