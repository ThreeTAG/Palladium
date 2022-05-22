package net.threetag.palladium.client.dynamictexture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.dynamictexture.transformer.AlphaMaskTextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.OverlayTextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.*;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class DynamicTexture {

    private static final Map<ResourceLocation, Function<JsonObject, DynamicTexture>> TYPE_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, Function<JsonObject, ITextureTransformer>> TRANSFORMER_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, Function<JsonObject, ITextureVariable>> VARIABLE_PARSERS = new HashMap<>();

    static {
        registerType(Palladium.id("default"), j -> new DefaultDynamicTexture(GsonHelper.getAsString(j, "base"), GsonHelper.getAsString(j, "output", "")));

        registerTransformer(Palladium.id("alpha_mask"), j -> new AlphaMaskTextureTransformer(GsonHelper.getAsString(j, "mask")));
        registerTransformer(Palladium.id("overlay"), j -> new OverlayTextureTransformer(GsonHelper.getAsString(j, "overlay")));

        registerVariable(Palladium.id("entity_ticks"), EntityTicksTextureVariable::new);
        registerVariable(Palladium.id("entity_health"), EntityHealthTextureVariable::new);
        registerVariable(Palladium.id("ability_integer_property"), AbilityIntegerPropertyVariable::new);
        registerVariable(Palladium.id("small_arms"), SmallArmsTextureVariable::new);
        registerVariable(Palladium.id("crouching"), CrouchingTextureVariable::new);
        registerVariable(Palladium.id("moon_phase"), MoonPhaseTextureVariable::new);
        registerVariable(Palladium.id("cape"), CapeTextureVariable::new);
    }

    public abstract ResourceLocation getTexture(LivingEntity entity);

    public abstract DynamicTexture transform(ITextureTransformer textureTransformer);

    public abstract DynamicTexture addVariable(String name, ITextureVariable variable);

    public static DynamicTexture parse(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new DefaultDynamicTexture(jsonElement.getAsString(), null);
        } else if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            ResourceLocation typeId = GsonUtil.getAsResourceLocation(json, "type", new ResourceLocation(Palladium.MOD_ID, "default"));

            if (!TYPE_PARSERS.containsKey(typeId)) {
                Palladium.LOGGER.error("Unknown dynamic texture type '" + typeId + "'");
                return null;
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
                        Palladium.LOGGER.error("Unknown texture transformer '" + transformerId + "'");
                    }
                }
            }

            if (GsonHelper.isValidNode(json, "variables")) {
                JsonObject variables = GsonHelper.getAsJsonObject(json, "variables");

                for (Map.Entry<String, JsonElement> entry : variables.entrySet()) {
                    JsonObject variableJson = entry.getValue().getAsJsonObject();
                    ResourceLocation variableId = GsonUtil.getAsResourceLocation(variableJson, "type");
                    if (VARIABLE_PARSERS.containsKey(variableId)) {
                        ITextureVariable variable = VARIABLE_PARSERS.get(variableId).apply(variableJson);
                        texture.addVariable(entry.getKey(), variable);
                    } else {
                        Palladium.LOGGER.error("Unknown texture variable '" + variableId + "'");
                    }
                }
            }

            return texture;
        }

        return null;
    }

    public static void registerType(ResourceLocation id, Function<JsonObject, DynamicTexture> function) {
        TYPE_PARSERS.put(id, function);
    }

    public static void registerTransformer(ResourceLocation id, Function<JsonObject, ITextureTransformer> function) {
        TRANSFORMER_PARSERS.put(id, function);
    }

    public static void registerVariable(ResourceLocation id, Function<JsonObject, ITextureVariable> function) {
        VARIABLE_PARSERS.put(id, function);
    }
}

