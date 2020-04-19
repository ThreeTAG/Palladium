package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class JayGarrickHelmetModel extends Model {

    public ModelRenderer helmet;

    public JayGarrickHelmetModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 64;
        textureHeight = 32;

        helmet = new ModelRenderer(this);
        helmet.addBox("plate", -6.5F, 0, -6.5F, 13, 1, 13, 0, 0, 0);
        helmet.addBox("helmet", -4, -2.4F, -4, 8, 2, 8, 0.6F, 0, 14);
        helmet.addBox("left1", 5.6F, -1, -0.8F, 1, 1, 1, 0, 0, 0);
        helmet.addBox("left2", 5.9F, -1.8F, -1.5F, 1, 1, 1, 0, 0, 2);
        helmet.addBox("left3", 5.6F, -2.6F, -0.7F, 1, 1, 4, 0, 0, 4);
        helmet.addBox("right1", -6.6F, -1, -0.8F, 1, 1, 1, 0, 0, 0);
        helmet.addBox("right2", -6.9F, -1.8F, -1.5F, 1, 1, 1, 0, 0, 2);
        helmet.addBox("right3", -6.6F, -2.6F, -0.7F, 1, 1, 4, 0, 0, 4);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.helmet.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
