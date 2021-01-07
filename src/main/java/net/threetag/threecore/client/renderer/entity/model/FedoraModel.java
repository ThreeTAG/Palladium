package net.threetag.threecore.client.renderer.entity.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


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
public class FedoraModel extends Model {

    public static final FedoraModel INSTANCE = new FedoraModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE_ELTON_HAT = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/elton_hat.png");
    public static final ResourceLocation TEXTURE_OWCA_FEDORA = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/owca_fedora.png");

    private final ModelRenderer hat;

    public FedoraModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 64;
        textureHeight = 32;

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, 22.5F, 0.0F);
        hat.setTextureOffset(0, 14).addBox(-4.5F, -2.5F, -4.5F, 9.0F, 3.0F, 9.0F, 0.0F, false);
        hat.setTextureOffset(0, 0).addBox(-6.5F, 0.5F, -6.5F, 13.0F, 1.0F, 13.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}