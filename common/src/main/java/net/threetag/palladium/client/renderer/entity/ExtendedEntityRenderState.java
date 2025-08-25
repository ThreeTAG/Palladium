package net.threetag.palladium.client.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.PlayerModelCacheExtension;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
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
                extState.palladium$addData(DataContextType.Client.RENDER_LAYERS, layers.getLayerStates());
            }

            extState.palladium$addData(DataContextType.Client.PARTIAL_TICK, partialTick);

            if (entity instanceof LivingEntity living) {
                extState.palladium$addData(DataContextType.Client.HIDDEN_BODY_PARTS, BodyPart.getHiddenBodyParts(living));
                extState.palladium$addData(DataContextType.Client.REMOVED_BODY_PARTS, BodyPart.getRemovedBodyParts(living));
                extState.palladium$addData(DataContextType.Client.AIM, AimAbility.getTimer(living, partialTick));
                Map<DataContext, PalladiumAnimation> animations = new HashMap<>();
                for (AbilityInstance<AnimationAbility> ability : AbilityUtil.getEnabledInstances(living, AbilitySerializers.ANIMATION.get())) {
                    for (ResourceLocation animationId : ability.getAbility().animations) {
                        var animation = PalladiumAnimationManager.INSTANCE.get(animationId);
                        if (animation != null) {
                            animations.put(DataContext.forAbility(living, ability), animation);
                        }
                    }
                }
                extState.palladium$addData(DataContextType.Client.ANIMATIONS, animations);
            }

            if (entity instanceof PlayerModelCacheExtension ext) {
                extState.palladium$addData(DataContextType.Client.CACHED_MODEL, ext.palladium$getCachedModel());
            }
        }
    }

}
