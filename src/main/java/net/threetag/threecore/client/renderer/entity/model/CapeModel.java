package net.threetag.threecore.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class CapeModel extends Model {

    public static final CapeModel INSTANCE = new CapeModel(RenderType::getEntityTranslucent);

    private final ModelRenderer cape;
    private final ModelRenderer connection;

    public CapeModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 64;
        textureHeight = 32;

        cape = new ModelRenderer(this);
        cape.setRotationPoint(0.0F, 0.0F, 2.0F);
        setRotationAngle(cape, 0.0F, 3.1416F, 0.0F);
        cape.setTextureOffset(0, 8).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 24.0F, 0.0F, 0.0F, false);

        connection = new ModelRenderer(this);
        connection.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(connection, 0.0F, 3.1416F, 0.0F);
        connection.setTextureOffset(-5, 3).addBox(-7.0F, 0.0F, -2.0F, 14.0F, 0.0F, 5.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        cape.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        connection.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setCapeRotation(float rotation) {
        this.cape.rotateAngleX = (float) Math.toRadians(rotation);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}