package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.entity.PalladiumAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        var player = (Player) (Object) this;

        if(player.getAttributes().hasAttribute(PalladiumAttributes.DESTROY_SPEED.get())) {
            cir.setReturnValue((float) (cir.getReturnValue() * player.getAttributeValue(PalladiumAttributes.DESTROY_SPEED.get())));
        }
    }

}
