package net.threetag.palladium.client.renderer.renderlayer.forge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.palladium.Palladium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderLayerRegistryImpl {

    private static final List<Pair<Predicate<EntityType<?>>, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>>>> REGISTERED = new ArrayList<>();

    public static void addLayer(Predicate<EntityType<?>> entityType, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>> renderLayer) {
        REGISTERED.add(Pair.of(entityType, renderLayer));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.AddLayers e) {
        for (EntityType entityType : ForgeRegistries.ENTITIES.getValues()) {
            try {
                for (Pair<Predicate<EntityType<?>>, Function<RenderLayerParent<?, ?>, RenderLayer<?, ?>>> pair : REGISTERED) {
                    if (pair.getFirst().test(entityType)) {
                        if (entityType == EntityType.PLAYER) {
                            e.getSkins().forEach(skin -> {
                                LivingEntityRenderer renderer = e.getSkin(skin);
                                if (renderer != null) {
                                    renderer.addLayer(pair.getSecond().apply(renderer));
                                }
                            });
                        } else {
                            EntityRenderer renderer = e.getRenderer(entityType);

                            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                                livingRenderer.addLayer(pair.getSecond().apply(livingRenderer));
                            }
                        }
                    }
                }
            } catch (Exception ignored) {

            }
        }
        REGISTERED.clear();
    }

}
