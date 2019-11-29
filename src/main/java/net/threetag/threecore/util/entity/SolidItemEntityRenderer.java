package net.threetag.threecore.util.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nullable;

public class SolidItemEntityRenderer extends EntityRenderer<SolidItemEntity> {

    private final ItemRenderer itemRenderer;

    public SolidItemEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void doRender(SolidItemEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ItemStack itemstack = entity.getItem();

        boolean flag = false;
        if (this.bindEntityTexture(entity)) {
            this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, null);
        GlStateManager.translated(x, y, z);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
        this.itemRenderer.renderItem(itemstack, transformedModel);

        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);
        if (flag) {
            this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SolidItemEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
