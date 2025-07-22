package net.threetag.palladium.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.RenderUtil;

import java.util.HashMap;
import java.util.Map;

public class HideBodyPartsAnimation {

    public static final Map<BodyPart, Boolean> CACHED_VISIBILITIES = new HashMap<>();

    public static void setupAnim(HumanoidModel<?> model, EntityRenderState state) {
        CACHED_VISIBILITIES.clear();

        for (BodyPart bodyPart : BodyPart.values()) {
            var modelPart = bodyPart.getModelPart(model);
            CACHED_VISIBILITIES.put(bodyPart, modelPart == null || modelPart.visible);
        }

        if (RenderUtil.getCurrentlyRenderedEntity() instanceof LivingEntity living) {
            for (BodyPart part : BodyPart.getHiddenBodyParts(living)) {
                part.setVisibility(model, false);
            }
        }
    }

}
