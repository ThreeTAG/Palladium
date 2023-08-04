package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.*;
import net.threetag.palladium.addonpack.builder.AccessoryBuilder;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccessoryParser extends AddonParser<Accessory> {

    private static final Map<ResourceLocation, TypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();

    public AccessoryParser() {
        super(GSON, "accessories", Accessory.REGISTRY.getRegistryKey());
    }

    @Override
    public AddonBuilder<Accessory> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        var builder = new AccessoryBuilder(id, json);

        builder.type(TYPE_SERIALIZERS.get(GsonUtil.getAsResourceLocation(json, "type", null)));

        var slotJson = json.get("slot");
        if (slotJson.isJsonPrimitive()) {
            builder.addSlot(AccessorySlot.getSlotByName(GsonUtil.getAsResourceLocation(json, "slot")));
        } else if (slotJson.isJsonArray()) {
            var array = GsonHelper.getAsJsonArray(json, "slot");
            for (JsonElement element : array) {
                builder.addSlot(AccessorySlot.getSlotByName(GsonUtil.convertToResourceLocation(element, "slot[].$")));
            }
        }

        return builder;
    }

    public static void registerTypeSerializer(TypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    static {
        registerTypeSerializer(new DefaultAccessory.Serializer());
        registerTypeSerializer(new RenderLayerAccessory.Serializer());
        registerTypeSerializer(new OverlayAccessory.Serializer());
        registerTypeSerializer(new HumanoidModelOverlayAccessory.Serializer());
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "accessories"), "Accessories")
                .add(HTMLBuilder.heading("Accessories"))
                .addDocumentationSettings(new ArrayList<>(TYPE_SERIALIZERS.values()));
    }

    public static void addSlotDocumentation(JsonDocumentationBuilder builder) {
        builder.addProperty("slot", ResourceLocation.class)
                .description("ID of the slot the accessory will be in. Can be one or many. Possible values: " + Arrays.toString(AccessorySlot.getSlots().stream().map(AccessorySlot::getName).toArray()))
                .required().exampleJson(new JsonPrimitive("palladium:head"));
    }

    public interface TypeSerializer extends IDocumentedConfigurable {

        DefaultAccessory parse(JsonObject json);

    }
}
