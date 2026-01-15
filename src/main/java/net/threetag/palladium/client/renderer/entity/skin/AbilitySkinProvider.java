package net.threetag.palladium.client.renderer.entity.skin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.threetag.palladium.entity.PlayerModelChangeType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;

public class AbilitySkinProvider implements PlayerSkinHandler.SkinProvider {

    @Override
    public Identifier getSkin(AbstractClientPlayer player, Identifier previousSkin, Identifier defaultSkin, PlayerModelType model) {
        var abilities = AbilityUtil.getEnabledInstances(player, AbilitySerializers.SKIN_CHANGE.get()).stream().sorted((a1, a2) -> a2.getAbility().priority - a1.getAbility().priority).toList();

        if (!abilities.isEmpty()) {
            var ability = abilities.getFirst();
            return ability.getAbility().texture.get(model == PlayerModelType.SLIM).getTexture(DataContext.forAbility(player, ability));
        }

        return previousSkin;
    }

    @Override
    public PlayerModelChangeType getModelType(AbstractClientPlayer player) {
        var abilities = AbilityUtil.getEnabledInstances(player, AbilitySerializers.SKIN_CHANGE.get()).stream().sorted((a1, a2) -> a2.getAbility().priority - a1.getAbility().priority).toList();

        if (!abilities.isEmpty()) {
            var ability = abilities.getFirst();
            return ability.getAbility().modelChangeType;
        }

        return PlayerSkinHandler.SkinProvider.super.getModelType(player);
    }

}
