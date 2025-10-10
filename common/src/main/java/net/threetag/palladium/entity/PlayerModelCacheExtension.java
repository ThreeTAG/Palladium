package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;

@Environment(EnvType.CLIENT)
public interface PlayerModelCacheExtension {

    PlayerModel<AbstractClientPlayer> palladium$getCachedModel();

    PalladiumAnimation.PoseStackResult palladium$getBodyAnimationResult();

    void palladium$setBodyAnimationResult(PalladiumAnimation.PoseStackResult result);

}
