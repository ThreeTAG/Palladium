package net.threetag.threecore.scripts.events;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.threetag.threecore.client.renderer.entity.model.BipedModelParser;
import net.threetag.threecore.event.SetRotationAnglesEvent;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.RenderUtil;

/**
 * Created by Nictogen on 2020-07-07.
 */
public class SetRotationAnglesScriptEvent extends LivingScriptEvent {
    public SetRotationAnglesEvent event;

    public SetRotationAnglesScriptEvent(SetRotationAnglesEvent event) {
        super(event.getEntityLiving());
        this.event = event;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public float getLimbSwing() {
        return event.limbSwing;
    }

    public float getLimbSwingAmount() {
        return event.limbSwingAmount;
    }

    public float getAgeInTicks() {
        return event.ageInTicks;
    }

    public float getNetHeadYaw() {
        return event.netHeadYaw;
    }

    public float getHeadPitch() {
        return event.headPitch;
    }

    public float getPartialTicks() {
        return RenderUtil.renderTickTime;
    }

    public void setRotationAngle(@ScriptParameterName("part") String part, @ScriptParameterName("xyz") String xyz, @ScriptParameterName("angle") float angle) {
        ModelRenderer modelRenderer = null;

        if (event.model instanceof BipedModelParser.ParsedBipedModel) {
            modelRenderer = ((BipedModelParser.ParsedBipedModel<?>)event.model).getNamedPart(part);
        } else if(event.model instanceof PlayerModel<?>){

        }

        if(modelRenderer == null) {
            switch (part) {
                case "head":
                    modelRenderer = event.model.bipedHead;
                    break;
                case "headwear":
                case "head_overlay":
                    modelRenderer = event.model.bipedHeadwear;
                    break;
                case "body":
                    modelRenderer = event.model.bipedBody;
                    break;
                case "rightArm":
                case "right_arm":
                    modelRenderer = event.model.bipedRightArm;
                    break;
                case "leftArm":
                case "left_arm":
                    modelRenderer = event.model.bipedLeftArm;
                    break;
                case "rightLeg":
                case "right_leg":
                    modelRenderer = event.model.bipedRightLeg;
                    break;
                case "leftLeg":
                case "left_leg":
                    modelRenderer = event.model.bipedLeftLeg;
                    break;
            }
        }

        if (modelRenderer != null) {
            switch (xyz) {
                case "x":
                    modelRenderer.rotateAngleX = (float) Math.toRadians(angle);
                    break;
                case "y":
                    modelRenderer.rotateAngleY = (float) Math.toRadians(angle);
                    break;
                case "z":
                    modelRenderer.rotateAngleZ = (float) Math.toRadians(angle);
                    break;
            }
        }
    }
}
