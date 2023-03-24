package net.threetag.palladium.compat.geckolib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

public class RenderLayerAnimationAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> RENDER_LAYER = new ResourceLocationProperty("render_layer").configurable("Determines the ID of the render layer receiving the animation. Must be a gecko render layer!");
    public static final PalladiumProperty<String> CONTROLLER = new StringProperty("controller").configurable("Name of the animation controller the animation is played on. Leave it as 'main' if you didnt specify one.");
    public static final PalladiumProperty<String> ANIMATION = new StringProperty("animation").configurable("Animation name that is supposed to be played");

    public RenderLayerAnimationAbility() {
        this.withProperty(RENDER_LAYER, new ResourceLocation("test", "example_layer"));
        this.withProperty(CONTROLLER, "main");
        this.withProperty(ANIMATION, "animation_name");
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.isClientSide && entity instanceof PalladiumLivingEntityExtension extension) {
            this.playAnimation(extension, entry);
        }
    }

    @Environment(EnvType.CLIENT)
    public void playAnimation(PalladiumLivingEntityExtension entity, AbilityEntry entry) {
        IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(entry.getProperty(RENDER_LAYER));
        if (layer != null) {
            var state = entity.palladium_getRenderLayerStates().get(layer);
            if (state instanceof GeckoLayerState gecko) {
                var controller = gecko.getController(entry.getProperty(CONTROLLER));
                if (controller != null) {
                    controller.markNeedsReload();
                    controller.setAnimation(new AnimationBuilder().addAnimation(entry.getProperty(ANIMATION)));
                }
            }
        }
    }
}
