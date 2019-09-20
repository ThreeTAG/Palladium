package net.threetag.threecore.util.client.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class EntityModelManager extends JsonReloadListener {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<ResourceLocation, Function<JsonObject, EntityModel>> PARSER = Maps.newHashMap();

    public EntityModelManager() {
        super(GSON, "models/entity");
    }

    static {
        // Default
        registerModelParser(new ResourceLocation(ThreeCore.MODID, "default"), new EntityModelParser());

        // Biped Model
        registerModelParser(new ResourceLocation(ThreeCore.MODID, "biped"), new BipedModelParser());
    }

    @Override
    protected Map<ResourceLocation, JsonObject> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        return super.prepare(resourceManagerIn, profilerIn);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonObject> entry : splashList.entrySet()) {
            try {
                EntityModel model = parseModel(entry.getValue());
                ModelRegistry.registerModel(entry.getKey().toString(), model);
            } catch (Exception e) {
                ThreeCore.LOGGER.error("Parsing error loading entity model {}", entry.getKey(), e);
            }
            ThreeCore.LOGGER.info("Loaded entity model {}", entry.getKey());
        }
    }

    public static void registerModelParser(ResourceLocation resourceLocation, Function<JsonObject, EntityModel> function) {
        Preconditions.checkNotNull(resourceLocation);
        Preconditions.checkNotNull(function);
        PARSER.put(resourceLocation, function);
    }

    public static EntityModel parseModel(JsonObject json) {
        Function<JsonObject, EntityModel> function = PARSER.get(new ResourceLocation(JSONUtils.getString(json, "type")));

        if (function == null)
            throw new JsonParseException("The entity model type '" + JSONUtils.getString(json, "type") + "' does not exist!");

        EntityModel model = function.apply(json);
        return Objects.requireNonNull(model);
    }

}
