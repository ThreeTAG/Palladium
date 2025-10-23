package net.threetag.palladium.client.renderer.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.entity.effect.EntityEffects;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.HashMap;
import java.util.Map;

public interface EntityEffectRenderer<T extends EntityEffect> {

    Map<EntityEffect, EntityEffectRenderer<?>> RENDERER_MAP = new HashMap<>();

    static void registerRenderers() {
        registerRenderer(EntityEffects.ENERGY_BEAM.value(), new EnergyBeamEffectRenderer());
    }

    static <T extends EntityEffect> void registerRenderer(T effect, EntityEffectRenderer<T> renderer) {
        RENDERER_MAP.put(effect, renderer);
    }

    @SuppressWarnings("unchecked")
    static <T extends EntityEffect> EntityEffectRenderer<T> getRenderer(T effect) {
        EntityEffectRenderer<T> renderer = (EntityEffectRenderer<T>) RENDERER_MAP.get(effect);

        if (renderer == null) {
            throw new IllegalStateException(String.format("No entity effect renderer registered for %s", PalladiumRegistries.ENTITY_EFFECT.getKey(effect).toString()));
        }

        return renderer;
    }

    static <T extends EntityEffect> void renderEffect(T effect, EffectEntityRenderer.EffectEntityRenderState renderState, Entity anchor, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        getRenderer(effect).render(effect, renderState, anchor, poseStack, submitNodeCollector, packedLightIn, isFirstPerson, partialTicks);
    }

    void render(T effect, EffectEntityRenderer.EffectEntityRenderState renderState, Entity anchor, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, boolean isFirstPerson, float partialTicks);

}
