package net.threetag.palladium.client.dynamictexture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.dynamictexture.transformer.AlphaMaskTextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.OverlayTextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.*;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DynamicTexture {

    private static final Map<ResourceLocation, Function<JsonObject, DynamicTexture>> TYPE_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, Function<JsonObject, ITextureTransformer>> TRANSFORMER_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, ITextureVariableSerializer> VARIABLE_PARSERS = new HashMap<>();

    static {
        registerType(Palladium.id("simple"), j -> new SimpleDynamicTexture(GsonUtil.getAsResourceLocation(j, "texture")));
        registerType(Palladium.id("default"), j -> new DefaultDynamicTexture(GsonHelper.getAsString(j, "base"), GsonHelper.getAsString(j, "output", "")));
        registerType(Palladium.id("entity"), j -> new EntityDynamicTexture(GsonHelper.getAsBoolean(j, "ignore_skin_change", false)));

        registerTransformer(Palladium.id("alpha_mask"), j -> new AlphaMaskTextureTransformer(GsonHelper.getAsString(j, "mask")));
        registerTransformer(Palladium.id("overlay"), j -> new OverlayTextureTransformer(GsonHelper.getAsString(j, "overlay")));

        registerVariable(new ConditionTextureVariable.Serializer());
        registerVariable(new CrouchingTextureVariable.Serializer());
        registerVariable(new SmallArmsTextureVariable.Serializer());
        registerVariable(new AbilityTicksTextureVariable.Serializer());
        registerVariable(new EntityTicksTextureVariable.Serializer());
        registerVariable(new AbilityIntegerPropertyVariable.Serializer());
        registerVariable(new AbilityFloatPropertyVariable.Serializer());
        registerVariable(new EntityHealthTextureVariable.Serializer());
        registerVariable(new MoonPhaseTextureVariable.Serializer());
        registerVariable(new IntegerPropertyVariable.Serializer());
        registerVariable(new FloatPropertyVariable.Serializer());
        registerVariable(new CapeTextureVariable.Serializer());
        registerVariable(new AbilityIdTextureVariable.Serializer());
        registerVariable(new OpenableEquipmentProgressVariable.Serializer());
    }

    public abstract ResourceLocation getTexture(DataContext context);

    public abstract DynamicTexture transform(ITextureTransformer textureTransformer);

    public abstract DynamicTexture addVariable(String name, ITextureVariable variable);

    public static DynamicTexture parse(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            var input = jsonElement.getAsString();
            if (input.equalsIgnoreCase("#entity")) {
                return new EntityDynamicTexture(false);
            } else if (input.startsWith("#")) {
                var dyn = DynamicTextureManager.INSTANCE.get(new ResourceLocation(input.substring(1)));

                if (dyn == null) {
                    throw new JsonParseException("Dynamic texture '" + new ResourceLocation(input.substring(1)) + "' can not be found");
                }

                return dyn;
            } else {
                return new SimpleDynamicTexture(new ResourceLocation(input));
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            ResourceLocation typeId = GsonUtil.getAsResourceLocation(json, "type", new ResourceLocation(Palladium.MOD_ID, "default"));

            if (!TYPE_PARSERS.containsKey(typeId)) {
                throw new JsonParseException("Unknown dynamic texture type '" + typeId + "'");
            }

            DynamicTexture texture = TYPE_PARSERS.get(typeId).apply(json);

            if (GsonHelper.isValidNode(json, "transformers")) {
                JsonArray transformers = GsonHelper.getAsJsonArray(json, "transformers");
                for (int i = 0; i < transformers.size(); i++) {
                    JsonObject transformerJson = transformers.get(i).getAsJsonObject();
                    ResourceLocation transformerId = GsonUtil.getAsResourceLocation(transformerJson, "type", new ResourceLocation(Palladium.MOD_ID, "default"));
                    if (TRANSFORMER_PARSERS.containsKey(transformerId)) {
                        ITextureTransformer transformer = TRANSFORMER_PARSERS.get(transformerId).apply(transformerJson);
                        texture.transform(transformer);
                    } else {
                        AddonPackLog.error("Unknown texture transformer '" + transformerId + "'");
                    }
                }
            }

            if (GsonHelper.isValidNode(json, "variables")) {
                JsonObject variables = GsonHelper.getAsJsonObject(json, "variables");

                for (Map.Entry<String, JsonElement> entry : variables.entrySet()) {
                    JsonObject variableJson = entry.getValue().getAsJsonObject();
                    ResourceLocation variableId = GsonUtil.getAsResourceLocation(variableJson, "type");
                    if (VARIABLE_PARSERS.containsKey(variableId)) {
                        ITextureVariable variable = VARIABLE_PARSERS.get(variableId).parse(variableJson);
                        texture.addVariable(entry.getKey(), variable);
                    } else {
                        AddonPackLog.error("Unknown texture variable '" + variableId + "'");
                    }
                }
            }

            return texture;
        }

        throw new JsonParseException("Dynamic texture must either be a primitive or object");
    }

    public static void registerType(ResourceLocation id, Function<JsonObject, DynamicTexture> function) {
        TYPE_PARSERS.put(id, function);
    }

    public static void registerTransformer(ResourceLocation id, Function<JsonObject, ITextureTransformer> function) {
        TRANSFORMER_PARSERS.put(id, function);
    }

    public static void registerVariable(ITextureVariableSerializer serializer) {
        VARIABLE_PARSERS.put(serializer.getId(), serializer);
    }

    public static HTMLBuilder variableDocumentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "dynamic_textures/variables"), "Dynamic Texture Variables")
                .add(HTMLBuilder.heading("Dynamic Texture Variables"))
                .addDocumentationSettings(VARIABLE_PARSERS.values().stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }
}

