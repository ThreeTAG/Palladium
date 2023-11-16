package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.condition.ChatMessageCondition;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.network.SyncPowersMessage;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladiumcore.event.ChatEvents;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import net.threetag.palladiumcore.util.DataSyncUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static PowerManager INSTANCE;
    public Map<ResourceLocation, Power> byName = ImmutableMap.of();
    public static final List<String> CHECK_FOR_CHAT_MESSAGES = new ArrayList<>();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("powers"), INSTANCE = new PowerManager());

        LivingEntityEvents.TICK.register(entity -> PowerManager.getPowerHandler(entity).ifPresent(IPowerHandler::tick));

        DataSyncUtil.registerDataSync(consumer -> consumer.accept(new SyncPowersMessage(getInstance(true).byName)));

        ChatEvents.SERVER_SUBMITTED.register((player, rawMessage, message) -> {
            if (CHECK_FOR_CHAT_MESSAGES.contains(rawMessage.trim().toLowerCase(Locale.ROOT))) {
                for (AbilityEntry entry : AbilityUtil.getEntries(player)) {
                    for (Condition condition : entry.getConfiguration().getUnlockingConditions()) {
                        if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(rawMessage.trim())) {
                            chat.onChat(player, entry);
                        }
                    }
                    for (Condition condition : entry.getConfiguration().getEnablingConditions()) {
                        if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(rawMessage.trim())) {
                            chat.onChat(player, entry);
                        }
                    }
                }
            }
            return EventResult.pass();
        });
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
        CHECK_FOR_CHAT_MESSAGES.clear();
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

    public static Optional<PowerHandler> getPowerHandler(LivingEntity entity) {
        if (entity instanceof PalladiumLivingEntityExtension ext) {
            return Optional.of(ext.palladium$getPowerHandler());
        } else {
            return Optional.empty();
        }
    }
}
