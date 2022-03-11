package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ItemBuilder;
import net.threetag.palladium.item.AddonItem;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemParser extends AddonParser<Item> {

    private static final Map<ResourceLocation, ItemTypeSerializer> TYPE_SERIALIZERS = new HashMap<>();

    public ItemParser() {
        super(GSON, "items", Registry.ITEM_REGISTRY);
    }

    @Override
    public AddonBuilder<Item> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        ItemBuilder builder = new ItemBuilder(id, json);

        builder.type(TYPE_SERIALIZERS.get(GsonUtil.getAsResourceLocation(json, "type", null)))
                .maxStackSize(GsonUtil.getAsIntRanged(json, "max_stack_size", 1, 64, 64))
                .maxDamage(GsonUtil.getAsIntMin(json, "max_damage", 1, 0))
                .creativeModeTab(GsonUtil.getAsResourceLocation(json, "creative_mode_tab", null))
                .rarity(getRarity(GsonHelper.getAsString(json, "rarity", null)))
                .fireResistant(GsonHelper.getAsBoolean(json, "is_fire_resistant", false))
                .tooltipLines(GsonUtil.getAsComponentList(json, "tooltip", null));

        GsonUtil.ifHasKey(json, "attribute_modifiers", je -> parseAttributeModifiers(builder, je));

        return builder;
    }

    static {
        registerTypeSerializer(new ResourceLocation(Palladium.MOD_ID, "default"), (json, properties) -> new AddonItem(properties));
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
                EquipmentSlot slot;
                try {
                    slot = EquipmentSlot.byName(key);
                } catch (Exception e) {
                    slot = null;
                }

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

    public static void registerTypeSerializer(ResourceLocation id, ItemTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(id, serializer);
    }

    public interface ItemTypeSerializer {

        IAddonItem parse(JsonObject json, Item.Properties properties);

    }
}
