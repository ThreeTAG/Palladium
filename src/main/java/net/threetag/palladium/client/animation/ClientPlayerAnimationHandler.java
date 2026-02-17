package net.threetag.palladium.client.animation;

import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;

import java.util.HashMap;
import java.util.Map;

public class ClientPlayerAnimationHandler extends PlayerAnimationHandler {

    private Map<PalladiumAnimationLayer, AnimationContainer> containers = new HashMap<>();

    @Override
    public void tick() {
        if (!this.getEntity().level().isClientSide()) {
            return;
        }

        for (AnimationContainer container : this.containers.values()) {
            container.tick();
        }
    }

    public void setContainer(PalladiumAnimationLayer layer, AnimationContainer container) {
        this.containers.put(layer, container);
    }

    public static ClientPlayerAnimationHandler getHandler(Avatar player) {
        return (ClientPlayerAnimationHandler) get(player, PalladiumEntityDataTypes.ANIMATIONS.get());
    }

}
