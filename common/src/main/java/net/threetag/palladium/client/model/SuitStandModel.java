package net.threetag.palladium.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;

public class SuitStandModel extends HumanoidModel<SuitStandRenderState> {

    public SuitStandModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createLayer() {
        var deformation = new CubeDeformation(-0.01F);
        MeshDefinition meshDefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, deformation), PartPose.ZERO);
        partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 16).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, deformation), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 16).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(160, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(SuitStandRenderState renderState) {
        super.setupAnim(renderState);
        this.hat.visible = false;
        this.head.xRot = ((float) Math.PI / 180F) * renderState.headPose.getX();
        this.head.yRot = ((float) Math.PI / 180F) * renderState.headPose.getY();
        this.head.zRot = ((float) Math.PI / 180F) * renderState.headPose.getZ();
        this.body.xRot = ((float) Math.PI / 180F) * renderState.bodyPose.getX();
        this.body.yRot = ((float) Math.PI / 180F) * renderState.bodyPose.getY();
        this.body.zRot = ((float) Math.PI / 180F) * renderState.bodyPose.getZ();
        this.leftArm.xRot = ((float) Math.PI / 180F) * renderState.leftArmPose.getX();
        this.leftArm.yRot = ((float) Math.PI / 180F) * renderState.leftArmPose.getY();
        this.leftArm.zRot = ((float) Math.PI / 180F) * renderState.leftArmPose.getZ();
        this.rightArm.xRot = ((float) Math.PI / 180F) * renderState.rightArmPose.getX();
        this.rightArm.yRot = ((float) Math.PI / 180F) * renderState.rightArmPose.getY();
        this.rightArm.zRot = ((float) Math.PI / 180F) * renderState.rightArmPose.getZ();
        this.leftLeg.xRot = ((float) Math.PI / 180F) * renderState.leftLegPose.getX();
        this.leftLeg.yRot = ((float) Math.PI / 180F) * renderState.leftLegPose.getY();
        this.leftLeg.zRot = ((float) Math.PI / 180F) * renderState.leftLegPose.getZ();
        this.rightLeg.xRot = ((float) Math.PI / 180F) * renderState.rightLegPose.getX();
        this.rightLeg.yRot = ((float) Math.PI / 180F) * renderState.rightLegPose.getY();
        this.rightLeg.zRot = ((float) Math.PI / 180F) * renderState.rightLegPose.getZ();
        this.leftArm.visible = renderState.showArms;
        this.rightArm.visible = renderState.showArms;
    }

}
