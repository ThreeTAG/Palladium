package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.GameInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPowersMessage;
import net.threetag.palladium.power.provider.IPowerProvider;
import net.threetag.palladium.power.provider.SuperpowerPowerProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PowerManager extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceLocation, Pair<Integer, IPowerProvider>> PROVIDERS = new HashMap<>();
    public static final IPowerProvider SUPERPOWER_PROVIDER = new SuperpowerPowerProvider();

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
    private Map<ResourceLocation, Power> byName = ImmutableMap.of();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, INSTANCE = new PowerManager());
        registerProvider(new ResourceLocation(Palladium.MOD_ID, "superpower"), SUPERPOWER_PROVIDER, 50);

        PalladiumEvents.LIVING_UPDATE.register(entity -> PowerManager.getPowerHolder(entity).tick());

        PlayerEvent.PLAYER_JOIN.register(player -> {
            new SyncPowersMessage(getInstance(player.level).byName).sendTo(player);
            new SyncPowerHolder(player.getId(), PowerManager.getPowerHolder(player).toNBT()).sendTo(player);
        });

        PalladiumEvents.START_TRACKING.register((tracker, target) -> {
            if (target instanceof LivingEntity livingEntity && tracker instanceof ServerPlayer serverPlayer) {
                new SyncPowerHolder(target.getId(), PowerManager.getPowerHolder(livingEntity).toNBT()).sendTo(serverPlayer);
            }
        });
    }

    public PowerManager() {
        super(GSON, "powers");
    }

    public static PowerManager getInstance(Level level) {
        return level.isClientSide ? ClientPowerManager.INSTANCE : INSTANCE;
    }

    public static void registerProvider(ResourceLocation id, IPowerProvider provider, int priority) {
        PROVIDERS.put(id, Pair.of(priority, provider));
    }

    public static IPowerProvider getProvider(ResourceLocation id) {
        var pair = PROVIDERS.get(id);
        return pair != null ? pair.getSecond() : null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.values().forEach(Power::invalidate);
        ImmutableMap.Builder<ResourceLocation, Power> builder = ImmutableMap.builder();
        object.forEach((id, json) -> builder.put(id, Power.fromJSON(id, json.getAsJsonObject())));
        this.byName = builder.build();
        Palladium.LOGGER.info("Loaded {} powers", this.byName.size());
        syncPowersToAll(this.byName);
    }

    public static void syncPowersToAll(Map<ResourceLocation, Power> powers) {
        MinecraftServer server = GameInstance.getServer();
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                new SyncPowersMessage(powers).sendTo(player);
            }
        }
    }

    public Power getPower(ResourceLocation id) {
        return this.byName.get(id);
    }

    public Collection<Power> getPowers() {
        return this.byName.values();
    }

    @ExpectPlatform
    public static IPowerHandler getPowerHandler(LivingEntity entity) {
        throw new AssertionError();
    }
}
