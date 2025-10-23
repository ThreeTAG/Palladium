package net.threetag.palladium.compat.geckolib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer;
import net.threetag.palladium.compat.geckolib.layer.GeoRenderLayerState;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;

public class GeckoClientAbilityHandler extends GeckoAbilityHandler {

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void triggerAnimation(LivingEntity entity, AbilityInstance<?> abilityInstance, ResourceLocation renderLayer, String controller, String trigger) {
        PalladiumEntityData.opt(entity, PalladiumEntityDataTypes.RENDER_LAYERS.get()).ifPresent(l -> {
            var layer = PackRenderLayerManager.INSTANCE.get(renderLayer);

            if (layer instanceof GeoRenderLayer geoLayer && l instanceof ClientEntityRenderLayers layers) {
                var state = layers.getLayerStates().get(layer);

                if (state instanceof GeoRenderLayerState geoState) {
                    var manager = geoState.getAnimatableInstanceCache().getManagerForId(geoLayer.renderer.getInstanceId(geoState, DataContext.forAbility(entity, abilityInstance)));
                    manager.tryTriggerAnimation(controller, trigger);
                }
            }
        });
    }
}
