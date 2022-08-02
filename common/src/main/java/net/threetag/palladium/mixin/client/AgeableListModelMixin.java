package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.threetag.palladium.client.model.animation.HumanoidAnimationsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableListModel.class)
public abstract class AgeableListModelMixin {

    @Shadow
    public abstract Iterable<ModelPart> headParts();

    @Shadow
    public abstract Iterable<ModelPart> bodyParts();

    @SuppressWarnings({"rawtypes", "ConstantConditions"})
    @Inject(at = @At("TAIL"), method = "renderToBuffer")
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        AgeableListModel model = (AgeableListModel) (Object) this;

        if (model instanceof HumanoidModel<?> humanoidModel) {
            HumanoidAnimationsManager.post(this.headParts(), this.bodyParts());
        }
    }

}
