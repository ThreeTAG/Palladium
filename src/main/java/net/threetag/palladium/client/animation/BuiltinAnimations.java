package net.threetag.palladium.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.ModelUtil;

import java.util.Collections;
import java.util.Set;

public class BuiltinAnimations {

    public static void setupAnim(HumanoidModel<?> model, HumanoidRenderState state) {
        var aim = state.getRenderDataOrDefault(PalladiumRenderStateKeys.AIM, new Float[]{0F, 0F});
        var hidden = state.getRenderDataOrDefault(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS, Collections.emptySet());

        aim(model, aim);
        hiddenModelParts(model, hidden);
    }

    public static void aim(HumanoidModel<?> model, Float[] aim) {
        float left = aim[0];
        float right = aim[1];

        if (left > 0F) {
            model.leftArm.xRot = Mth.lerp(left, model.leftArm.xRot, (float) (model.head.xRot - Math.PI / 2F));
            model.leftArm.yRot = Mth.lerp(left, model.leftArm.yRot, model.head.yRot);
            model.leftArm.zRot = Mth.lerp(left, model.leftArm.zRot, model.head.zRot);
        }

        if (right > 0F) {
            model.rightArm.xRot = Mth.lerp(right, model.rightArm.xRot, (float) (model.head.xRot - Math.PI / 2F));
            model.rightArm.yRot = Mth.lerp(right, model.rightArm.yRot, model.head.yRot);
            model.rightArm.zRot = Mth.lerp(right, model.rightArm.zRot, model.head.zRot);
        }
    }

    public static void hiddenModelParts(HumanoidModel<?> model, Set<String> hidden) {
        for (String name : hidden) {
            var part = ModelUtil.getPartFromModel(model, name);

            if (part != null) {
                part.visible = false;
            }
        }
    }

}
