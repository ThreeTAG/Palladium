package net.threetag.palladium.client.renderer.entity;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.client.util.ClientContextTypes;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.client.util.PlayerModelCacheExtension;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.flight.DefaultFlightType;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;
import net.threetag.palladium.power.ability.*;

import java.util.HashMap;
import java.util.Map;

public interface ExtendedEntityRenderState {

    <T> void palladium$addData(DataContextType<T> type, T value);

    <T> T palladium$getData(DataContextType<T> type);

    boolean palladium$hasData(DataContextType<?> type);

    void palladium$resetData();

    static void init(Entity entity, EntityRenderState state, float partialTick) {
        if (state instanceof ExtendedEntityRenderState extState) {
            extState.palladium$resetData();

            if (PalladiumEntityData.get(entity, PalladiumEntityDataTypes.RENDER_LAYERS.get()) instanceof ClientEntityRenderLayers layers) {
                extState.palladium$addData(ClientContextTypes.RENDER_LAYERS, layers.getLayerStates());
            }

            extState.palladium$addData(ClientContextTypes.PARTIAL_TICK, partialTick);

            if (entity instanceof LivingEntity living) {
                extState.palladium$addData(ClientContextTypes.HIDDEN_BODY_PARTS, BodyPart.getHiddenBodyParts(living));
                extState.palladium$addData(ClientContextTypes.REMOVED_BODY_PARTS, BodyPart.getRemovedBodyParts(living));
                extState.palladium$addData(ClientContextTypes.AIM, AimAbility.getTimer(living, partialTick));
                extState.palladium$addData(ClientContextTypes.IN_FLIGHT, EntityFlightHandler.get(living).getInFlightTimer(partialTick));

                // Animations
                Map<DataContext, PalladiumAnimation> animations = new HashMap<>();
                var flightAnimationHandler = EntityFlightHandler.get(living).getAnimationHandler();
                var flightAnimationId = flightAnimationHandler != null ? flightAnimationHandler.getAnimationAssetId() : null;
                var flightAnim = PalladiumAnimationManager.INSTANCE.get(flightAnimationId);
                if (flightAnim != null) {
                    animations.put(DataContext.forEntity(living), flightAnim);
                }

                for (AbilityInstance<AnimationAbility> ability : AbilityUtil.getEnabledInstances(living, AbilitySerializers.ANIMATION.get())) {
                    for (ResourceLocation animationId : ability.getAbility().animations) {
                        var animation = PalladiumAnimationManager.INSTANCE.get(animationId);
                        if (animation != null) {
                            animations.put(DataContext.forAbility(living, ability), animation);
                        }
                    }
                }
                extState.palladium$addData(ClientContextTypes.ANIMATIONS, animations);
            }

            if (entity instanceof PlayerModelCacheExtension ext) {
                extState.palladium$addData(ClientContextTypes.CACHED_MODEL, ext.palladium$getCachedModel());
            }
        }
    }

    static void manipulatePlayerState(AbstractClientPlayer player, PlayerRenderState state, float partialTick) {
        var flightHandler = EntityFlightHandler.get(player);
        var flightProgress = flightHandler.getInFlightTimer(partialTick);

        if (flightProgress > 0.0F) {
            state.walkAnimationSpeed = Mth.lerp(flightProgress, state.walkAnimationSpeed, 0F);
        }

        if (flightHandler.getAnimationHandler() instanceof DefaultFlightType.AnimationHandler flight) {
            var scale = flight.getPropulsionScale(partialTick);

            if (scale > 0F) {
                double d = Mth.lerp(partialTick, player.xCloakO, player.xCloak) - Mth.lerp(partialTick, player.xo, player.getX());
                double f = Mth.lerp(partialTick, player.zCloakO, player.zCloak) - Mth.lerp(partialTick, player.zo, player.getZ());
                float g = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
                double h = Mth.sin(g * (float) (Math.PI / 180.0));
                double i = -Mth.cos(g * (float) (Math.PI / 180.0));
                state.capeLean = (float) (d * h + f * i) * 100.0F;
                state.capeLean = state.capeLean * (1.0F - scale);
                state.capeLean = Mth.clamp(state.capeLean, 0.0F, 150.0F);
            }
        }
    }

}
