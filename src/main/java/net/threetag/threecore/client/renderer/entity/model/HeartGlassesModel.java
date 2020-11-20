package net.threetag.threecore.client.renderer.entity.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.function.Function;

public class HeartGlassesModel extends Model {

    public static final HeartGlassesModel INSTANCE = new HeartGlassesModel(RenderType::getEntityTranslucentCull);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/heart_glasses.png");

    private final ModelRenderer hat;

    public HeartGlassesModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 32;
        textureHeight = 16;

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, 24.0F, 0.0F);
        hat.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 5.0F, 0.0F, 0.0F, false);
        hat.setTextureOffset(0, 0).addBox(5.0F, -3.0F, -5.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        hat.setTextureOffset(0, 1).addBox(-5.0F, -3.0F, -5.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}