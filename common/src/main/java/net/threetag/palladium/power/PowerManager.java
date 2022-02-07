package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.threetag.palladium.Palladium;

import java.util.Map;

public class PowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private Map<ResourceLocation, Power> byName = ImmutableMap.of();

    public PowerManager() {
        super(GSON, "powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, Power> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            builder.put(id, Power.fromJSON(id, json.getAsJsonObject()));
        });
        this.byName = builder.build();
        Palladium.LOGGER.info("Loaded {} powers", this.byName.size());
    }
}
