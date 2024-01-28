package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.SuitStandBasePlateModel;
import net.threetag.palladium.client.model.SuitStandModel;
import net.threetag.palladium.client.renderer.renderlayer.ColorableSuitStandLayer;
import net.threetag.palladium.entity.SuitStand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SuitStandRenderer extends LivingEntityRenderer<SuitStand, SuitStandBasePlateModel> {

    public static final ResourceLocation DEFAULT_SKIN_LOCATION = Palladium.id("textures/entity/suit_stand.png");

    public SuitStandRenderer(EntityRendererProvider.Context context) {
        super(context, new SuitStandBasePlateModel(context.bakeLayer(SuitStandBasePlateModel.MODEL_LAYER_LOCATION)), 0F);
        this.addLayer(new ColorableSuitStandLayer(this, new SuitStandModel(context.bakeLayer(SuitStandModel.MODEL_LAYER_LOCATION))));
        this.addLayer(new HumanoidArmorLayer(this, new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(SuitStand livingEntity, PoseStack matrixStack, float partialTickTime) {
        float scale = 0.9375F;
        matrixStack.scale(scale, scale, scale);
        if (!livingEntity.isNoBasePlate()) matrixStack.translate(0, -1F / 16F, 0F);
    }

    @Override
    protected void setupRotations(SuitStand entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        float f = (float) (entityLiving.level().getGameTime() - entityLiving.lastHit) + partialTicks;
        if (f < 5.0F) {
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(SuitStand entity) {
        double d = this.entityRenderDispatcher.distanceToSqr(entity);
        float f = entity.isCrouching() ? 32.0F : 64.0F;
        return !(d >= (double) (f * f)) && entity.isCustomNameVisible();
    }

    @Nullable
    public RenderType getRenderType(SuitStand livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (!livingEntity.isMarker()) {
            return super.getRenderType(livingEntity, bodyVisible, translucent, glowing);
        } else {
            ResourceLocation resourceLocation = this.getTextureLocation(livingEntity);
            if (translucent) {
                return RenderType.entityTranslucent(resourceLocation, false);
            } else {
                return bodyVisible ? RenderType.entityCutoutNoCull(resourceLocation, false) : null;
            }
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SuitStand entity) {
        return DEFAULT_SKIN_LOCATION;
    }
}
