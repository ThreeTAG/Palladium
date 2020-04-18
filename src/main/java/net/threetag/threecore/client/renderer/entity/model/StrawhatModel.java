package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class StrawhatModel extends Model {

    public ModelRenderer hat;

    public StrawhatModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        this.textureHeight = this.textureWidth = 64;

        hat = new ModelRenderer(this);
        hat.addBox("base", -8, 0, -8, 16, 0, 16, 0, 15, 48);
        hat.setTextureOffset(32, 0);
        hat.addBox( -4, -4.6F, -4, 8, 4, 8, 0.52F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.hat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
