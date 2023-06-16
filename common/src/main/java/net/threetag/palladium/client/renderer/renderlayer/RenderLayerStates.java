package net.threetag.palladium.client.renderer.renderlayer;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderLayerStates {

    private final Map<IPackRenderLayer, State> map = new HashMap<>();

    public void tick(LivingEntity entity) {
        if (PackRenderLayerManager.STATE_FUNCTION == null) {
            return;
        }

        // Gather current layers
        List<IPackRenderLayer> layers = new ArrayList<>();
        PackRenderLayerManager.forEachLayer(entity, (context, layer) -> {
            layers.add(layer);

            if (layer instanceof CompoundPackRenderLayer com) {
                layers.addAll(com.layers());
            }
        });

        // Remove inactive layers
        List<IPackRenderLayer> toRemove = new ArrayList<>();
        for (IPackRenderLayer layer : map.keySet()) {
            boolean found = false;
            for (IPackRenderLayer checkedLayer : layers) {
                if (checkedLayer.isOrContains(layer)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                toRemove.add(layer);
            }
        }

        for (IPackRenderLayer layer : toRemove) {
            this.map.remove(layer);
        }
    }

    @Nullable
    public State get(IPackRenderLayer layer) {
        return this.map.get(layer);
    }

    @Nullable
    public State getOrCreate(IPackRenderLayer layer) {
        if (PackRenderLayerManager.STATE_FUNCTION == null) {
            return null;
        }

        return this.map.computeIfAbsent(layer, l -> PackRenderLayerManager.STATE_FUNCTION.apply(l));
    }

    public static class State {

        int ticks;

        public void tick() {
            this.ticks++;
        }

    }

}
