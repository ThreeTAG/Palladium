package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.PalladiumAnimationRegistry;
import net.threetag.palladium.entity.BodyPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow
    protected abstract Iterable<ModelPart> headParts();

    @Shadow
    protected abstract Iterable<ModelPart> bodyParts();

    @Inject(at = @At("RETURN"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        if(!PalladiumAnimationRegistry.SKIP_ANIMATIONS) {
            PalladiumAnimationRegistry.applyAnimations(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }

        if (!(model instanceof PlayerModel<?>)) {
            BodyPart.hideParts(model, entity);
        }
    }

    @Inject(at = @At("HEAD"), method = "prepareMobModel(Lnet/minecraft/world/entity/LivingEntity;FFF)V")
    public void prepareMobModel(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        PalladiumAnimationRegistry.PARTIAL_TICK = partialTick;

        // Reset visibility
        BodyPart.resetBodyParts(entity, model);

        // Reset poses
        PalladiumAnimationRegistry.resetPoses(this.headParts(), this.bodyParts());
    }

}
