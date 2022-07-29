package net.threetag.palladium.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.GameInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SupporterHandler {

    private static final String BASE_URL = "https://squirrelcontrol.threetag.net/api/";
    private static final Map<UUID, PlayerData> DATA = Maps.newHashMap();
    private static boolean CHECK = false;

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            SupporterHandler.loadPlayerData(player.getUUID());

            if (CHECK && !SupporterHandler.getPlayerData(player.getUUID()).hasModAccess()) {
                player.connection.disconnect(new TextComponent("You are not allowed to use this mod!"));
            }
        });

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> SupporterHandler.loadPlayerData(player.getUUID()));
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            if (player != null)
                DATA.remove(player.getUUID());
        });
    }

    public static PlayerData loadPlayerData(UUID uuid) {
        try {
            JsonObject json = readJsonFromUrl(BASE_URL + "player/" + uuid.toString());
            PlayerData data = new PlayerData(uuid, GsonHelper.getAsJsonObject(json, "data"));
            DATA.put(uuid, data);
            Palladium.LOGGER.info("Successfully read user's supporter data! (" + uuid + ")");

            if (GameInstance.getServer() != null) {
                Player player = GameInstance.getServer().getPlayerList().getPlayer(uuid);

                if (player != null) {
                    Accessory.getPlayerData(player).ifPresent(accessoryData -> accessoryData.validate(player));
                }
            }

            return data;
        } catch (Exception e) {
            Palladium.LOGGER.error("Was not able to read user's supporter data! (" + uuid.toString() + ")");
        }
        PlayerData data = new PlayerData(uuid, new JsonObject());
        DATA.put(uuid, data);
        return data;
    }

    public static void enableSupporterCheck() {
        if (!CHECK) {
            CHECK = true;
            Palladium.LOGGER.info("The supporter check has been enabled!");
        }
    }

    public static boolean isSupporterCheckEnabled() {
        return CHECK;
    }

    public static PlayerData getPlayerData(UUID uuid) {
        if (DATA.containsKey(uuid)) {
            return DATA.get(uuid);
        } else {
            PlayerData data = new PlayerData(uuid, new JsonObject());
            DATA.put(uuid, data);
            return data;
        }
    }

    public static PlayerData getPlayerDataUnsafe(UUID uuid) {
        return DATA.get(uuid);
    }

    public static JsonObject readJsonFromUrl(String url) throws Exception {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            JsonObject json = (new JsonParser()).parse(rd).getAsJsonObject();

            if (GsonHelper.getAsInt(json, "error") != 200) {
                throw new Exception("Error while reading json: " + GsonHelper.getAsString(json, "message"));
            }

            return json;
        } finally {
            is.close();
        }
    }

    public static class PlayerData {

        private final UUID uuid;
        private final List<Accessory> accessories;
        private final boolean modAccess;
        private final boolean hasCloak;
        private ResourceLocation cloakTexture;

        public PlayerData(UUID uuid, JsonObject json) {
            this.uuid = uuid;
            this.accessories = new ArrayList<>();
            JsonArray data = GsonHelper.getAsJsonArray(json, "accessoires", new JsonArray());

            for (int i = 0; i < data.size(); i++) {
                ResourceLocation id = new ResourceLocation(data.get(i).getAsString());

                if(id.getNamespace().equalsIgnoreCase("threecore")) {
                    id = Palladium.id(id.getPath());
                }

                if (Accessory.REGISTRY.contains(id)) {
                    this.accessories.add(Accessory.REGISTRY.get(id));
                }
            }

            this.modAccess = GsonHelper.getAsBoolean(json, "mod_access", false);

            if (GsonHelper.isValidNode(json, "cloak")) {
                this.hasCloak = true;
                if (Platform.getEnv() == EnvType.CLIENT) {
                    loadCloakTexture(GsonHelper.getAsString(json, "cloak"));
                }
            } else {
                this.hasCloak = false;
            }
        }

        @Environment(EnvType.CLIENT)
        public void loadCloakTexture(String url) {
            if (RenderSystem.isOnRenderThread()) {
                try {
                    ResourceLocation resourceLocation = Palladium.id("cloaks/" + this.uuid.toString());
                    Minecraft.getInstance().getTextureManager().release(resourceLocation);
                    InputStream stream = new URL(url).openStream();
                    NativeImage image = NativeImage.read(stream);
                    Minecraft.getInstance().getTextureManager().register(resourceLocation, new DynamicTexture(image));
                    stream.close();
                    this.cloakTexture = resourceLocation;
                } catch (IOException e) {
                    Palladium.LOGGER.error("Error loading supporter cloak texture: " + e.getMessage());
                }
            }
        }

        public boolean hasModAccess() {
            return this.modAccess;
        }

        public boolean hasAccessory(Accessory accessory) {
            return this.accessories.contains(accessory) || Platform.isDevelopmentEnvironment();
        }

        public List<Accessory> getAccessories() {
            return ImmutableList.copyOf(this.accessories);
        }

        public boolean hasCloak() {
            return this.hasCloak;
        }

        @Nullable
        public ResourceLocation getCloakTexture() {
            return this.cloakTexture;
        }
    }

}
