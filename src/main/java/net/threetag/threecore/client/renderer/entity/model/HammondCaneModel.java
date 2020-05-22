package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.function.Function;

public class HammondCaneModel extends Model {

    public static final HammondCaneModel INSTANCE = new HammondCaneModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/hammond_cane.png");

    private final ModelRenderer cane;

    public HammondCaneModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 16;
        textureHeight = 16;

        cane = new ModelRenderer(this);
        cane.setRotationPoint(0.0F, 24.0F, 0.0F);
        cane.setTextureOffset(0, 0).addBox(-0.5F, -14.0F, -0.5F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        cane.setTextureOffset(4, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        cane.setTextureOffset(4, 5).addBox(-1.0F, -17.0F, 0.0F, 2.0F, 3.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        cane.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
