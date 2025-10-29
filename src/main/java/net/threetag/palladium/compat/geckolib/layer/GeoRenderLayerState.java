package net.threetag.palladium.compat.geckolib.layer;

import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.logic.context.DataContext;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GeoRenderLayerState extends PackRenderLayer.State implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, false);

    public GeoRenderLayerState(GeoRenderLayer renderLayer, DataContext context) {
        super(renderLayer, context);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        for (AnimationControllerFactory<?> factory : ((GeoRenderLayer) this.renderLayer).animationController) {
            AnimationControllerFactory f = factory;
            controllers.add(f.create(this));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        return this.ticks + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
    }
}
