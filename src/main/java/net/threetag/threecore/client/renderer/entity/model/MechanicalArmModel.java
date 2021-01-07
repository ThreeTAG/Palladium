package net.threetag.threecore.client.renderer.entity.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class MechanicalArmModel extends Model {

    public static final MechanicalArmModel INSTANCE = new MechanicalArmModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/mechanical_arm.png");
    public static final ResourceLocation TEXTURE_SLIM = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/mechanical_slim_arm.png");

    private final ModelRenderer normal;
    private final ModelRenderer slim;

    public MechanicalArmModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 32;
        textureHeight = 32;

        normal = new ModelRenderer(this);
        normal.setRotationPoint(0.0F, 16.0F, -2.0F);
        normal.setTextureOffset(0, 0).addBox(-2.0F, 5.0F, 0.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        normal.setTextureOffset(0, 13).addBox(-0.5F, -2.0F, 1.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        normal.setTextureOffset(12, 0).addBox(-1.0F, 1.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        normal.setTextureOffset(0, 7).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        normal.setTextureOffset(12, 12).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        slim = new ModelRenderer(this);
        slim.setRotationPoint(0.0F, 16.0F, -2.0F);
        slim.setTextureOffset(0, 0).addBox(-2.0F, 5.0F, 0.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        slim.setTextureOffset(0, 13).addBox(-1.0F, -2.0F, 1.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        slim.setTextureOffset(12, 0).addBox(-1.5F, 1.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        slim.setTextureOffset(0, 7).addBox(-2.0F, -4.0F, 0.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        slim.setTextureOffset(12, 12).addBox(-2.0F, -2.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        normal.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        slim.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderArm(boolean slimArms, MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (slimArms) {
            slim.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            normal.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}