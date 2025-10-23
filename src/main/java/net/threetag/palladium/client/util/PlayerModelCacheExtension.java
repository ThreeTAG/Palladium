package net.threetag.palladium.client.util;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public interface PlayerModelCacheExtension {

    PlayerModel palladium$getCachedModel();

    default void palladium$animateInFirstPerson(LocalPlayer player, AvatarRenderer<?> avatarRenderer) {
//        var renderState = playerRenderer.createRenderState(player, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        this.palladium$getCachedModel().resetPose();
//        this.palladium$getCachedModel().setupAnim(renderState);
        // TODO arm zRot keeps increasing without limit -> spins around player only in first person
    }

}
