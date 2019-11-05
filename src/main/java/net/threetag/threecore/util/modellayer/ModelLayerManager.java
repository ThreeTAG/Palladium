package net.threetag.threecore.util.modellayer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullFunction;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.client.model.ModelRegistry;
import net.threetag.threecore.util.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.util.modellayer.predicates.IsSizeChangingPredicate;
import net.threetag.threecore.util.modellayer.predicates.ItemDurabilityPredicate;
import net.threetag.threecore.util.modellayer.predicates.NotPredicate;
import net.threetag.threecore.util.modellayer.texture.DefaultModelTexture;
import net.threetag.threecore.util.modellayer.texture.ModelLayerTexture;
import net.threetag.threecore.util.modellayer.texture.transformer.AlphaMaskTextureTransformer;
import net.threetag.threecore.util.modellayer.texture.transformer.ITextureTransformer;
import net.threetag.threecore.util.modellayer.texture.transformer.OverlayTextureTransformer;
import net.threetag.threecore.util.modellayer.texture.variable.ITextureVariable;
import net.threetag.threecore.util.modellayer.texture.variable.IntegerNbtTextureVariable;
import net.threetag.threecore.util.modellayer.texture.variable.SmallArmsTextureVariable;
import net.threetag.threecore.util.player.PlayerHelper;

import java.util.List;
import java.util.Map;

public class ModelLayerManager {

    private static final Map<ResourceLocation, NonNullFunction<JsonObject, IModelLayerPredicate>> PREDICATES = Maps.newHashMap();
    private static final Map<ResourceLocation, NonNullFunction<JsonObject, ModelLayer>> MODEL_LAYERS = Maps.newHashMap();
    private static final Map<ResourceLocation, NonNullFunction<JsonObject, ModelLayerTexture>> MODEL_LAYER_TEXTURES = Maps.newHashMap();
    private static final Map<ResourceLocation, NonNullFunction<JsonObject, ITextureVariable>> TEXTURE_VARIABLES = Maps.newHashMap();
    private static final Map<ResourceLocation, NonNullFunction<JsonObject, ITextureTransformer>> TEXTURE_TRANSFORMERS = Maps.newHashMap();

    static {
        // Default Layer
        registerModelLayer(new ResourceLocation(ThreeCore.MODID, "default"), j -> new ModelLayer(new LazyLoadBase<BipedModel>(() -> {
            Model model = ModelRegistry.getModel(JSONUtils.getString(j, "model"));
            return model instanceof BipedModel ? (BipedModel) model : null;
        }), ModelLayerTexture.parse(j.get("texture")), JSONUtils.getBoolean(j, "glow", false)));

        // ----------------------------------------------------------------------------------------------------------------------------------------------

        // Default
        registerModelTexture(new ResourceLocation(ThreeCore.MODID, "default"), j -> {
            DefaultModelTexture texture = new DefaultModelTexture(JSONUtils.getString(j, "base"), JSONUtils.getString(j, "output", ""));
            if (JSONUtils.hasField(j, "variables")) {
                JsonObject variables = JSONUtils.getJsonObject(j, "variables");
                variables.entrySet().forEach(e -> {
                    ITextureVariable textureVariable = parseTextureVariable(e.getValue().getAsJsonObject());
                    if (textureVariable != null) {
                        texture.addVariable(e.getKey(), textureVariable);
                    } else {
                        ThreeCore.LOGGER.warn("Texture transformer type '" + JSONUtils.getString(e.getValue().getAsJsonObject(), "type") + "' does not exist!");
                    }
                });
            }
            return texture;
        });

        // ----------------------------------------------------------------------------------------------------------------------------------------------

        // Alpha Mask
        registerTextureTransformer(new ResourceLocation(ThreeCore.MODID, "alpha_mask"), j -> new AlphaMaskTextureTransformer(JSONUtils.getString(j, "mask")));

        // Overlay
        registerTextureTransformer(new ResourceLocation(ThreeCore.MODID, "overlay"), j -> new OverlayTextureTransformer(JSONUtils.getString(j, "overlay")));

        // ----------------------------------------------------------------------------------------------------------------------------------------------

        // Integer NBT
        registerTextureVariable(new ResourceLocation(ThreeCore.MODID, "integer_nbt"), j -> new IntegerNbtTextureVariable(JSONUtils.getString(j, "nbt_tag")));

        registerTextureVariable(new ResourceLocation(ThreeCore.MODID, "small_arms"), j -> new SmallArmsTextureVariable(JSONUtils.getString(j, "name", "")));

        // ----------------------------------------------------------------------------------------------------------------------------------------------

        // Not
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "not"), j -> new NotPredicate(parsePredicate(JSONUtils.getJsonObject(j, "predicate"))));

        // Sneaking
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "sneaking"), j -> context -> context.getAsEntity().isSneaking());

        // Damage
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "durability"), j -> new ItemDurabilityPredicate(JSONUtils.getFloat(j, "min", 0F), JSONUtils.getFloat(j, "max", 1F)));

        // Small Arms
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "small_arms"), j -> c -> c.getAsEntity() instanceof PlayerEntity && PlayerHelper.hasSmallArms((PlayerEntity) c.getAsEntity()));

        // Is Size Changing
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "is_size_changing"), j -> new IsSizeChangingPredicate());
    }

    public static void registerPredicate(ResourceLocation id, NonNullFunction<JsonObject, IModelLayerPredicate> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        PREDICATES.put(id, function);
    }

    public static void registerModelLayer(ResourceLocation id, NonNullFunction<JsonObject, ModelLayer> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        MODEL_LAYERS.put(id, function);
    }

    public static void registerModelTexture(ResourceLocation id, NonNullFunction<JsonObject, ModelLayerTexture> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        MODEL_LAYER_TEXTURES.put(id, function);
    }

    public static void registerTextureVariable(ResourceLocation id, NonNullFunction<JsonObject, ITextureVariable> textureVariable) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(textureVariable);
        TEXTURE_VARIABLES.put(id, textureVariable);
    }

    public static void registerTextureTransformer(ResourceLocation id, NonNullFunction<JsonObject, ITextureTransformer> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        TEXTURE_TRANSFORMERS.put(id, function);
    }

    public static ModelLayerTexture parseTexture(JsonObject jsonObject) {
        ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type", ThreeCore.MODID + ":default"));
        NonNullFunction<JsonObject, ModelLayerTexture> function = MODEL_LAYER_TEXTURES.get(id);
        return function != null ? function.apply(jsonObject) : null;
    }

    public static ITextureTransformer parseTextureTransformer(JsonObject jsonObject) {
        ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
        NonNullFunction<JsonObject, ITextureTransformer> function = TEXTURE_TRANSFORMERS.get(id);
        return function != null ? function.apply(jsonObject) : null;
    }

    public static ITextureVariable parseTextureVariable(JsonObject jsonObject) {
        ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
        NonNullFunction<JsonObject, ITextureVariable> function = TEXTURE_VARIABLES.get(id);
        return function != null ? function.apply(jsonObject) : null;
    }

    public static IModelLayerPredicate parsePredicate(JsonObject json) {
        NonNullFunction<JsonObject, IModelLayerPredicate> function = PREDICATES.get(new ResourceLocation(JSONUtils.getString(json, "type", ThreeCore.MODID + ":default")));
        return function != null ? function.apply(json) : null;
    }

    public static ModelLayer parseLayer(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return ModelLayerLoader.getModelLayer(new ResourceLocation(jsonElement.getAsString()));
        }

        JsonObject json = jsonElement.getAsJsonObject();
        NonNullFunction<JsonObject, ModelLayer> function = MODEL_LAYERS.get(new ResourceLocation(JSONUtils.getString(json, "type", ThreeCore.MODID + ":default")));

        if (function == null)
            return null;

        ModelLayer layer = function.apply(json);
        if (JSONUtils.hasField(json, "predicates")) {
            JsonArray predicateArray = JSONUtils.getJsonArray(json, "predicates");
            for (int i = 0; i < predicateArray.size(); i++) {
                IModelLayerPredicate predicate = parsePredicate(predicateArray.get(i).getAsJsonObject());
                if (predicate != null)
                    layer.addPredicate(predicate);
            }
        }
        return layer;
    }

    public static boolean arePredicatesFulFilled(List<IModelLayerPredicate> predicates, IModelLayerContext context) {
        for (IModelLayerPredicate predicate : predicates) {
            if (!predicate.test(context))
                return false;
        }
        return true;
    }

}
