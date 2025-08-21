package net.threetag.palladium.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PalladiumHubData extends PalladiumEntityData<Player, PalladiumHubData> {

    public static final MapCodec<PalladiumHubData> CODEC = MapCodec.unit(PalladiumHubData::new);
    private static final String BASE_URL = "https://squirrelcontrol.threetag.net/api/";

    private final Set<ResourceLocation> unlockedCustomizations = new HashSet<>();
    private boolean loaded = false;

    public void init() {
        if (!this.getEntity().level().isClientSide) {
            var uuid = this.getEntity().getUUID();
            CompletableFuture.runAsync(() -> {
                try {
                    JsonObject json = readJsonFromUrl(BASE_URL + "player/" + uuid);
                    var data = GsonHelper.getAsJsonObject(json, "data");
                    this.load(data);
                    Palladium.LOGGER.info("Successfully read user's palladium data! ({})", uuid);
                } catch (Exception e) {
                    if (Platform.isDevelopmentEnvironment()) {
                        Palladium.LOGGER.warn("Was not able to read user's palladium data! ({})", uuid.toString());
                    }
                }
            }, Util.backgroundExecutor()).join();
        }
    }

    public void load(JsonObject json) {
        if (this.loaded) {
            return;
        }

        JsonArray data = GsonHelper.getAsJsonArray(json, "accessoires", new JsonArray());

        for (int i = 0; i < data.size(); i++) {
            ResourceLocation id = ResourceLocation.parse(data.get(i).getAsString());

            if (id.getNamespace().equalsIgnoreCase("threecore")) {
                id = Palladium.id(id.getPath());
            }

            this.unlockedCustomizations.add(id);
        }

        this.loaded = true;
        EntityCustomizationHandler.get(this.getEntity()).validateUnlocked(this);
    }

    public boolean hasCustomizationUnlocked(Customization customization) {
        var id = this.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION).getKey(customization);
        return id != null && this.unlockedCustomizations.contains(id);
    }

    public static JsonObject readJsonFromUrl(String url) throws Exception {
        try (InputStream is = URI.create(url).toURL().openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            JsonObject json = GsonHelper.parse(rd);

            if (GsonHelper.getAsInt(json, "error") != 200) {
                throw new Exception("Error while reading json: " + GsonHelper.getAsString(json, "message"));
            }

            return json;
        }
    }

    @Override
    public MapCodec<PalladiumHubData> codec() {
        return CODEC;
    }

    public static void registerEvent() {
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> get(serverPlayer, PalladiumEntityDataTypes.PALLADIUM_HUB.get()).init());
    }
}
