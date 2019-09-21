package net.threetag.threecore.util.armorlayer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.armorlayer.predicates.ItemDurabilityPredicate;
import net.threetag.threecore.util.armorlayer.predicates.NotPredicate;
import net.threetag.threecore.util.client.model.ModelRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ArmorLayerManager {

    private static final Map<ResourceLocation, Function<JsonObject, IArmorLayerPredicate>> PREDICATES = Maps.newHashMap();
    private static final Map<ResourceLocation, Function<JsonObject, ArmorLayer>> ARMOR_LAYERS = Maps.newHashMap();

    static {
        // Default Layer
        registerArmorLayer(new ResourceLocation(ThreeCore.MODID, "default"), j -> new ModelArmorLayer(new LazyLoadBase<BipedModel>(() -> {
            Model model = ModelRegistry.getModel(JSONUtils.getString(j, "model"));
            return model instanceof BipedModel ? (BipedModel) model : null;
        }), new ResourceLocation(JSONUtils.getString(j, "texture"))));

        // Glow Layer
        registerArmorLayer(new ResourceLocation(ThreeCore.MODID, "glow"), j -> new GlowArmorLayer(new LazyLoadBase<BipedModel>(() -> {
            Model model = ModelRegistry.getModel(JSONUtils.getString(j, "model"));
            return model instanceof BipedModel ? (BipedModel) model : null;
        }), new ResourceLocation(JSONUtils.getString(j, "texture"))));

        // ----------------------------------------------------------------------------------------------------------------------------------------------

        // Not
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "not"), j -> new NotPredicate(parsePredicate(JSONUtils.getJsonObject(j, "predicate"))));

        // Sneaking
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "sneaking"), j -> new IArmorLayerPredicate() {
            @Override
            public boolean test(ItemStack stack, @Nullable LivingEntity entity) {
                return entity != null && entity.isSneaking();
            }
        });

        // Damage
        registerPredicate(new ResourceLocation(ThreeCore.MODID, "durability"), j -> new ItemDurabilityPredicate(JSONUtils.getFloat(j, "min", 0F), JSONUtils.getFloat(j, "max", 1F)));
    }

    public static void registerPredicate(ResourceLocation id, Function<JsonObject, IArmorLayerPredicate> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        PREDICATES.put(id, function);
    }

    public static void registerArmorLayer(ResourceLocation id, Function<JsonObject, ArmorLayer> function) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(function);
        ARMOR_LAYERS.put(id, function);
    }

    public static IArmorLayerPredicate parsePredicate(JsonObject json) {
        Function<JsonObject, IArmorLayerPredicate> function = PREDICATES.get(new ResourceLocation(JSONUtils.getString(json, "type")));

        if (function == null)
            return null;

        return function.apply(json);
    }

    public static ArmorLayer parseLayer(JsonObject json) {
        Function<JsonObject, ArmorLayer> function = ARMOR_LAYERS.get(new ResourceLocation(JSONUtils.getString(json, "type")));

        if (function == null)
            return null;

        ArmorLayer layer = function.apply(json);
        if (JSONUtils.hasField(json, "predicates")) {
            JsonArray predicateArray = JSONUtils.getJsonArray(json, "predicates");
            for (int i = 0; i < predicateArray.size(); i++) {
                IArmorLayerPredicate predicate = parsePredicate(predicateArray.get(i).getAsJsonObject());
                if (predicate != null)
                    layer.addPredicate(predicate);
            }
        }
        return layer;
    }

    public static boolean arePredicatesFulFilled(List<IArmorLayerPredicate> predicates, ItemStack stack, LivingEntity entity) {
        for (IArmorLayerPredicate predicate : predicates) {
            if (!predicate.test(stack, entity))
                return false;
        }
        return true;
    }

    public interface IArmorLayerPredicate {

        boolean test(ItemStack stack, @Nullable LivingEntity entity);

    }

}
