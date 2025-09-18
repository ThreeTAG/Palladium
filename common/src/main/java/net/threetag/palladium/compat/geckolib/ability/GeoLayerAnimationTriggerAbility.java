package net.threetag.palladium.compat.geckolib.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer;
import net.threetag.palladium.compat.geckolib.layer.GeoRenderLayerState;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.power.ability.*;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class GeoLayerAnimationTriggerAbility extends Ability {

    public static final MapCodec<GeoLayerAnimationTriggerAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("render_layer").forGetter(a -> a.renderLayer),
            Codec.STRING.fieldOf("controller").forGetter(a -> a.controller),
            Codec.STRING.fieldOf("trigger").forGetter(a -> a.trigger),
            propertiesCodec(), stateCodec(), energyBarUsagesCodec()
    ).apply(instance, GeoLayerAnimationTriggerAbility::new));

    private final ResourceLocation renderLayer;
    private final String controller;
    private final String trigger;

    public GeoLayerAnimationTriggerAbility(ResourceLocation renderLayer, String controller, String trigger, AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
        this.renderLayer = renderLayer;
        this.controller = controller;
        this.trigger = trigger;
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if (enabled && entity.level().isClientSide) {
            playAnimation(entity, abilityInstance, this.renderLayer, this.controller, this.trigger);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void playAnimation(LivingEntity entity, AbilityInstance<?> abilityInstance, ResourceLocation renderLayer, String controller, String trigger) {
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

    @Override
    public AbilitySerializer<?> getSerializer() {
        return GeckoLibCompat.TRIGGER_LAYER_ANIMATION.get();
    }

    public static final class Serializer extends AbilitySerializer<GeoLayerAnimationTriggerAbility> {

        @Override
        public MapCodec<GeoLayerAnimationTriggerAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, GeoLayerAnimationTriggerAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("This ability let's you trigger animations defined in a GeckoLib render layer.")
                    .add("render_layer", TYPE_RESOURCE_LOCATION, "The ID of the render layer receiving the animation. Must be a gecko render layer!")
                    .add("controller", TYPE_STRING, "Name of the animation controller the animation is played on.")
                    .add("trigger", TYPE_STRING, "Name of the animation trigger")
                    .setExampleObject(new GeoLayerAnimationTriggerAbility(ResourceLocation.fromNamespaceAndPath("example", "geo_render_layer"), "example_controller", "example_trigger", AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }

    }
}
