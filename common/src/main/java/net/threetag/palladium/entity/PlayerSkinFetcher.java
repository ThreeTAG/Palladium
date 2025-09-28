package net.threetag.palladium.entity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class PlayerSkinFetcher {

    private static final Map<String, GameProfile> CACHED_NAME_PROFILES = new ConcurrentHashMap<>();
    private static final Map<String, SkinData> CACHED_NAME_SKINS = new ConcurrentHashMap<>();
    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Nullable
    public static ResourceLocation getOrLoadPlayerSkin(String username) {
        var skinData = getSkinData(username);

        if (skinData.hasFailed() || skinData.isLoading()) {
            return null;
        } else {
            return skinData.texture();
        }
    }

    @Nullable
    public static String getOrLoadModelType(String username) {
        var skinData = getSkinData(username);

        if (skinData.hasFailed() || skinData.isLoading()) {
            return null;
        } else {
            return skinData.skinModel();
        }
    }

    public static SkinData getSkinData(final @NotNull String username) {
        return CACHED_NAME_SKINS.computeIfAbsent(username.toLowerCase(Locale.ROOT), (key) -> {
            Util.backgroundExecutor().execute(() -> {
                var profile = getGameProfile(username);

                if (profile != null) {
                    Minecraft.getInstance().getSkinManager().registerSkins(profile, (type, resourceLocation, minecraftProfileTexture) -> {
                        if (type == MinecraftProfileTexture.Type.SKIN) {
                            var skinModel = minecraftProfileTexture.getMetadata("model");
                            if (skinModel == null) {
                                skinModel = "default";
                            }

                            CACHED_NAME_SKINS.computeIfAbsent(profile.getName().toLowerCase(Locale.ROOT), (n) -> new SkinData()).load(skinModel, resourceLocation);
                        }
                    }, true);
                } else {
                    CACHED_NAME_SKINS.computeIfAbsent(username.toLowerCase(Locale.ROOT), (n) -> new SkinData()).setFailed();
                }
            });

            return new SkinData();
        });
    }

    public static @Nullable GameProfile getGameProfile(final @NotNull String name) {
        return CACHED_NAME_PROFILES.computeIfAbsent(name, (key) -> {
            JsonObject object = parseURL("https://api.mojang.com/users/profiles/minecraft/" + key + "?unsigned=false");
            if (object == null) return null;
            UUID uuid = fromStringWithoutDashes(GsonHelper.getAsString(object, "id"));
            String realName = GsonHelper.getAsString(object, "name");
            return new GameProfile(uuid, realName);
        });
    }

    private static @Nullable JsonObject parseURL(final @NotNull String purl) {
        try {
            URI uri = new URI(purl);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IllegalStateException("Failed to do an HTTP request to: " + purl + " Response code: " + response.statusCode());
            }

            String responseBody = response.body();
            return JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (Exception e) {
            Palladium.LOGGER.warn("Failed to fetch URL: {}", purl);
        }
        return null;
    }

    private static UUID fromStringWithoutDashes(final @NotNull String uuid) {
        String correctedUUID = UUID_PATTERN.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
        return UUID.fromString(correctedUUID);
    }

    public static final class SkinData {

        private String skinModel;
        private ResourceLocation texture;
        private boolean failed;

        public SkinData() {
            this.skinModel = null;
            this.texture = null;
        }

        public boolean isLoading() {
            return this.texture == null;
        }

        public void load(String skinModel, ResourceLocation texture) {
            this.skinModel = skinModel;
            this.texture = texture;
        }

        public boolean hasFailed() {
            return this.failed;
        }

        public void setFailed() {
            this.failed = true;
        }

        public String skinModel() {
            return this.skinModel;
        }

        public ResourceLocation texture() {
            return this.texture;
        }

    }

}
