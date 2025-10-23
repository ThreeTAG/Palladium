package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.JumpKeyBind;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends ClientInput {

    @Shadow
    @Final
    private Options options;

    @Unique
    private boolean palladium$lastJumpKeyDown = false;

    @Unique
    private boolean palladium$cancelJump = false;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickHead(CallbackInfo ci) {
        if (this.palladium$lastJumpKeyDown != this.options.keyJump.isDown()) {
            boolean pressed = this.options.keyJump.isDown();
            this.palladium$lastJumpKeyDown = pressed;
            var client = Minecraft.getInstance();

            if (!pressed) {
                this.palladium$cancelJump = false;
            }

            for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(client.player)) {
                if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                    if (handler.getKeyBindType() instanceof JumpKeyBind jump) {
                        if (pressed) {
                            if (client.screen == null) {
                                handler.onKeyPressed(client.player, ability);
                            }
                        } else {
                            handler.onKeyReleased(client.player, ability);
                        }

                        if (pressed && jump.cancelJump && client.screen == null) {
                            this.palladium$cancelJump = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickReturn(CallbackInfo ci) {
        if (this.palladium$cancelJump) {
            this.keyPresses = new Input(
                    this.options.keyUp.isDown(),
                    this.options.keyDown.isDown(),
                    this.options.keyLeft.isDown(),
                    this.options.keyRight.isDown(),
                    false,
                    this.options.keyShift.isDown(),
                    this.options.keySprint.isDown()
            );
        }
    }

}
