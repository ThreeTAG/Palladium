package net.threetag.palladium.client.renderer.entity.state;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
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
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.VibrationPackRenderLayer;
import net.threetag.palladium.client.trail.EntityTrailHandler;
import net.threetag.palladium.client.trail.Trail;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.flight.DefaultFlightType;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.AimAbility;
import net.threetag.palladium.power.ability.OpacityChanging;

import java.util.*;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumRenderStateKeys {

    public static ContextKey<Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State>> RENDER_LAYERS = create("render_layers");
    public static ContextKey<Set<String>> HIDDEN_MODEL_PARTS = create("hidden_model_parts");
    public static ContextKey<Float[]> AIM = create("aim");
    public static ContextKey<Float> SHRINK_OVERLAY = create("shrink_player_overlay");
    public static ContextKey<Map<DataContext, PalladiumAnimation>> ANIMATIONS = create("animations");
    public static ContextKey<Float> IN_FLIGHT = create("in_flight");
    public static ContextKey<Float> OPACITY = create("opacity");
    public static ContextKey<Integer> TINT = create("tint");
    public static ContextKey<Map<Trail, EntityTrailHandler.TrailInstance>> TRAILS = create("trails");
    public static ContextKey<EntityRenderState> VIBRATION_RENDER_STATE = create("vibration_render_state");

    public static boolean IGNORE_VIBRATION_STATE = false;

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
            state.setRenderData(SHRINK_OVERLAY, AbilityUtil.getHighestAnimationTimerProgress(entity, AbilitySerializers.SHRINK_PLAYER_OVERLAY.get(), state.partialTick));
            state.setRenderData(IN_FLIGHT, EntityFlightHandler.get(entity).getInFlightTimer(state.partialTick));
            state.setRenderData(TINT, -1);
            state.setRenderData(TRAILS, EntityTrailHandler.get(entity).getTrails());

            float opacity = OpacityChanging.getOpacity(entity, entity instanceof LocalPlayer, state.partialTick);
            state.setRenderData(OPACITY, opacity);

            if (opacity < 1.0F) {
                List<EntityRenderState.ShadowPiece> newShadows = new ArrayList<>();
                for (EntityRenderState.ShadowPiece shadow : state.shadowPieces) {
                    newShadows.add(new EntityRenderState.ShadowPiece(shadow.relativeX(), shadow.relativeY(), shadow.relativeZ(), shadow.shapeBelow(), shadow.alpha() * opacity));
                }
                state.shadowPieces.clear();
                state.shadowPieces.addAll(newShadows);
            }

            if (!IGNORE_VIBRATION_STATE) {
                state.setRenderData(VIBRATION_RENDER_STATE, VibrationPackRenderLayer.createRenderStateForVibrations(entity, state.partialTick));
                IGNORE_VIBRATION_STATE = false;
            }

            // Animations
            Map<DataContext, PalladiumAnimation> animations = new HashMap<>();
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

    public static void ignoreVibrationState() {
        IGNORE_VIBRATION_STATE = true;
    }

}
