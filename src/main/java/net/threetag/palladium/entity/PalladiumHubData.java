package net.threetag.palladium.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
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

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumHubData extends PalladiumEntityData<Player, PalladiumHubData> {

    public static final Codec<PalladiumHubData> CODEC = MapCodec.unit(PalladiumHubData::new).codec();
    private static final String BASE_URL = "https://squirrelcontrol.threetag.net/api/";

    private final Set<Identifier> unlockedCustomizations = new HashSet<>();
    private boolean loaded = false;

    public void init() {
        if (!this.getEntity().level().isClientSide()) {
            var uuid = this.getEntity().getUUID();
            CompletableFuture.runAsync(() -> {
                try {
                    JsonObject json = readJsonFromUrl(BASE_URL + "player/" + uuid);
                    var data = GsonHelper.getAsJsonObject(json, "data");
                    this.load(data);
                    Palladium.LOGGER.info("Successfully read user's palladium data! ({})", uuid);
                } catch (Exception e) {
                    if (!FMLEnvironment.isProduction()) {
                        Palladium.LOGGER.warn("Was not able to read user's palladium data! ({}) {}", uuid.toString(), e.getMessage());
                    }
                    this.loaded = false;
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
            Identifier id = Identifier.parse(data.get(i).getAsString());

            if (id.getNamespace().equalsIgnoreCase("threecore")) {
                id = Palladium.id(id.getPath());
            }

            if (id.toString().equals("palladium:mechanical_arm")) {
                id = Palladium.id("mechanical_arms");
            }

            this.unlockedCustomizations.add(id);
        }

        this.loaded = true;
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
    public Codec<PalladiumHubData> codec() {
        return CODEC;
    }

    @SubscribeEvent
    static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        get(e.getEntity(), PalladiumEntityDataTypes.PALLADIUM_HUB.get()).init();
    }

}
