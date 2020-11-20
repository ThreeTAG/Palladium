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
public class FezModel extends Model {

    public static final FezModel INSTANCE = new FezModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/fez.png");

    private final ModelRenderer fez;

    public FezModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 16;
        textureHeight = 16;

        fez = new ModelRenderer(this);
        fez.setRotationPoint(0.0F, 24.0F, 0.0F);
        fez.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        fez.setTextureOffset(0, 8).addBox(-3.0F, -5.0F, 0.0F, 3.0F, 4.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        fez.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}