package net.threetag.palladium.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;

public class SuitStandBasePlateModel extends SuitStandModel {

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
    public void setupAnim(SuitStandRenderState renderState) {
        super.setupAnim(renderState);
        this.setAllVisible(false);
        this.basePlate.visible = renderState.showBasePlate;
    }

}
