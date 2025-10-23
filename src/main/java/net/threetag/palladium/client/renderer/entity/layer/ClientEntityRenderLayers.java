package net.threetag.palladium.client.renderer.entity.layer;

import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class ClientEntityRenderLayers extends EntityRenderLayers {

    private final Map<PackRenderLayer<?>, PackRenderLayer.State> layers = new HashMap<>();

    @Override
    public void tick() {
        // Gather current layers
        List<PackRenderLayer<?>> layers = new ArrayList<>();
        PackRenderLayerProvider.forEach(this.getEntity(), (context, layer) -> {
            layers.add(layer);

            if (!this.layers.containsKey(layer)) {
                this.layers.put(layer, layer.createState(context));
            } else {
                this.layers.get(layer).updateContext(context);
            }

            if (layer instanceof CompoundPackRenderLayer com) {
                for (PackRenderLayer<?> comLayer : com.getLayers()) {
                    if (!this.layers.containsKey(comLayer)) {
                        this.layers.put(comLayer, comLayer.createState(context));
                    } else {
                        this.layers.get(comLayer).updateContext(context);
                    }
                }
            }
        });

        // Remove inactive layers
        for (Map.Entry<PackRenderLayer<?>, PackRenderLayer.State> e : this.layers.entrySet()) {
            var layer = e.getKey();
            boolean found = false;
            for (PackRenderLayer<?> checkedLayer : layers) {
                if (checkedLayer.isOrContains(layer)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                e.getValue().markForRemoval();
            }
        }

        List<PackRenderLayer<?>> toRemove = new ArrayList<>();
        for (Map.Entry<PackRenderLayer<?>, PackRenderLayer.State> e : this.layers.entrySet()) {
            if (e.getValue().isMarkedForRemoval()) {
                toRemove.add(e.getKey());
            }
        }

        for (PackRenderLayer<?> layer : toRemove) {
            this.layers.remove(layer);
        }

        for (PackRenderLayer.State state : this.layers.values()) {
            state.tick(this.getEntity());
        }
    }

    public Collection<PackRenderLayer<?>> getLayers() {
        return this.layers.keySet();
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
    public <T extends PackRenderLayer.State> Map<PackRenderLayer<T>, T> getLayerStates() {
        Map map = this.layers;
        return map;
    }
}
