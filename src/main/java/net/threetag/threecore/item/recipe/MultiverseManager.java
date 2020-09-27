package net.threetag.threecore.item.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.network.SyncMultiverseMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class MultiverseManager {

    private static final Map<String, Universe> UNIVERSES = Maps.newHashMap();
    public static final List<String> CLIENT_UNIVERSES = Lists.newArrayList();
    private static final Map<ResourceLocation, Function<JsonObject, Function<PlayerEntity, Boolean>>> CONDITIONS = Maps.newHashMap();
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    static {
        registerCondition(new ResourceLocation(ThreeCore.MODID, "date"), json -> {
            LocalDate start = LocalDate.of(JSONUtils.getInt(json, "startYear"), Month.of(JSONUtils.getInt(json, "startMonth")), JSONUtils.getInt(json, "startDay"));
            LocalDate end = LocalDate.of(JSONUtils.getInt(json, "endYear"), Month.of(JSONUtils.getInt(json, "endMonth")), JSONUtils.getInt(json, "endDay"));
            return player -> LocalDate.now().isAfter(start) && LocalDate.now().isBefore(end);
        });

        registerCondition(new ResourceLocation(ThreeCore.MODID, "date_without_year"), json -> {
            LocalDate now = LocalDate.now();
            LocalDate start = LocalDate.of(now.getYear(), Month.of(JSONUtils.getInt(json, "startMonth")), JSONUtils.getInt(json, "startDay"));
            LocalDate end = LocalDate.of(now.getYear(), Month.of(JSONUtils.getInt(json, "endMonth")), JSONUtils.getInt(json, "endDay"));
            return player -> now.isAfter(start) && now.isBefore(end);
        });
    }

    public static void reload(IResourceManager resourceManagerIn) {
        UNIVERSES.clear();
        registerUniverse("earth-18515");

        for (String namespace : resourceManagerIn.getResourceNamespaces()) {
            try {
                for (IResource resource : resourceManagerIn.getAllResources(new ResourceLocation(namespace, "multiverse.json"))) {
                    try (
                            InputStream inputstream = resource.getInputStream();
                            Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
                    ) {
                        JsonObject json = JSONUtils.fromJson(GSON, reader, JsonObject.class);
                        json.entrySet().forEach((e) -> {
                            String id = e.getKey();

                            Universe universe = new Universe(id);
                            universe.addConditions(parseConditions(e.getValue().getAsJsonObject().get("conditions")));
                            registerUniverse(universe);
                        });
                    } catch (RuntimeException runtimeexception) {
                        ThreeCore.LOGGER.warn("Invalid multiverse.json in datapack: '{}'", resource.getPackName(), runtimeexception);
                    }
                }
            } catch (IOException e) {

            }
        }
    }

    public static void sync() {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            List<String> ids = getUniverses().stream().map(Universe::getIdentifier).collect(Collectors.toList());
            for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncMultiverseMessage(ids), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof ServerPlayerEntity) {
            List<String> ids = getUniverses().stream().map(Universe::getIdentifier).collect(Collectors.toList());
            ThreeCore.NETWORK_CHANNEL.sendTo(new SyncMultiverseMessage(ids), ((ServerPlayerEntity) e.getEntity()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static String registerUniverse(String id) {
        if (!UNIVERSES.containsKey(id)) {
            UNIVERSES.put(id, new Universe(id));
        }

        return id;
    }

    public static Universe registerUniverse(Universe universe) {
        if (UNIVERSES.containsKey(universe.identifier)) {
            Universe u = UNIVERSES.get(universe.identifier);
            u.conditions.addAll(universe.conditions);
            return u;
        } else {
            UNIVERSES.put(universe.identifier, universe);
            return universe;
        }
    }

    public static boolean registerCondition(ResourceLocation id, Function<JsonObject, Function<PlayerEntity, Boolean>> function) {
        if (CONDITIONS.containsKey(id)) {
            ThreeCore.LOGGER.warn("Universe condition '" + id.toString() + "' already exists!");
            return false;
        } else {
            CONDITIONS.put(id, function);
            return true;
        }
    }

    private static List<Function<PlayerEntity, Boolean>> parseConditions(JsonElement jsonElement) {
        if (jsonElement == null) {
            return Collections.emptyList();
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
            Function<JsonObject, Function<PlayerEntity, Boolean>> function = CONDITIONS.get(id);
            if (function == null) {
                ThreeCore.LOGGER.warn("Multiverse condition '" + JSONUtils.getString(jsonObject, "type") + "' does not exist!");
                return Collections.emptyList();
            }
            return Collections.singletonList(function.apply(jsonObject));
        } else if (jsonElement.isJsonArray()) {
            List<Function<PlayerEntity, Boolean>> list = Lists.newArrayList();
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement1.getAsJsonObject();
                ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
                Function<JsonObject, Function<PlayerEntity, Boolean>> function = CONDITIONS.get(id);
                if (function == null) {
                    ThreeCore.LOGGER.warn("Multiverse condition '" + JSONUtils.getString(jsonObject, "type") + "' does not exist!");
                } else {
                    list.add(function.apply(jsonObject));
                }
            }
            return list;
        } else {
            throw new JsonParseException("Condition list must be a json object or json array!");
        }
    }

    public static Universe getRandomAvailableUniverse(PlayerEntity playerEntity) {
        List<Universe> universes = getUniverses().stream().filter(universe -> universe.isAvailable(playerEntity)).collect(Collectors.toList());
        return universes.get(new Random().nextInt(universes.size()));
    }

    public static List<Universe> getUniverses() {
        return ImmutableList.copyOf(UNIVERSES.values());
    }

    public static Universe getUniverse(String id) {
        return UNIVERSES.get(id);
    }

    public static class Universe {

        private final String identifier;
        private List<Function<PlayerEntity, Boolean>> conditions;

        public Universe(String identifier) {
            this.identifier = identifier;
            this.conditions = Lists.newArrayList();
        }

        public String getIdentifier() {
            return identifier;
        }

        public Universe addCondition(Function<PlayerEntity, Boolean> condition) {
            this.conditions.add(condition);
            return this;
        }

        public Universe addConditions(List<Function<PlayerEntity, Boolean>> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public boolean isAvailable(PlayerEntity playerEntity) {
            for (Function<PlayerEntity, Boolean> condition : this.conditions) {
                if (!condition.apply(playerEntity)) {
                    return false;
                }
            }
            return true;
        }
    }
}
