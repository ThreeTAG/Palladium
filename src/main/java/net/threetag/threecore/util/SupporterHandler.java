package net.threetag.threecore.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.threetag.threecore.ThreeCore;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class SupporterHandler {

    private static final String SUPPORTER_FILE = "https://drive.google.com/uc?export=download&id=19iN_yzXWHhn5SfM5GJ-Q1ST5bOtFe8fE";
    private static Map<UUID, SupporterData> REGISTRY = Maps.newHashMap();
    private static boolean CHECK = false;

    public static void load() {
        try {
            JsonObject json = readJsonFromUrl(SUPPORTER_FILE);

            JsonArray array = JSONUtils.getJsonArray(json, "supporters");

            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                UUID uuid = UUID.fromString(JSONUtils.getString(obj, "uuid"));
                String name = JSONUtils.getString(obj, "name");
                CompoundNBT nbt = JsonToNBT.getTagFromJson(obj.toString());
                SupporterData data = new SupporterData(uuid, name, JSONUtils.getBoolean(obj, "access", false), nbt);
                REGISTRY.put(uuid, data);
            }

            ThreeCore.LOGGER.info("Successfully read supporter information file!");
        } catch (Exception e) {
            ThreeCore.LOGGER.error("Was not able to read supporter information file!");
            e.printStackTrace();
            addDefaultData();
            ThreeCore.LOGGER.error("Loaded default supporter data!");
        }
    }

    private static void addDefaultData() {
        REGISTRY.put(UUID.fromString("0669d99d-b34d-40fc-a4d8-c7ee963cc842"), new SupporterData(UUID.fromString("0669d99d-b34d-40fc-a4d8-c7ee963cc842"), "TheLucraft", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("70e36bc3-f6d5-406b-924c-46d5c5f52101"), new SupporterData(UUID.fromString("70e36bc3-f6d5-406b-924c-46d5c5f52101"), "Neon", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("3fa3dc7d-3de2-4ba1-a0ca-adc57bf0827d"), new SupporterData(UUID.fromString("3fa3dc7d-3de2-4ba1-a0ca-adc57bf0827d"), "Sheriff", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("fa396f29-9e23-479b-93a5-43e0780f1453"), new SupporterData(UUID.fromString("fa396f29-9e23-479b-93a5-43e0780f1453"), "Nictogen", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("7400ab2f-0980-453a-a945-0bafe6cba8cc"), new SupporterData(UUID.fromString("7400ab2f-0980-453a-a945-0bafe6cba8cc"), "Spyeedy", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("13b07ab0-663e-456d-98fa-debdb8a3777b"), new SupporterData(UUID.fromString("13b07ab0-663e-456d-98fa-debdb8a3777b"), "HydroSimp", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("bc8b891e-5c25-4c9f-ae61-cdfb270f1cc1"), new SupporterData(UUID.fromString("bc8b891e-5c25-4c9f-ae61-cdfb270f1cc1"), "Suffril", true, new CompoundNBT()));
        REGISTRY.put(UUID.fromString("ab572785-66d7-4f5f-b9d4-2a3a68fb9d1a"), new SupporterData(UUID.fromString("ab572785-66d7-4f5f-b9d4-2a3a68fb9d1a"), "Honeyluck", true, new CompoundNBT()));
    }

    public static void enableSupporterCheck() {
        if (!CHECK) {
            CHECK = true;
            ThreeCore.LOGGER.info("The supporter check has been enabled!");
        }
    }

    public static SupporterData getSupporterData(PlayerEntity player) {
        if (REGISTRY.containsKey(player.getGameProfile().getId())) {
            return REGISTRY.get(player.getGameProfile().getId());
        }
        return null;
    }

    public static SupporterData getSupporterData(UUID uuid) {
        return REGISTRY.get(uuid);
    }

    public static List<UUID> getUUIDs() {
        return ImmutableList.copyOf(REGISTRY.keySet());
    }

    public static JsonObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            JsonObject json = (new JsonParser()).parse(rd).getAsJsonObject();
            return json;
        } finally {
            is.close();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onWorldJoin(EntityJoinWorldEvent e) {
        if (CHECK && e.getEntity() == net.minecraft.client.Minecraft.getInstance().player && (getSupporterData(net.minecraft.client.Minecraft.getInstance().player) == null || !getSupporterData(net.minecraft.client.Minecraft.getInstance().player).modAccess)) {
            throw new RuntimeException("You are not allowed to play this version of the mod!");
        }
    }

    public static class SupporterData {

        protected UUID owner;
        protected String name;
        protected boolean modAccess;
        protected CompoundNBT nbt;

        public SupporterData(UUID owner, String name, boolean modAccess, CompoundNBT nbt) {
            this.owner = owner;
            this.name = name;
            this.modAccess = modAccess;
            this.nbt = nbt;
        }

        public UUID getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public boolean hasModAccess() {
            return modAccess;
        }

        public CompoundNBT getNbt() {
            return nbt;
        }
    }


}
