package net.threetag.palladium.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.SuitStand;

import java.util.Collections;

public class SuitStandBasePlateModel extends HumanoidModel<SuitStand> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Palladium.id("suit_stand"), "base_plate");

    private final ModelPart basePlate;

    public SuitStandBasePlateModel(ModelPart modelPart) {
        super(modelPart);
        this.basePlate = modelPart.getChild("base_plate");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("base_plate", CubeListBuilder.create().texOffs(0, 32).addBox(-6F, 0F, -6F, 12, 1, 12, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public Iterable<ModelPart> headParts() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.basePlate);
    }

    @Override
    public void prepareMobModel(SuitStand entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.basePlate.xRot = 0.0F;
        this.basePlate.yRot = (float) (Math.PI / 180.0) * -Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        this.basePlate.zRot = 0.0F;
        this.basePlate.visible = !entity.isNoBasePlate();
    }

    @Override
    public void setupAnim(SuitStand entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.hat.visible = false;
        this.leftArm.visible = entity.isShowArms();
        this.rightArm.visible = entity.isShowArms();
        this.head.xRot = ((float) Math.PI / 180F) * entity.getHeadPose().getX();
        this.head.yRot = ((float) Math.PI / 180F) * entity.getHeadPose().getY();
        this.head.zRot = ((float) Math.PI / 180F) * entity.getHeadPose().getZ();
        this.body.xRot = ((float) Math.PI / 180F) * entity.getBodyPose().getX();
        this.body.yRot = ((float) Math.PI / 180F) * entity.getBodyPose().getY();
        this.body.zRot = ((float) Math.PI / 180F) * entity.getBodyPose().getZ();
        this.leftArm.xRot = ((float) Math.PI / 180F) * entity.getLeftArmPose().getX();
        this.leftArm.yRot = ((float) Math.PI / 180F) * entity.getLeftArmPose().getY();
        this.leftArm.zRot = ((float) Math.PI / 180F) * entity.getLeftArmPose().getZ();
        this.rightArm.xRot = ((float) Math.PI / 180F) * entity.getRightArmPose().getX();
        this.rightArm.yRot = ((float) Math.PI / 180F) * entity.getRightArmPose().getY();
        this.rightArm.zRot = ((float) Math.PI / 180F) * entity.getRightArmPose().getZ();
        this.leftLeg.xRot = ((float) Math.PI / 180F) * entity.getLeftLegPose().getX();
        this.leftLeg.yRot = ((float) Math.PI / 180F) * entity.getLeftLegPose().getY();
        this.leftLeg.zRot = ((float) Math.PI / 180F) * entity.getLeftLegPose().getZ();
        this.rightLeg.xRot = ((float) Math.PI / 180F) * entity.getRightLegPose().getX();
        this.rightLeg.yRot = ((float) Math.PI / 180F) * entity.getRightLegPose().getY();
        this.rightLeg.zRot = ((float) Math.PI / 180F) * entity.getRightLegPose().getZ();
        this.hat.copyFrom(this.head);
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        ModelPart modelPart = this.getArm(side);
        boolean bl = modelPart.visible;
        modelPart.visible = true;
        super.translateToHand(side, poseStack);
        modelPart.visible = bl;
    }
}
