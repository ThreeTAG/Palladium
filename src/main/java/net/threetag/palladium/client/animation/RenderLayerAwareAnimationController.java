package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.DefaultPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;

import java.util.Map;

public class RenderLayerAwareAnimationController extends PlayerAnimationController {

    public RenderLayerAwareAnimationController(Avatar avatar, AnimationStateHandler animationHandler) {
        super(avatar, animationHandler);
    }

    @Override
    public void setupAnim(AnimationData state) {
        boolean dirty = false;
        if (PalladiumEntityData.get(this.avatar, PalladiumEntityDataTypes.RENDER_LAYERS.get()) instanceof ClientEntityRenderLayers layers) {
            for (PackRenderLayer<?> layer : layers.getLayers()) {
                if (layer instanceof DefaultPackRenderLayer packRenderLayer) {
                    for (String partName : packRenderLayer.getPartNames(this.avatar)) {
                        if (!this.bones.containsKey(partName)) {
                            this.registerPlayerAnimBone(partName);
                            dirty = true;
                        }
                    }
                }
            }
        }
        if (dirty)
            forceAnimationReset();
        super.setupAnim(state);
    }

    public Map<String, PlayerAnimBone> getActiveBones(){
        return this.activeBones;
    }
}
