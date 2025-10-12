package net.threetag.palladium.client.renderer.entity.skin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.entity.PlayerModelChangeType;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkinHandler {

    private static final List<Pair<Integer, SkinProvider>> PROVIDER = new ArrayList<>();

    public static PlayerSkin getCurrentSkin(AbstractClientPlayer player, PlayerSkin original) {
        if (PROVIDER.isEmpty()) {
            return original;
        }

        ResourceLocation startSkin = original.texture();
        var modelType = original.model();

        for (Pair<Integer, SkinProvider> pair : PROVIDER) {
            modelType = pair.getSecond().getModelType(player).toPlayerSkinModelType(modelType);
        }

        for (Pair<Integer, SkinProvider> pair : PROVIDER) {
            startSkin = pair.getSecond().getSkin(player, startSkin, original.texture(), modelType);
        }

        if (original.model() == modelType && startSkin.equals(original.texture())) {
            return original;
        }

        return new PlayerSkin(startSkin, original.textureUrl(), original.capeTexture(), original.elytraTexture(), modelType, original.secure());
    }

    public static void registerSkinProvider(int priority, SkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface SkinProvider {

        ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin, PlayerSkin.Model model);

        default PlayerModelChangeType getModelType(AbstractClientPlayer player) {
            return PlayerModelChangeType.KEEP;
        }

    }

    static {
        // Abilities
        registerSkinProvider(30, new AbilitySkinProvider());
    }

}
