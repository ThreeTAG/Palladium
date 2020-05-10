package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HydraulicPressPistonModel extends Model {

    public final ModelRenderer plate1;
    public final ModelRenderer plate2;

    public HydraulicPressPistonModel() {
        super(RenderType::getEntityCutoutNoCull);
        textureWidth = 32;
        textureHeight = 32;

        this.plate1 = new ModelRenderer(this, 0, 16);
        this.plate1.addBox(12, -1, 4.0F, 1, 8, 8, 0.0F, false);
        this.plate2 = new ModelRenderer(this);
        this.plate2.addBox(3, -1, 4.0F, 1, 8, 8, 0.0F, false);
    }

    public void setProgress(float progress) {
        this.plate1.rotationPointX = -progress * 4;
        this.plate2.rotationPointX = progress * 4;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int combinedLightIn, int combinedOverlayIn, float red, float green, float blue, float alpha) {
        this.plate1.render(matrixStack, vertexBuilder, combinedLightIn, combinedOverlayIn, red, green, blue, alpha);
        this.plate2.render(matrixStack, vertexBuilder, combinedLightIn, combinedOverlayIn, red, green, blue, alpha);
    }
}
