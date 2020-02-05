package net.threetag.threecore.entity;

import net.minecraft.client.Minecraft;
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
import net.threetag.threecore.client.renderer.entity.SuitStandRenderer;
import net.threetag.threecore.client.renderer.entity.SolidItemEntityRenderer;
import net.threetag.threecore.client.renderer.entity.ProjectileEntityRenderer;

@ObjectHolder(ThreeCore.MODID)
public class TCEntityTypes {

    @ObjectHolder("suit_stand")
    public static final EntityType<SuitStandEntity> SUIT_STAND = null;

    @ObjectHolder("projectile")
    public static final EntityType<ProjectileEntity> PROJECTILE = null;

    @ObjectHolder("solid_item")
    public static final EntityType<SolidItemEntity> SOLID_ITEM_ENTITY = null;

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            RenderingRegistry.registerEntityRenderingHandler(SuitStandEntity.class, SuitStandRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ProjectileEntity.class, manager -> new ProjectileEntityRenderer(manager, Minecraft.getInstance().getItemRenderer()));
            RenderingRegistry.registerEntityRenderingHandler(SolidItemEntity.class, SolidItemEntityRenderer::new);
        });
    }

    @SubscribeEvent
    public void registerEntityTypes(RegistryEvent.Register<EntityType<?>> e) {
        e.getRegistry().register(EntityType.Builder.<SuitStandEntity>create(SuitStandEntity::new, EntityClassification.MISC).size(12F / 16F, 2F).setCustomClientFactory((spawnEntity, world) -> SUIT_STAND.create(world)).build(ThreeCore.MODID + ":suit_stand").setRegistryName("suit_stand"));
        e.getRegistry().register(EntityType.Builder.<ProjectileEntity>create(ProjectileEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setCustomClientFactory((spawnEntity, world) -> PROJECTILE.create(world)).build(ThreeCore.MODID + ":projectile").setRegistryName("projectile"));
        e.getRegistry().register(EntityType.Builder.<SolidItemEntity>create(SolidItemEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setCustomClientFactory((spawnEntity, world) -> SOLID_ITEM_ENTITY.create(world)).build(ThreeCore.MODID + ":solid_item").setRegistryName("solid_item"));
    }

}
