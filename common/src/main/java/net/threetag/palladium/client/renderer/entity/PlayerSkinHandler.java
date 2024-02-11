package net.threetag.palladium.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.SkinChangeAbility;
import net.threetag.palladium.util.property.ChangedPlayerModelTypeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerSkinHandler {

    private static final List<Pair<Integer, ISkinProvider>> PROVIDER = new ArrayList<>();

    public static ResourceLocation getCurrentSkin(GameProfile gameProfile, ResourceLocation defaultSkin) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player != null) {
            ResourceLocation start = defaultSkin;

            for (Pair<Integer, ISkinProvider> pair : PROVIDER) {
                start = pair.getSecond().getSkin(player, start, defaultSkin);
            }

            return start;
        }

        return defaultSkin;
    }

    public static String getCurrentModelType(GameProfile gameProfile, String modelType) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player != null && !PROVIDER.isEmpty()) {
            var overriddenType = PROVIDER.get(PROVIDER.size() - 1).getSecond().getModelType(player);

            if (overriddenType == ChangedPlayerModelTypeProperty.ChangedModelType.KEEP) {
                return modelType;
            } else if (overriddenType == ChangedPlayerModelTypeProperty.ChangedModelType.NORMAL) {
                return "default";
            } else {
                return "slim";
            }
        }

        return modelType;
    }

    public static void registerSkinProvider(int priority, ISkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface ISkinProvider {

        ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin);

        default ChangedPlayerModelTypeProperty.ChangedModelType getModelType(AbstractClientPlayer player) {
            return ChangedPlayerModelTypeProperty.ChangedModelType.KEEP;
        }

    }

    static {
        // Abilities
        registerSkinProvider(30, new SkinChangeAbility.SkinProvider());
    }

}
