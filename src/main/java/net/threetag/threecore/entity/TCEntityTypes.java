package net.threetag.threecore.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.ProjectileEntityRenderer;
import net.threetag.threecore.client.renderer.entity.SolidItemEntityRenderer;
import net.threetag.threecore.client.renderer.entity.SuitStandRenderer;
import net.threetag.threecore.util.entityeffect.EffectEntity;
import net.threetag.threecore.util.entityeffect.EffectEntityRenderer;

import java.util.function.Supplier;

public class TCEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, ThreeCore.MODID);

    public static final RegistryObject<EntityType<SuitStandEntity>> SUIT_STAND = register("suit_stand", () -> EntityType.Builder.<SuitStandEntity>create(SuitStandEntity::new, EntityClassification.MISC).size(12F / 16F, 2F));
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile", () -> EntityType.Builder.<ProjectileEntity>create(ProjectileEntity::new, EntityClassification.MISC).size(0.25F, 0.25F));
    public static final RegistryObject<EntityType<SolidItemEntity>> SOLID_ITEM_ENTITY = register("solid_item", () -> EntityType.Builder.<SolidItemEntity>create(SolidItemEntity::new, EntityClassification.MISC).size(0.25F, 0.25F));
    public static final RegistryObject<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>create(EffectEntity::new, EntityClassification.MISC).size(0.1F, 0.1F));

    @OnlyIn(Dist.CLIENT)
    public static void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(SuitStandEntity.class, SuitStandRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ProjectileEntity.class, manager -> new ProjectileEntityRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(SolidItemEntity.class, SolidItemEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EffectEntity.class, EffectEntityRenderer::new);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(ThreeCore.MODID + ":" + id));
    }

}
