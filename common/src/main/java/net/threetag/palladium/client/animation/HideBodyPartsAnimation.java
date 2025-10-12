package net.threetag.palladium.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.threetag.palladium.client.renderer.entity.ExtendedEntityRenderState;
import net.threetag.palladium.client.util.ClientContextTypes;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.entity.BodyPart;

import java.util.HashMap;
import java.util.Map;

public class HideBodyPartsAnimation {

    public static final Map<BodyPart, Boolean> CACHED_VISIBILITIES = new HashMap<>();

    public static void setupAnim(HumanoidModel<?> model, EntityRenderState state) {
        CACHED_VISIBILITIES.clear();

        for (BodyPart bodyPart : BodyPart.values()) {
            var modelPart = ModelUtil.getModelPartByBodyPart(model, bodyPart);
            CACHED_VISIBILITIES.put(bodyPart, modelPart == null || modelPart.visible);
        }

        if (state instanceof ExtendedEntityRenderState ext && ext.palladium$hasData(ClientContextTypes.HIDDEN_BODY_PARTS)) {
            for (BodyPart part : ext.palladium$getData(ClientContextTypes.HIDDEN_BODY_PARTS)) {
                ModelUtil.setVisibilityByBodyPart(model, part, false);
            }
        }
    }

}
