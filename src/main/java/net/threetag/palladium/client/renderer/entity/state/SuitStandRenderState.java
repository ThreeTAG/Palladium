package net.threetag.palladium.client.renderer.entity.state;

import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.item.DyeColor;
import net.threetag.palladium.entity.SuitStand;

public class SuitStandRenderState extends ArmorStandRenderState {

    public int id;
    public DyeColor color;
    public boolean rainbowColor;

    public SuitStandRenderState() {
        this.headPose = SuitStand.DEFAULT_HEAD_POSE;
        this.bodyPose = SuitStand.DEFAULT_BODY_POSE;
        this.leftArmPose = SuitStand.DEFAULT_LEFT_ARM_POSE;
        this.rightArmPose = SuitStand.DEFAULT_RIGHT_ARM_POSE;
        this.leftLegPose = SuitStand.DEFAULT_LEFT_LEG_POSE;
        this.rightLegPose = SuitStand.DEFAULT_RIGHT_LEG_POSE;
    }

    public int getDyeColor() {
        return this.rainbowColor ? ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, this.ageInTicks) : ColorLerper.Type.SHEEP.getColor(this.color);
    }

}
