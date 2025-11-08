package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.screen.AbilityWheelRenderer;
import net.threetag.palladium.entity.DualWieldingPlayerHandler;
import net.threetag.palladium.network.AbilityKeyPressedMessage;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.EntityGlowAbility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    public LocalPlayer player;

    @Shadow
    public Entity cameraEntity;

    @Shadow
    @Final
    public Options options;

    @Shadow
    public MultiPlayerGameMode gameMode;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void startAttackAbilityWheel(CallbackInfoReturnable<Boolean> cir) {
        if (AbilityWheelRenderer.CURRENT_WHEEL != null) {
            var ability = AbilityWheelRenderer.CURRENT_WHEEL.getSelectedAbility();
            if (ability != null) {
                if (!ability.isOnCooldown()) {
                    new AbilityKeyPressedMessage(ability.getReference(), true).send();
                    PalladiumKeyMappings.LEFT_CLICKED_ABILITY = ability;
                    AbilityWheelRenderer.setWheel(null);
                }
            }
            cir.setReturnValue(false);
            this.options.keyAttack.consumeClick();
            this.options.keyAttack.setDown(false);
        }
    }

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasMissTime()Z"), cancellable = true)
    private void startAttackStartAbility(CallbackInfoReturnable<Boolean> cir) {
        if (PalladiumKeyMappings.LEFT_CLICKED_ABILITY == null && AbilityWheelRenderer.CURRENT_WHEEL == null) {
            var entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConfiguration.KeyType.LEFT_CLICK);

            if (entry != null && entry.isUnlocked() && (!entry.getConfiguration().needsEmptyHand() || this.player.getMainHandItem().isEmpty())) {
                var pressType = entry.getConfiguration().getKeyPressType();

                if (pressType == AbilityConfiguration.KeyPressType.ACTION) {
                    if (!entry.isOnCooldown()) {
                        new AbilityKeyPressedMessage(entry.getReference(), true).send();
                        PalladiumKeyMappings.LEFT_CLICKED_ABILITY = entry;
                        cir.setReturnValue(false);
                    }
                } else if (pressType == AbilityConfiguration.KeyPressType.ACTIVATION) {
                    if (!entry.isOnCooldown() && !entry.isEnabled()) {
                        new AbilityKeyPressedMessage(entry.getReference(), true).send();
                        PalladiumKeyMappings.LEFT_CLICKED_ABILITY = entry;
                        cir.setReturnValue(false);
                    }
                } else {
                    new AbilityKeyPressedMessage(entry.getReference(), true).send();
                    PalladiumKeyMappings.LEFT_CLICKED_ABILITY = entry;
                    cir.setReturnValue(false);
                }
            }
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void startAttackStopAbility(CallbackInfoReturnable<Boolean> cir) {
        if (PalladiumKeyMappings.LEFT_CLICKED_ABILITY != null) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startUseItem", at = @At("TAIL"), cancellable = true)
    private void startUseItemStartAbility(CallbackInfo ci) {
        if (PalladiumKeyMappings.RIGHT_CLICKED_ABILITY == null) {
            var entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConfiguration.KeyType.RIGHT_CLICK);

            if (entry != null && entry.isUnlocked() && (!entry.getConfiguration().needsEmptyHand() || this.player.getMainHandItem().isEmpty())) {
                var pressType = entry.getConfiguration().getKeyPressType();

                if (pressType == AbilityConfiguration.KeyPressType.ACTION) {
                    if (!entry.isOnCooldown()) {
                        new AbilityKeyPressedMessage(entry.getReference(), true).send();
                        PalladiumKeyMappings.RIGHT_CLICKED_ABILITY = entry;
                        ci.cancel();
                        return;
                    }
                } else if (pressType == AbilityConfiguration.KeyPressType.ACTIVATION) {
                    if (!entry.isOnCooldown() && !entry.isEnabled()) {
                        new AbilityKeyPressedMessage(entry.getReference(), true).send();
                        PalladiumKeyMappings.RIGHT_CLICKED_ABILITY = entry;
                        ci.cancel();
                        return;
                    }
                } else {
                    new AbilityKeyPressedMessage(entry.getReference(), true).send();
                    PalladiumKeyMappings.RIGHT_CLICKED_ABILITY = entry;
                    ci.cancel();
                    return;
                }
            }
        } else {
            ci.cancel();
            return;
        }

        DualWieldingPlayerHandler.attackClient();
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void startUseItemStopAbility(CallbackInfo ci) {
        if (PalladiumKeyMappings.RIGHT_CLICKED_ABILITY != null) {
            ci.cancel();
        }
    }

    @Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
    private void showGlow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (EntityGlowAbility.shouldGlow(entity, this.cameraEntity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void continueAttack(boolean leftClick, CallbackInfo ci) {
        if (leftClick) {
            if (AbilityWheelRenderer.CURRENT_WHEEL != null) {
                var ability = AbilityWheelRenderer.CURRENT_WHEEL.getSelectedAbility();
                if (ability != null) {
                    if (!ability.isOnCooldown()) {
                        new AbilityKeyPressedMessage(ability.getReference(), true).send();
                        PalladiumKeyMappings.LEFT_CLICKED_ABILITY = ability;
                        AbilityWheelRenderer.setWheel(null);
                    }
                }
                ci.cancel();
                this.options.keyAttack.consumeClick();
                this.options.keyAttack.setDown(false);
                this.gameMode.stopDestroyBlock();
            }
        }
    }

}
