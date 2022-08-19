package net.threetag.palladium.client.renderer.renderlayer;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class RenderLayerRegistry {

    @ExpectPlatform
    public static void addLayer(Predicate<EntityType<?>> entityType, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>> renderLayer) {
        throw new AssertionError();
    }

    public static void addToPlayer(Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>> renderLayer) {
        addLayer(type -> type == EntityType.PLAYER, renderLayer);
    }

    public static void addToAll(Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>> renderLayer) {
        addLayer(type -> true, renderLayer);
    }

}
