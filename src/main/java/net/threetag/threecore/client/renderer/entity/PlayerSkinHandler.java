package net.threetag.threecore.client.renderer.entity;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.SkinChangeAbility;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerSkinHandler {

    private static List<Pair<Integer, ISkinProvider>> PROVIDER = Lists.newLinkedList();

    public static ResourceLocation getCurrentSkin(GameProfile gameProfile, ResourceLocation defaultSkin) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) Minecraft.getInstance().world.getPlayerByUuid(gameProfile.getId());

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

        ResourceLocation getSkin(AbstractClientPlayerEntity player, ResourceLocation previousSkin, ResourceLocation defaultSkin);

    }

    static {
        // Abilities
        registerSkinProvider(30, (player, previousSkin, defaultSkin) -> {
            List<SkinChangeAbility> abilities = AbilityHelper.getAbilitiesFromClass(player, SkinChangeAbility.class).stream().filter(a -> a.getConditionManager().isEnabled()).sorted((a1, a2) -> a2.get(SkinChangeAbility.PRIORITY) - a1.get(SkinChangeAbility.PRIORITY)).collect(Collectors.toList());

            if (abilities.size() > 0) {
                return abilities.get(0).get(SkinChangeAbility.TEXTURE);
            }

            return previousSkin;
        });
    }

}
