package net.threetag.palladium.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.*;

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

    public static void registerSkinProvider(int priority, ISkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface ISkinProvider {

        ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin);

    }

    static {
        // Abilities
        registerSkinProvider(30, (player, previousSkin, defaultSkin) -> {
            var abilities = AbilityUtil.getEnabledEntries(player, Abilities.SKIN_CHANGE.get()).stream().filter(AbilityEntry::isEnabled).sorted((a1, a2) -> a2.getProperty(SkinChangeAbility.PRIORITY) - a1.getProperty(SkinChangeAbility.PRIORITY)).toList();

            if (abilities.size() > 0) {
                return abilities.get(0).getProperty(SkinChangeAbility.TEXTURE);
            }

            return previousSkin;
        });
    }

}
