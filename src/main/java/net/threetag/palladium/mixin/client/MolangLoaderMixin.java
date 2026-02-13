package net.threetag.palladium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.molang.MolangLoader;
import net.threetag.palladium.logic.molang.PlayerAnimationLibCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.unnamed.mocha.MochaEngine;

@Mixin(MolangLoader.class)
public class MolangLoaderMixin {

    @Inject(
            method = "createNewEngine(Lcom/zigythebird/playeranimcore/animation/AnimationController;)Lteam/unnamed/mocha/MochaEngine;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/zigythebird/playeranimcore/event/MolangEvent$MolangEventInterface;registerMolangQueries(Lcom/zigythebird/playeranimcore/animation/AnimationController;Lteam/unnamed/mocha/MochaEngine;Lcom/zigythebird/playeranimcore/molang/QueryBinding;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void createNewEngine(AnimationController controller, CallbackInfoReturnable<MochaEngine<AnimationController>> cir, @Local(name = "engine") MochaEngine<AnimationController> mochaEngine) {
        PlayerAnimationLibCompat.setup(mochaEngine, controller);
    }

    // Shutting up those stupid warnings if you use basic math functions
    @ModifyArg(
            method = "createBaseEngine",
            at = @At(value = "INVOKE", target = "Lteam/unnamed/mocha/MochaEngine;warnOnReflectiveFunctionUsage(Z)Lteam/unnamed/mocha/MochaEngine;"),
            index = 0,
            remap = false
    )
    private static boolean injected(boolean warnOnReflectiveFunctionUsage) {
        return false;
    }

}
