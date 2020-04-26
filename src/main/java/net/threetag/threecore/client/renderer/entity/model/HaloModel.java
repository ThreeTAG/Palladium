package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.function.Function;

public class HaloModel extends Model {
    private static ResourceLocation wingTexture = new ResourceLocation(ThreeCore.MODID, "textures/models/wings.png");
    private ModelRenderer body;
    private ModelRenderer head;
    private ModelRenderer left_wing_1;
    private ModelRenderer right_wing_1;
    private ModelRenderer left_wing_2;
    private ModelRenderer left_wing_0;
    private ModelRenderer left_wing_3;
    private ModelRenderer left_wing_4;
    private ModelRenderer right_wing_2;
    private ModelRenderer right_wing_0;
    private ModelRenderer right_wing_3;
    private ModelRenderer right_wing_4;
    private ModelRenderer halo;
    private ModelRenderer halo_1;
    private ModelRenderer halo_2;
    private ModelRenderer halo_3;
    private ModelRenderer halo_4;
    private ModelRenderer halo_5;
    private ModelRenderer halo_6;
    private ModelRenderer halo_7;
    private ModelRenderer halo_8;
    private ModelRenderer halo_9;
    private ModelRenderer halo_10;
    private ModelRenderer halo_11;

    public HaloModel(Function<ResourceLocation, RenderType> p_i225947_1_) {
        super(p_i225947_1_);
        this.textureWidth = 81;
        this.textureHeight = 34;
        this.right_wing_2 = new ModelRenderer(this, 42, 0);
        this.right_wing_2.setRotationPoint(0.0F, 4.0F, -1.0F);
        this.right_wing_2.addBox(-1.0F, 0.0F, 0.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(right_wing_2, 1.2292353921796064F, 0.0F, 0.0F);
        this.halo_1 = new ModelRenderer(this, 0, 32);
        this.halo_1.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_1.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_1, 0.0F, -0.5235987755982988F, 0.0F);
        this.left_wing_3 = new ModelRenderer(this, 26, 0);
        this.left_wing_3.setRotationPoint(0.0F, 7.0F, 2.0F);
        this.left_wing_3.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(left_wing_3, -1.2292353921796064F, 0.0F, 0.0F);
        this.halo_3 = new ModelRenderer(this, 0, 32);
        this.halo_3.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_3.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_3, 0.0F, -0.5235987755982988F, 0.0F);
        this.right_wing_1 = new ModelRenderer(this, 8, 0);
        this.right_wing_1.setRotationPoint(-2.4F, 2.0F, 1.5F);
        this.right_wing_1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(right_wing_1, 1.53588974175501F, -0.9424777960769379F, 0.0F);
        this.left_wing_0 = new ModelRenderer(this, 6, 0);
        this.left_wing_0.setRotationPoint(2.4F, 2.0F, 1.5F);
        this.left_wing_0.addBox(-3.4F, -2.0F, -15.0F, 1, 11, 18, 0.0F);
        this.halo_11 = new ModelRenderer(this, 0, 32);
        this.halo_11.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_11.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_11, 0.0F, -0.5235987755982988F, 0.0F);
        this.right_wing_3 = new ModelRenderer(this, 50, 0);
        this.right_wing_3.setRotationPoint(0.0F, 7.0F, 2.0F);
        this.right_wing_3.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(right_wing_3, -1.2292353921796064F, 0.0F, 0.0F);
        this.halo = new ModelRenderer(this, 0, 32);
        this.halo.setRotationPoint(0.0F, -9.0F, -3.85F);
        this.halo.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo, 0.0F, -0.2617993877991494F, 0.0F);
        this.halo_10 = new ModelRenderer(this, 0, 32);
        this.halo_10.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_10.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_10, 0.0F, -0.5235987755982988F, 0.0F);
        this.left_wing_2 = new ModelRenderer(this, 16, 0);
        this.left_wing_2.setRotationPoint(0.0F, 4.0F, -1.0F);
        this.left_wing_2.addBox(-1.0F, 0.0F, 0.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(left_wing_2, 1.2292353921796064F, 0.0F, 0.0F);
        this.halo_5 = new ModelRenderer(this, 0, 32);
        this.halo_5.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_5.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_5, 0.0F, -0.5235987755982988F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.head = new ModelRenderer(this, 0, 34);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 0, 0, 0, 0.0F);
        this.halo_7 = new ModelRenderer(this, 0, 32);
        this.halo_7.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_7.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_7, 0.0F, -0.5235987755982988F, 0.0F);
        this.left_wing_1 = new ModelRenderer(this, 0, 0);
        this.left_wing_1.setRotationPoint(2.4F, 2.0F, 1.5F);
        this.left_wing_1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(left_wing_1, 1.53588974175501F, 0.9424777960769379F, 0.0F);
        this.right_wing_4 = new ModelRenderer(this, 64, 0);
        this.right_wing_4.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.right_wing_4.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(right_wing_4, -1.1383037381507017F, 0.0F, 0.0F);
        this.left_wing_4 = new ModelRenderer(this, 34, 0);
        this.left_wing_4.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.left_wing_4.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(left_wing_4, -1.1383037381507017F, 0.0F, 0.0F);
        this.halo_4 = new ModelRenderer(this, 0, 32);
        this.halo_4.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_4.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_4, 0.0F, -0.5235987755982988F, 0.0F);
        this.halo_6 = new ModelRenderer(this, 0, 32);
        this.halo_6.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_6.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_6, 0.0F, -0.5235987755982988F, 0.0F);
        this.halo_8 = new ModelRenderer(this, 0, 32);
        this.halo_8.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_8.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_8, 0.0F, -0.5235987755982988F, 0.0F);
        this.halo_9 = new ModelRenderer(this, 0, 32);
        this.halo_9.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_9.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_9, 0.0F, -0.5235987755982988F, 0.0F);
        this.right_wing_0 = new ModelRenderer(this, 44, 0);
        this.right_wing_0.setRotationPoint(-2.4F, 2.0F, 1.5F);
        this.right_wing_0.addBox(2.4F, -2.0F, -15.0F, 1, 11, 18, 0.0F);
        this.halo_2 = new ModelRenderer(this, 0, 32);
        this.halo_2.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.halo_2.addBox(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(halo_2, 0.0F, -0.5235987755982988F, 0.0F);
        this.right_wing_1.addChild(this.right_wing_2);
        this.halo.addChild(this.halo_1);
        this.left_wing_2.addChild(this.left_wing_3);
        this.halo_2.addChild(this.halo_3);
        this.body.addChild(this.right_wing_1);
        this.left_wing_1.addChild(this.left_wing_0);
        this.halo_10.addChild(this.halo_11);
        this.right_wing_2.addChild(this.right_wing_3);
        this.head.addChild(this.halo);
        this.halo_9.addChild(this.halo_10);
        this.left_wing_1.addChild(this.left_wing_2);
        this.halo_4.addChild(this.halo_5);
        this.halo_6.addChild(this.halo_7);
        this.body.addChild(this.left_wing_1);
        this.right_wing_3.addChild(this.right_wing_4);
        this.left_wing_3.addChild(this.left_wing_4);
        this.halo_3.addChild(this.halo_4);
        this.halo_5.addChild(this.halo_6);
        this.halo_7.addChild(this.halo_8);
        this.halo_8.addChild(this.halo_9);
        this.right_wing_1.addChild(this.right_wing_0);
        this.halo_1.addChild(this.halo_2);
    }


    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
        Minecraft.getInstance().getTextureManager().bindTexture(wingTexture);
        head.render(matrixStack, iVertexBuilder, i, i1, v, v1, v2, v3);
    }
}