package net.threetag.palladium.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.PlayerSkinChangeAbility;
import net.threetag.palladium.power.ability.SkinChangeAbility;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerSkinHandler {

    public static final List<Pair<Integer, ISkinProvider>> PROVIDER = new ArrayList<>();

    public static ResourceLocation getCurrentSkin(GameProfile gameProfile, ResourceLocation defaultSkin) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player instanceof PlayerSkinChangeHandler handler) {
            var skin = handler.palladium$getOverridenSkin();
            return skin != null ? skin.getTexture() : defaultSkin;
        }

        return defaultSkin;
    }

    public static String getCurrentModelType(GameProfile gameProfile, String modelType) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player instanceof PlayerSkinChangeHandler handler) {
            var skin = handler.palladium$getOverridenSkin();
            return skin != null ? skin.getModelName() : modelType;
        }

        return modelType;
    }

    public static void registerSkinProvider(int priority, ISkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface ISkinProvider {

        PlayerSkinInfo getSkin(AbstractClientPlayer player, PlayerSkinInfo previousSkin, PlayerSkinInfo defaultSkin);

    }

    static {
        // Abilities
        registerSkinProvider(30, new SkinChangeAbility.SkinProvider());
        registerSkinProvider(40, new PlayerSkinChangeAbility.SkinProvider());
    }

    public interface PlayerSkinChangeHandler {

        @Nullable
        PlayerSkinInfo palladium$getOverridenSkin();

    }

}
