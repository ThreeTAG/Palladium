package net.threetag.palladium.client.renderer.entity.skin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.threetag.palladium.entity.PlayerModelChangeType;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkinHandler {

    private static final List<Pair<Integer, SkinProvider>> PROVIDER = new ArrayList<>();

    public static PlayerSkin getCurrentSkin(AbstractClientPlayer player, PlayerSkin original) {
        if (PROVIDER.isEmpty()) {
            return original;
        }
// TODO
//        ResourceLocation startSkin = original.body().texturePath();
//        var modelType = original.model();
//
//        for (Pair<Integer, SkinProvider> pair : PROVIDER) {
//            modelType = pair.getSecond().getModelType(player).toPlayerSkinModelType(original.model());
//        }
//
//        for (Pair<Integer, SkinProvider> pair : PROVIDER) {
//            startSkin = pair.getSecond().getSkin(player, startSkin, original.body().texturePath(), modelType);
//        }
//
//        if (original.model() == modelType && startSkin.equals(original.body().texturePath())) {
//            return original;
//        }
//
//        return new PlayerSkin(startSkin, original.textureUrl(), original.capeTexture(), original.elytraTexture(), modelType, original.secure());
        return original;
    }

    public static void registerSkinProvider(int priority, SkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface SkinProvider {

        ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin, PlayerModelType model);

        default PlayerModelChangeType getModelType(AbstractClientPlayer player) {
            return PlayerModelChangeType.KEEP;
        }

    }

    static {
        // Abilities
        registerSkinProvider(30, new AbilitySkinProvider());
    }

}
