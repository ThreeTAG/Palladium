package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.geom.ModelPart;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelPart.class)
public class ModelPartMixin {

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At("HEAD"), ordinal = 3, argsOnly = true)
    private float injected(float alpha) {
        return alpha * HumanoidRendererModifications.ALPHA_MULTIPLIER;
    }

}
