package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.MouseClickKeyBind;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public Screen screen;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void startAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player == null) {
            return;
        }

        for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(this.player)) {
            if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                    if (this.screen == null && mouseClick.clickType == MouseClickKeyBind.ClickType.LEFT_CLICK) {
                        handler.onKeyPressed(this.player, ability);

                        if (mouseClick.cancelInteraction) {
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void startUseItem(CallbackInfo ci) {
        if (this.player == null) {
            return;
        }

        for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(this.player)) {
            if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                    if (this.screen == null && mouseClick.clickType == MouseClickKeyBind.ClickType.RIGHT_CLICK) {
                        handler.onKeyPressed(this.player, ability);

                        if (mouseClick.cancelInteraction) {
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "pickBlock", at = @At("HEAD"), cancellable = true)
    private void pickBlock(CallbackInfo ci) {
        if (this.player == null) {
            return;
        }

        for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(this.player)) {
            if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                    if (this.screen == null && mouseClick.clickType == MouseClickKeyBind.ClickType.MIDDLE_CLICK) {
                        handler.onKeyPressed(this.player, ability);

                        if (mouseClick.cancelInteraction) {
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }

}
