package net.threetag.palladium.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.threetag.palladium.client.renderer.entity.ExtendedEntityRenderState;
import net.threetag.palladium.data.DataContextType;
import net.threetag.palladium.entity.BodyPart;

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

        if (state instanceof ExtendedEntityRenderState ext && ext.palladium$hasData(DataContextType.Client.HIDDEN_BODY_PARTS)) {
            for (BodyPart part : ext.palladium$getData(DataContextType.Client.HIDDEN_BODY_PARTS)) {
                part.setVisibility(model, false);
            }
        }
    }

}
