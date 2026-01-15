package net.threetag.palladium.client.renderer.entity.state;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.trail.EntityTrailHandler;
import net.threetag.palladium.client.trail.Trail;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.flight.DefaultFlightType;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.*;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumRenderStateKeys {

    public static ContextKey<Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State>> RENDER_LAYERS = create("render_layers");
    public static ContextKey<Set<String>> HIDDEN_MODEL_PARTS = create("hidden_model_parts");
    public static ContextKey<Float[]> AIM = create("aim");
    public static ContextKey<Map<DataContext, PalladiumAnimation>> ANIMATIONS = create("animations");
    public static ContextKey<Float> IN_FLIGHT = create("in_flight");
    public static ContextKey<Float> OPACITY = create("opacity");
    public static ContextKey<Integer> TINT = create("tint");
    public static ContextKey<Map<Trail, EntityTrailHandler.TrailInstance>> TRAILS = create("trails");
    public static boolean IGNORE_TRAILS = false;

    private static <T> ContextKey<T> create(String name) {
        return new ContextKey<>(Palladium.id(name));
    }

    @SubscribeEvent
    static void add(RegisterRenderStateModifiersEvent e) {
        e.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> {
            if (PalladiumEntityData.get(entity, PalladiumEntityDataTypes.RENDER_LAYERS.get()) instanceof ClientEntityRenderLayers layers) {
                state.setRenderData(RENDER_LAYERS, layers.getLayerStates());
            } else {
                state.setRenderData(RENDER_LAYERS, Collections.emptyMap());
            }

            state.setRenderData(HIDDEN_MODEL_PARTS, ModelUtil.getHiddenModelPartNames(entity));
            state.setRenderData(AIM, AimAbility.getTimer(entity, state.partialTick));
            state.setRenderData(IN_FLIGHT, EntityFlightHandler.get(entity).getInFlightTimer(state.partialTick));
            state.setRenderData(OPACITY, 1F - AbilityUtil.getHighestAnimationTimerProgress(entity, AbilitySerializers.INVISIBILITY.get(), state.partialTick));
            state.setRenderData(TINT, -1);
            if (!IGNORE_TRAILS) {
                state.setRenderData(TRAILS, EntityTrailHandler.get(entity).getTrails());
            }

            // Animations
            Map<DataContext, PalladiumAnimation> animations = new HashMap<>();
            var flightAnimationHandler = EntityFlightHandler.get(entity).getAnimationHandler();
            var flightAnimationId = flightAnimationHandler != null ? flightAnimationHandler.getAnimationAssetId() : null;
            var flightAnim = PalladiumAnimationManager.INSTANCE.get(flightAnimationId);
            if (flightAnim != null) {
                animations.put(DataContext.forEntity(entity), flightAnim);
            }
            for (AbilityInstance<AnimationAbility> ability : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.ANIMATION.get())) {
                for (Identifier animationId : ability.getAbility().animations) {
                    var animation = PalladiumAnimationManager.INSTANCE.get(animationId);
                    if (animation != null) {
                        animations.put(DataContext.forAbility(entity, ability), animation);
                    }
                }
            }
            state.setRenderData(ANIMATIONS, animations);
        });
    }

    public static <AvatarLikeEntity extends Avatar & ClientAvatarEntity> void manipulatePlayerState(AvatarLikeEntity player, AvatarRenderState state, float partialTick) {
        var flightHandler = EntityFlightHandler.get(player);
        var flightProgress = flightHandler.getInFlightTimer(partialTick);

        if (flightProgress > 0.0F) {
            state.walkAnimationSpeed = Mth.lerp(flightProgress, state.walkAnimationSpeed, 0F);
        }

        if (flightHandler.getAnimationHandler() instanceof DefaultFlightType.AnimationHandler flight) {
            var scale = flight.getPropulsionScale(partialTick);

            if (scale > 0F) {
                ClientAvatarState clientavatarstate = player.avatarState();
                double d0 = clientavatarstate.getInterpolatedCloakX(partialTick) - Mth.lerp(partialTick, player.xo, player.getX());
                double d2 = clientavatarstate.getInterpolatedCloakZ(partialTick) - Mth.lerp(partialTick, player.zo, player.getZ());
                float f = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
                double d3 = Mth.sin(f * (float) (Math.PI / 180.0));
                double d4 = -Mth.cos(f * (float) (Math.PI / 180.0));
                state.capeLean = (float) (d0 * d3 + d2 * d4) * 100.0F;
                state.capeLean = state.capeLean * (1.0F - scale);
                state.capeLean = Mth.clamp(state.capeLean, 0.0F, 150.0F);
            }
        }
    }

}
