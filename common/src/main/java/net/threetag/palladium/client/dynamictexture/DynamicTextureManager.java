package net.threetag.palladium.client.dynamictexture;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DynamicTextureManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static DynamicTextureManager INSTANCE;

    public Map<ResourceLocation, DynamicTexture> byName = ImmutableMap.of();

    public DynamicTextureManager() {
        super(GSON, "palladium/dynamic_textures");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, DynamicTexture> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, DynamicTexture.parse(json));
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading dynamic texture {}", id, e);
            }
        });
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} dynamic textures", this.byName.size());
    }

    @Nullable
    public DynamicTexture get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
