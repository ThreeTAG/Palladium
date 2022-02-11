package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.power.provider.SuperpowerPowerProvider;

import java.util.Collection;
import java.util.Map;

public class PowerManager extends SimpleJsonResourceReloadListener {

    public static final ResourceKey<Registry<PowerProvider>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "power_providers"));
    public static final Registrar<PowerProvider> PROVIDER_REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new PowerProvider[0]).build();
    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(Palladium.MOD_ID, RESOURCE_KEY);

    public static final RegistrySupplier<PowerProvider> SUPERPOWER_PROVIDER = PROVIDERS.register("superpower", SuperpowerPowerProvider::new);

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
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

        PalladiumEvents.REGISTER_PROPERTY.register(handler -> handler.register(SuperpowerPowerProvider.SUPERPOWER_ID, null));
    }

    public PowerManager() {
        super(GSON, "powers");
    }

    public static PowerManager getInstance(Level level) {
        return level.isClientSide ? ClientPowerManager.INSTANCE : INSTANCE;
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
