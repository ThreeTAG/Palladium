package net.threetag.threecore.client.renderer.entity.model;// Made with Blockbench 3.5.2
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

public class GuzzlerHelmetModel extends Model {

    public static final GuzzlerHelmetModel INSTANCE = new GuzzlerHelmetModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/guzzler_helmet.png");

    private final ModelRenderer root;

    public GuzzlerHelmetModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 32;
        textureHeight = 32;

        root = new ModelRenderer(this);
        root.setRotationPoint(0.0F, 25.0F, 0.0F);

        ModelRenderer base1 = new ModelRenderer(this);
        base1.setRotationPoint(6.0F, -7.0F, 0.0F);
        root.addChild(base1);
        base1.setTextureOffset(8, 25).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        ModelRenderer top1 = new ModelRenderer(this);
        top1.setRotationPoint(6.0F, -8.25F, 0.0F);
        root.addChild(top1);
        top1.setTextureOffset(22, 18).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        ModelRenderer base2 = new ModelRenderer(this);
        base2.setRotationPoint(-6.0F, -7.0F, 0.0F);
        root.addChild(base2);
        base2.setTextureOffset(0, 25).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        ModelRenderer top2 = new ModelRenderer(this);
        top2.setRotationPoint(-6.0F, -8.25F, 0.0F);
        root.addChild(top2);
        top2.setTextureOffset(22, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer holders = new ModelRenderer(this);
        holders.setRotationPoint(-5.8F, -7.0F, 0.2F);
        root.addChild(holders);
        holders.setTextureOffset(0, 21).addBox(10.3F, -0.5F, -1.7F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        holders.setTextureOffset(12, 21).addBox(-1.7F, -0.5F, -1.7F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer connector = new ModelRenderer(this);
        connector.setRotationPoint(0.0F, -9.25F, 0.0F);
        root.addChild(connector);
        connector.setTextureOffset(0, 16).addBox(-5.0F, -0.75F, -0.5F, 10.0F, 1.0F, 1.0F, 0.0F, true);
        connector.setTextureOffset(0, 18).addBox(4.0F, 0.25F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        connector.setTextureOffset(4, 18).addBox(-5.0F, 0.25F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        ModelRenderer head = new ModelRenderer(this);
        head.setRotationPoint(4.0F, -1.0F, -4.0F);
        root.addChild(head);
        head.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, 0.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}