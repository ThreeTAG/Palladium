package net.threetag.palladium.mixin.fabric;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.mixin.client.rendering.LivingEntityRendererAccessor;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.renderer.renderlayer.fabric.RenderLayerRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Shadow
    private Map<EntityType<?>, EntityRenderer<?>> renderers;

    @Shadow
    private Map<String, EntityRenderer<? extends Player>> playerRenderers;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(at = @At("RETURN"), method = "onResourceManagerReload")
    private void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        for (EntityType entityType : Registry.ENTITY_TYPE) {
            try {
                for (Pair<Predicate<EntityType<?>>, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>>> pair : RenderLayerRegistryImpl.REGISTERED) {
                    if (pair.getFirst().test(entityType)) {
                        if (entityType == EntityType.PLAYER) {
                            this.playerRenderers.values().forEach(renderer -> {
                                if (renderer instanceof LivingEntityRendererAccessor accessor) {
                                    accessor.callAddFeature(pair.getSecond().apply((RenderLayerParent<?, ?>) accessor));
                                }
                            });
                        } else {
                            EntityRenderer renderer = this.renderers.get(entityType);

                            if (renderer instanceof LivingEntityRendererAccessor accessor) {
                                accessor.callAddFeature(pair.getSecond().apply((RenderLayerParent<?, ?>) accessor));
                            }
                        }
                    }
                }
            } catch (Exception ignored) {

            }
        }
        RenderLayerRegistryImpl.REGISTERED.clear();
    }

}
