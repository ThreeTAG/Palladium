package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
import net.threetag.palladium.network.SetPowerMessage;
import net.threetag.palladium.network.SyncPowersMessage;
import net.threetag.palladium.power.holderfactory.PowerProviderFactory;
import net.threetag.palladium.power.holderfactory.SuperpowerPowerProviderFactory;
import net.threetag.palladium.power.provider.IPowerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class PowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
    private Map<ResourceLocation, IPowerProvider> providers = ImmutableMap.of();
    private Map<ResourceLocation, Power> byName = ImmutableMap.of();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, INSTANCE = new PowerManager());

        PalladiumEvents.LIVING_UPDATE.register(entity -> PowerManager.getPowerHandler(entity).tick());

        PlayerEvent.PLAYER_JOIN.register(player -> {
            new SyncPowersMessage(getInstance(player.level).byName).sendTo(player);
            getPowerHandler(player).getPowerHolders().forEach((provider, holder) -> new SetPowerMessage(player.getId(), provider, holder != null ? holder.getPower().getId() : null).sendTo(player));
        });

        PalladiumEvents.START_TRACKING.register((tracker, target) -> {
            if (target instanceof LivingEntity livingEntity && tracker instanceof ServerPlayer serverPlayer) {
                getPowerHandler(livingEntity).getPowerHolders().forEach((provider, holder) -> new SetPowerMessage(target.getId(), provider, holder != null ? holder.getPower().getId() : null).sendTo(serverPlayer));
            }
        });

        PalladiumEvents.REGISTER_PROPERTY.register(handler -> handler.register(SuperpowerPowerProviderFactory.SUPERPOWER_ID, null));
    }

    public PowerManager() {
        super(GSON, "powers");
    }

    public static PowerManager getInstance(@Nullable Level level) {
        return level != null && level.isClientSide ? ClientPowerManager.INSTANCE : INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        // Providers
        this.providers = generateProviders();

        // Powers
        this.byName.values().forEach(Power::invalidate);
        ImmutableMap.Builder<ResourceLocation, Power> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, Power.fromJSON(id, json.getAsJsonObject()));
            } catch (Exception e) {
                Palladium.LOGGER.error("Parsing error loading power {}", id, e);
            }
        });
        this.byName = builder.build();
        Palladium.LOGGER.info("Loaded {} powers", this.byName.size());
        syncPowersToAll(this.byName);
    }

    public static Map<ResourceLocation, IPowerProvider> generateProviders() {
        ImmutableMap.Builder<ResourceLocation, IPowerProvider> providerBuilder = ImmutableMap.builder();
        for (PowerProviderFactory factory : PowerProviderFactory.REGISTRY) {
            factory.create(provider -> providerBuilder.put(provider.getKey(), provider));
        }
        return providerBuilder.build();
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

    public Collection<IPowerProvider> getProviders() {
        return this.providers.values();
    }

    @ExpectPlatform
    public static IPowerHandler getPowerHandler(LivingEntity entity) {
        throw new AssertionError();
    }
}
