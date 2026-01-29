package net.threetag.palladium.mixin.client;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.logic.molang.EntityContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerAnimationController.class)
public abstract class PlayerAnimationControllerMixin implements EntityContext {

    @Shadow
    public abstract Avatar getAvatar();

    @Override
    public Entity entity() {
        return this.getAvatar();
    }

    @Override
    public @Nullable Avatar player() {
        return this.getAvatar();
    }

    @Override
    public float partialTick() {
        var controller = (PlayerAnimationController) (Object) this;
        return controller.getAnimationData().getPartialTick();
    }
}
