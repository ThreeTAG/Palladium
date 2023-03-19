package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.network.SyncPowersMessage;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import net.threetag.palladiumcore.util.DataSyncUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
    public Map<ResourceLocation, Power> byName = ImmutableMap.of();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("powers"), INSTANCE = new PowerManager());

        LivingEntityEvents.TICK.register(entity -> PowerManager.getPowerHandler(entity).ifPresent(IPowerHandler::tick));

        DataSyncUtil.registerDataSync(consumer -> consumer.accept(new SyncPowersMessage(getInstance(true).byName)));
    }

    public PowerManager() {
        super(GSON, "palladium/powers");
    }

    public static PowerManager getInstance(boolean server) {
        return !server ? ClientPowerManager.INSTANCE : INSTANCE;
    }

    public static PowerManager getInstance(@Nullable Level level) {
        return level != null && level.isClientSide ? ClientPowerManager.INSTANCE : INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, Power> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, Power.fromJSON(id, json.getAsJsonObject()));
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading power {}", id, e);
            }
        });
        this.byName.values().forEach(Power::invalidate);
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} powers", this.byName.size());
    }

    public Power getPower(ResourceLocation id) {
        return this.byName.get(id);
    }

    public Set<ResourceLocation> getIds() {
        return this.byName.keySet();
    }

    public Collection<Power> getPowers() {
        return this.byName.values();
    }

    @ExpectPlatform
    public static Optional<IPowerHandler> getPowerHandler(LivingEntity entity) {
        throw new AssertionError();
    }
}
