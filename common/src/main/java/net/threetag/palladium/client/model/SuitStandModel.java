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
        this.head.xRot = ((float) Math.PI / 180F) * renderState.headPose.x();
        this.head.yRot = ((float) Math.PI / 180F) * renderState.headPose.y();
        this.head.zRot = ((float) Math.PI / 180F) * renderState.headPose.z();
        this.body.xRot = ((float) Math.PI / 180F) * renderState.bodyPose.x();
        this.body.yRot = ((float) Math.PI / 180F) * renderState.bodyPose.y();
        this.body.zRot = ((float) Math.PI / 180F) * renderState.bodyPose.z();
        this.leftArm.xRot = ((float) Math.PI / 180F) * renderState.leftArmPose.x();
        this.leftArm.yRot = ((float) Math.PI / 180F) * renderState.leftArmPose.y();
        this.leftArm.zRot = ((float) Math.PI / 180F) * renderState.leftArmPose.z();
        this.rightArm.xRot = ((float) Math.PI / 180F) * renderState.rightArmPose.x();
        this.rightArm.yRot = ((float) Math.PI / 180F) * renderState.rightArmPose.y();
        this.rightArm.zRot = ((float) Math.PI / 180F) * renderState.rightArmPose.z();
        this.leftLeg.xRot = ((float) Math.PI / 180F) * renderState.leftLegPose.x();
        this.leftLeg.yRot = ((float) Math.PI / 180F) * renderState.leftLegPose.y();
        this.leftLeg.zRot = ((float) Math.PI / 180F) * renderState.leftLegPose.z();
        this.rightLeg.xRot = ((float) Math.PI / 180F) * renderState.rightLegPose.x();
        this.rightLeg.yRot = ((float) Math.PI / 180F) * renderState.rightLegPose.y();
        this.rightLeg.zRot = ((float) Math.PI / 180F) * renderState.rightLegPose.z();
        this.leftArm.visible = renderState.showArms;
        this.rightArm.visible = renderState.showArms;
    }

}
