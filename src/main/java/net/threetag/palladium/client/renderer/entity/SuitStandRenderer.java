package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.PalladiumModelLayers;
import net.threetag.palladium.client.model.SuitStandBasePlateModel;
import net.threetag.palladium.client.model.SuitStandModel;
import net.threetag.palladium.client.renderer.entity.layer.ColorableSuitStandLayer;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;
import net.threetag.palladium.entity.SuitStand;
import org.jetbrains.annotations.Nullable;

public class SuitStandRenderer extends LivingEntityRenderer<SuitStand, SuitStandRenderState, SuitStandBasePlateModel> {

    public static final Identifier TEXTURE = Palladium.id("textures/entity/suit_stand.png");

    public SuitStandRenderer(EntityRendererProvider.Context context) {
        super(context, new SuitStandBasePlateModel(context.bakeLayer(PalladiumModelLayers.SUIT_STAND_BASE_PLATE)), 0F);
        this.addLayer(new ColorableSuitStandLayer(this, new SuitStandModel(context.bakeLayer(PalladiumModelLayers.SUIT_STAND))));
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        ArmorModelSet.bake(ModelLayers.ARMOR_STAND_ARMOR, context.getModelSet(), SuitStandModel::new),
                        ArmorModelSet.bake(ModelLayers.ARMOR_STAND_SMALL_ARMOR, context.getModelSet(), SuitStandModel::new),
                        context.getEquipmentRenderer()
                )
        );
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    }

    @Override
    public Identifier getTextureLocation(SuitStandRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public SuitStandRenderState createRenderState() {
        return new SuitStandRenderState();
    }

    @Override
    public void extractRenderState(SuitStand suitStand, SuitStandRenderState renderState, float f) {
        super.extractRenderState(suitStand, renderState, f);
        HumanoidMobRenderer.extractHumanoidRenderState(suitStand, renderState, f, this.itemModelResolver);
        renderState.yRot = Mth.rotLerp(f, suitStand.yRotO, suitStand.getYRot());
        renderState.isMarker = suitStand.isMarker();
        renderState.isSmall = suitStand.isSmall();
        renderState.showArms = suitStand.showArms();
        renderState.showBasePlate = suitStand.showBasePlate();
        renderState.bodyPose = suitStand.getBodyPose();
        renderState.headPose = suitStand.getHeadPose();
        renderState.leftArmPose = suitStand.getLeftArmPose();
        renderState.rightArmPose = suitStand.getRightArmPose();
        renderState.leftLegPose = suitStand.getLeftLegPose();
        renderState.rightLegPose = suitStand.getRightLegPose();
        renderState.wiggle = (float) (suitStand.level().getGameTime() - suitStand.lastHit) + f;
        renderState.id = suitStand.getId();
        renderState.color = suitStand.getDyeColor();
        renderState.rainbowColor = suitStand.getCustomName() != null && "Lucraft".equals(suitStand.getCustomName().getString());
    }

    @Override
    protected void scale(SuitStandRenderState renderState, PoseStack poseStack) {
        float scale = 0.9375F;
        poseStack.scale(scale, scale, scale);
        if (renderState.showBasePlate)
            poseStack.translate(0, -1F / 16F, 0F);
    }

    @Override
    protected void setupRotations(SuitStandRenderState renderState, PoseStack poseStack, float f, float g) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f));
        if (renderState.wiggle < 5.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(renderState.wiggle / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(SuitStand suitStand, double d) {
        return suitStand.isCustomNameVisible();
    }

    @Nullable
    protected RenderType getRenderType(SuitStandRenderState renderState, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (!renderState.isMarker) {
            return super.getRenderType(renderState, bodyVisible, translucent, glowing);
        } else {
            Identifier identifier = this.getTextureLocation(renderState);
            if (translucent) {
                return RenderTypes.entityTranslucent(identifier, false);
            } else {
                return bodyVisible ? RenderTypes.entityCutoutNoCull(identifier, false) : null;
            }
        }
    }
}
