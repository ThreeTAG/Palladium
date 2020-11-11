package net.threetag.threecore.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.capability.CapabilityAccessoires;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class SupporterHandler {

    private static final String BASE_URL = "https://squirrelcontrol.threetag.net/api/";
    private static Map<UUID, PlayerData> DATA = Maps.newHashMap();
    private static boolean CHECK = false;

    public static PlayerData loadPlayerData(UUID uuid) {
        try {
            JsonObject json = readJsonFromUrl(BASE_URL + "player/" + uuid.toString());
            PlayerData data = new PlayerData(uuid, JSONUtils.getJsonObject(json, "data"));
            DATA.put(uuid, data);
            ThreeCore.LOGGER.info("Successfully read user's supporter data! (" + uuid.toString() + ")");

            if (ServerLifecycleHooks.getCurrentServer() != null) {
                PlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);

                if (player != null) {
                    player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoires -> accessoires.validate(player));
                }
            }

            return data;
        } catch (Exception e) {
            ThreeCore.LOGGER.error("Was not able to read user's supporter data!");
            e.printStackTrace();
        }
        PlayerData data = new PlayerData(uuid, new JsonObject());
        DATA.put(uuid, data);
        return data;
    }

    public static void enableSupporterCheck() {
        if (!CHECK) {
            CHECK = true;
            ThreeCore.LOGGER.info("The supporter check has been enabled!");
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

            if (JSONUtils.getInt(json, "error") != 200) {
                throw new Exception("Error while reading json: " + JSONUtils.getString(json, "message"));
            }

            return json;
        } finally {
            is.close();
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof PlayerEntity) {
            if (SupporterHandler.getPlayerDataUnsafe(e.getEntity().getUniqueID()) == null) {
                SupporterHandler.loadPlayerData(e.getEntity().getUniqueID());
            }

            if (CHECK && !SupporterHandler.getPlayerData(e.getEntity().getUniqueID()).hasModAccess() && e.getEntity() instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) e.getEntity()).connection.disconnect(new StringTextComponent("You are not allowed to use this mod!"));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggedOutEvent e) {
        if (e.getPlayer() != null) {
            DATA.remove(e.getPlayer().getUniqueID());
        }
    }

    public static class PlayerData {

        private final UUID uuid;
        private final List<Accessoire> accessoires;
        private final boolean modAccess;
        private ResourceLocation cloakTexture;

        public PlayerData(UUID uuid, JsonObject json) {
            this.uuid = uuid;
            this.accessoires = Lists.newArrayList();
            JsonArray data = JSONUtils.getJsonArray(json, "accessoires", new JsonArray());

            for (int i = 0; i < data.size(); i++) {
                ResourceLocation id = new ResourceLocation(data.get(i).getAsString());

                if (Accessoire.REGISTRY.containsKey(id)) {
                    this.accessoires.add(Accessoire.REGISTRY.getValue(id));
                }
            }

            this.modAccess = JSONUtils.getBoolean(json, "mod_access", false);

            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if (JSONUtils.hasField(json, "cloak")) {
                    loadCloakTexture(JSONUtils.getString(json, "cloak"));
                }
            });
        }

        @OnlyIn(Dist.CLIENT)
        public void loadCloakTexture(String url) {
            new Thread(() -> {
                try {
                    ResourceLocation resourceLocation = new ResourceLocation(ThreeCore.MODID, "cloaks/" + this.uuid.toString());
                    Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(NativeImage.read(new URL(url).openStream())));
                    this.cloakTexture = resourceLocation;
                } catch (IOException e) {
                    ThreeCore.LOGGER.error("Error loading supporter cloak texture: " + e.getMessage());
                }
            }).start();
        }

        public boolean hasModAccess() {
            return this.modAccess;
        }

        public boolean hasAccessoire(Accessoire accessoire) {
            return this.accessoires.contains(accessoire);
        }

        @Nullable
        public ResourceLocation getCloakTexture() {
            return this.cloakTexture;
        }
    }

}
