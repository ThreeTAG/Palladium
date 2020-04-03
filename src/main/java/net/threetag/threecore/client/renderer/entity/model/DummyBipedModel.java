package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DummyBipedModel<T extends LivingEntity> extends BipedModel<T> {

    public static final DummyBipedModel INSTANCE = new DummyBipedModel(0F);

    public DummyBipedModel(float p_i1148_1_) {
        super(p_i1148_1_);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}