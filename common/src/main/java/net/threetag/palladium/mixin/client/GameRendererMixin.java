package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.ShaderEffectAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private PostChain postEffect;

    @Shadow
    public abstract void loadEffect(ResourceLocation resourceLocation);

    @Shadow
    public abstract Minecraft getMinecraft();

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(PoseStack matrixStack, float partialTicks, CallbackInfo ci) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity livingEntity && !AbilityUtil.getEnabledEntries(livingEntity, Abilities.ENERGY_BLAST.get()).isEmpty()) {
            ci.cancel();
        }
    }

    @Inject(method = "checkEntityPostEffect", at = @At("RETURN"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (this.postEffect == null && this.minecraft.player != null) {
            var shader = ShaderEffectAbility.get(this.minecraft.player);
            if (shader != null) {
                this.loadEffect(shader);
            }
        }
    }

}
