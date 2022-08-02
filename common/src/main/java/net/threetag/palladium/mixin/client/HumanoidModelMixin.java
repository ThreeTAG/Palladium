package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.HumanoidAnimationsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow
    protected abstract Iterable<ModelPart> headParts();

    @Shadow
    protected abstract Iterable<ModelPart> bodyParts();

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("RETURN"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity == null)
            return;

        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        HumanoidAnimationsManager.pre(model, this.headParts(), this.bodyParts(), entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

}
