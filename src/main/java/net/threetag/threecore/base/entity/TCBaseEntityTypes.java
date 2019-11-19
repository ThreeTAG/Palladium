package net.threetag.threecore.base.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.client.renderer.entity.SuitStandRenderer;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseEntityTypes {

    @ObjectHolder("suit_stand")
    public static final EntityType<SuitStandEntity> SUIT_STAND = null;

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            RenderingRegistry.registerEntityRenderingHandler(SuitStandEntity.class, SuitStandRenderer::new);
        });
    }

    @SubscribeEvent
    public void registerEntityTypes(RegistryEvent.Register<EntityType<?>> e) {
        e.getRegistry().register(EntityType.Builder.<SuitStandEntity>create(SuitStandEntity::new, EntityClassification.MISC).size(12F / 16F, 2F).setCustomClientFactory((spawnEntity, world) -> SUIT_STAND.create(world)).build(ThreeCore.MODID + ":suit_stand").setRegistryName("suit_stand"));
    }

}
