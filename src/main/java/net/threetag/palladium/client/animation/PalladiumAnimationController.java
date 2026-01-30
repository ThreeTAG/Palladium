package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.DefaultPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;

public class PalladiumAnimationController extends PlayerAnimationController {

    public PalladiumAnimationController(Avatar avatar, AnimationStateHandler animationHandler) {
        super(avatar, animationHandler);
    }

    @Override
    public void setupNewAnimation() {
        if (PalladiumEntityData.get(this.avatar, PalladiumEntityDataTypes.RENDER_LAYERS.get()) instanceof ClientEntityRenderLayers layers) {
            for (PackRenderLayer<?> layer : layers.getLayers()) {
                if(layer instanceof DefaultPackRenderLayer packRenderLayer){
                    for (String partName : packRenderLayer.getPartNames(this.avatar)) {
                        if(!this.bones.containsKey(partName))
                            this.registerPlayerAnimBone(partName);
                    }
                }
            }
        }
        super.setupNewAnimation();
    }
}
