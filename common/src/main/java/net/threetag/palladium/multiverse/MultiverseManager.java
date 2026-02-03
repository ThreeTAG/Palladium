package net.threetag.palladium.multiverse;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.network.SyncMultiverseMessage;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import net.threetag.palladiumcore.util.DataSyncUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiverseManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static MultiverseManager INSTANCE;
    public Map<ResourceLocation, Universe> byName = ImmutableMap.of();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("multiverse"), INSTANCE = new MultiverseManager());
        DataSyncUtil.registerDataSync(consumer -> consumer.accept(new SyncMultiverseMessage(getInstance(null).byName)));
    }

    public MultiverseManager() {
        super(GSON, "palladium/multiverse");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, Universe> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, Universe.fromJson(id, json.getAsJsonObject()));
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading universe {}", id, e);
            }
        });
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} universes", this.byName.size());
    }

    public Map<ResourceLocation, Universe> getUniverses() {
        return ImmutableMap.copyOf(byName);
    }

    public Universe get(ResourceLocation id) {
        return this.getUniverses().get(id);
    }

    public Universe getRandomAvailableUniverse(DataContext context) {
        List<Universe> available = new ArrayList<>();

        for (Universe universe : this.getUniverses().values()) {
            for (int i = 0; i < universe.getWeight(context); i++) {
                available.add(universe);
            }
        }

        return available.isEmpty() ? null : available.get(RandomSource.create().nextInt(available.size()));
    }

    public static MultiverseManager getInstance(@Nullable Level level) {
        return getInstance(level != null && level.isClientSide());
    }

    public static MultiverseManager getInstance(boolean client) {
        return !client ? INSTANCE : ClientMultiverseManager.INSTANCE;
    }
}
