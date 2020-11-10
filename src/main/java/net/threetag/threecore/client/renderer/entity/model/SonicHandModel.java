package net.threetag.threecore.client.renderer.entity.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.function.Function;

public class SonicHandModel extends Model {

    public static final SonicHandModel INSTANCE = new SonicHandModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/sonic_hand.png");

    private final ModelRenderer root;

    public SonicHandModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);

        textureWidth = 32;
        textureHeight = 16;

        root = new ModelRenderer(this);
        root.setRotationPoint(0.0F, 0.0F, 0.0F);

        ModelRenderer cortex1 = new ModelRenderer(this);
        cortex1.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(cortex1);
        cortex1.setTextureOffset(0, 4).addBox(-0.25F, -0.75F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer cortex2 = new ModelRenderer(this);
        cortex2.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(cortex2);
        cortex2.setTextureOffset(0, 4).addBox(-0.75F, -0.75F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer cortex3 = new ModelRenderer(this);
        cortex3.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(cortex3);
        cortex3.setTextureOffset(0, 4).addBox(-0.75F, -0.25F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer cortex4 = new ModelRenderer(this);
        cortex4.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(cortex4);
        cortex4.setTextureOffset(0, 4).addBox(-0.25F, -0.25F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer connector = new ModelRenderer(this);
        connector.setRotationPoint(0.0F, 0.0F, -3.0F);
        root.addChild(connector);
        connector.setTextureOffset(8, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        ModelRenderer tip = new ModelRenderer(this);
        tip.setRotationPoint(0.0F, 0.0F, -3.25F);
        root.addChild(tip);
        tip.setTextureOffset(0, 1).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        ModelRenderer base = new ModelRenderer(this);
        base.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(base);
        base.setTextureOffset(0, 8).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 3.0F, 0.0F, true);

        ModelRenderer coneTop = new ModelRenderer(this);
        coneTop.setRotationPoint(0.0F, -2.0F, -0.2F);
        root.addChild(coneTop);
        setRotationAngle(coneTop, -0.3316F, 0.0F, 0.0F);
        coneTop.setTextureOffset(16, 12).addBox(-2.5F, -0.5F, -3.0F, 5.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer coneBottom = new ModelRenderer(this);
        coneBottom.setRotationPoint(0.0F, 2.0F, -0.2F);
        root.addChild(coneBottom);
        setRotationAngle(coneBottom, 0.3316F, 0.0F, 0.0F);
        coneBottom.setTextureOffset(16, 12).addBox(-2.5F, -0.5F, -3.0F, 5.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer coneRight = new ModelRenderer(this);
        coneRight.setRotationPoint(2.0F, 0.0F, -0.2F);
        root.addChild(coneRight);
        setRotationAngle(coneRight, 0.0F, -0.3316F, 0.0F);
        coneRight.setTextureOffset(16, 4).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 3.0F, 0.0F, false);

        ModelRenderer coneLeft = new ModelRenderer(this);
        coneLeft.setRotationPoint(-2.0F, 0.0F, -0.2F);
        root.addChild(coneLeft);
        setRotationAngle(coneLeft, 0.0F, 0.3316F, 0.0F);
        coneLeft.setTextureOffset(16, 4).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 3.0F, 0.0F, true);

        ModelRenderer coneCornerTopLeft = new ModelRenderer(this);
        coneCornerTopLeft.setRotationPoint(-2.0F, -2.0F, -0.2F);
        root.addChild(coneCornerTopLeft);
        setRotationAngle(coneCornerTopLeft, -0.3316F, 0.3316F, 0.0F);
        coneCornerTopLeft.setTextureOffset(8, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer coneCornerTopRight = new ModelRenderer(this);
        coneCornerTopRight.setRotationPoint(2.0F, -2.0F, -0.2F);
        root.addChild(coneCornerTopRight);
        setRotationAngle(coneCornerTopRight, -0.3316F, -0.3316F, 0.0F);
        coneCornerTopRight.setTextureOffset(8, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer coneCornerBottomLeft = new ModelRenderer(this);
        coneCornerBottomLeft.setRotationPoint(-2.0F, 2.0F, -0.2F);
        root.addChild(coneCornerBottomLeft);
        setRotationAngle(coneCornerBottomLeft, 0.3316F, 0.3316F, 0.0F);
        coneCornerBottomLeft.setTextureOffset(8, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        ModelRenderer coneCornerBottomRight = new ModelRenderer(this);
        coneCornerBottomRight.setRotationPoint(2.0F, 2.0F, -0.2F);
        root.addChild(coneCornerBottomRight);
        setRotationAngle(coneCornerBottomRight, 0.3316F, -0.3316F, 0.0F);
        coneCornerBottomRight.setTextureOffset(16, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}