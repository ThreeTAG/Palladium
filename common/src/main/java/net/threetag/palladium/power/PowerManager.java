package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPowerHolder;
import net.threetag.palladium.network.SyncPowersMessage;

import java.util.Collection;
import java.util.Map;

public class PowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
    private Map<ResourceLocation, Power> byName = ImmutableMap.of();

    public PowerManager() {
        super(GSON, "powers");

        // make sure this is only registered once
        if (INSTANCE == null) {
            PalladiumEvents.LIVING_UPDATE.register(entity -> PowerManager.getPowerHolder(entity).tick(entity));

            PlayerEvent.PLAYER_JOIN.register(player -> {
                new SyncPowersMessage(this.byName).sendTo(player);
                new SyncPowerHolder(player.getId(), PowerManager.getPowerHolder(player).toNBT()).sendTo(player);
            });

            PalladiumEvents.START_TRACKING.register((tracker, target) -> {
                if (target instanceof LivingEntity livingEntity && tracker instanceof ServerPlayer serverPlayer) {
                    new SyncPowerHolder(target.getId(), PowerManager.getPowerHolder(livingEntity).toNBT()).sendTo(serverPlayer);
                }
            });
        }

        INSTANCE = this;
    }

    public static PowerManager getInstance() {
        return Platform.getEnvironment() == Env.CLIENT ? ClientPowerManager.INSTANCE : INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.values().forEach(Power::invalidate);
        ImmutableMap.Builder<ResourceLocation, Power> builder = ImmutableMap.builder();
        object.forEach((id, json) -> builder.put(id, Power.fromJSON(id, json.getAsJsonObject())));
        this.byName = builder.build();
        Palladium.LOGGER.info("Loaded {} powers", this.byName.size());
        syncPowersToAll();
    }

    public static void syncPowersToAll() {
        MinecraftServer server = GameInstance.getServer();
        if (server != null) {
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                new SyncPowersMessage(getInstance().byName).sendTo(player);
                new SyncPowerHolder(player.getId(), PowerManager.getPowerHolder(player).toNBT()).sendToLevel((ServerLevel) player.level);
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
    public static IPowerHolder getPowerHolder(LivingEntity entity) {
        throw new AssertionError();
    }
}
