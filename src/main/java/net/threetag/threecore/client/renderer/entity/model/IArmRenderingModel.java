package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.HandSide;

public interface IArmRenderingModel {

    void renderArm(HandSide handSide, MatrixStack matrixStack, IVertexBuilder vertexBuilder, int combinedLight);

}
