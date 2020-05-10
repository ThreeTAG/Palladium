package net.threetag.threecore.client.renderer.entity.model;

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
public class WoodenLegModel extends Model {

    public static final WoodenLegModel INSTANCE = new WoodenLegModel(RenderType::getEntityCutoutNoCull);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/wooden_leg.png");

    private final ModelRenderer leg;
    private final ModelRenderer playerLeg;

    public WoodenLegModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        this.textureHeight = 16;
        this.textureWidth = 16;

        leg = new ModelRenderer(this);
        leg.setRotationPoint(0.0F, 16.0F, 0.0F);
        leg.addBox("base", -2F, 0F, -2F, 4, 2, 4, 0, 0, 0);
        leg.addBox("connection", -1F, 2F, -1F, 2, 1, 2, 0, 0, 6);
        leg.addBox("stick", -0.5F, 3F, -0.5F, 1, 5, 1, 0, 0, 9);

        this.textureHeight = 64;
        this.textureWidth = 64;
        playerLeg = new ModelRenderer(this);
        playerLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        playerLeg.addBox("leg", -2, 12, -2, 4, 4, 4, 0, 16, 48);
        playerLeg.addBox("leg_overlay", -2, 12, -2, 4, 4, 4, 0.5F, 0, 48);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        leg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderPlayerLeg(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        playerLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
