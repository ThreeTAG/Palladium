package net.threetag.palladium.client.renderer.renderlayer.fabric;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class RenderLayerRegistryImpl {

    public static final List<Pair<Predicate<EntityType<?>>, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>>>> REGISTERED = new ArrayList<>();

    public static void addLayer(Predicate<EntityType<?>> entityType, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>> renderLayer) {
        REGISTERED.add(Pair.of(entityType, renderLayer));
    }
}
