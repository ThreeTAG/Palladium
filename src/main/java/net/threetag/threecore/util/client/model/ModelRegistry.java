package net.threetag.threecore.util.client.model;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.model.Model;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ModelRegistry {

    private static Map<String, Model> MODELS = Maps.newHashMap();

    public static Model getModel(String key) {
        return MODELS.get(key);
    }

    public static Model registerModel(String key, Model model) {
        MODELS.put(key, model);
        return model;
    }

    public static Model getOrCreateModel(String key, Supplier<Model> modelSupplier) {
        if (MODELS.containsKey(key))
            return getModel(key);
        else
            return registerModel(key, modelSupplier.get());
    }

}