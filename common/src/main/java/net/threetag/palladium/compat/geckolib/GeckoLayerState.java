package net.threetag.palladium.compat.geckolib;

import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

public class GeckoLayerState extends RenderLayerStates.State implements IAnimatable {

    public final GeckoRenderLayer layer;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this, false);
    private final Map<String, AnimationController<GeckoLayerState>> animationController = new HashMap<>();

    static {
        AnimationController.addModelFetcher(new Fetcher());
    }

    public GeckoLayerState(GeckoRenderLayer layer) {
        this.layer = layer;
    }

    @Override
    public void registerControllers(AnimationData data) {
        if (this.layer.animationLocation != null) {
            for (ParsedAnimationController<GeckoLayerState> parsed : this.layer.animationControllers) {
                var controller = parsed.createController(this);
                data.addAnimationController(controller);
                this.animationController.put(parsed.name, controller);
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Nullable
    public AnimationController<GeckoLayerState> getController(String name) {
        return this.animationController.get(name);
    }

    public static class Fetcher implements AnimationController.ModelFetcher<GeckoLayerState> {

        @Override
        public IAnimatableModel<GeckoLayerState> apply(IAnimatable animatable) {
            if (animatable instanceof GeckoLayerState state) {
                return state.layer.getGeoModel();
            }
            return null;
        }
    }
}
