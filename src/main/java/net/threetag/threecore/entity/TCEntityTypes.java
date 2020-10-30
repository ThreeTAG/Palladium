package net.threetag.threecore.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
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
import net.threetag.threecore.entity.attributes.TCAttributes;
import net.threetag.threecore.util.entityeffect.EffectEntity;
import net.threetag.threecore.util.entityeffect.EffectEntityRenderer;

import java.util.Map;
import java.util.function.Supplier;

public class TCEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ThreeCore.MODID);

    public static final RegistryObject<EntityType<SuitStandEntity>> SUIT_STAND = register("suit_stand", () -> EntityType.Builder.<SuitStandEntity>create(SuitStandEntity::new, EntityClassification.MISC).size(12F / 16F, 2F));
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile", () -> EntityType.Builder.<ProjectileEntity>create(ProjectileEntity::new, EntityClassification.MISC).size(0.25F, 0.25F));
    public static final RegistryObject<EntityType<SolidItemEntity>> SOLID_ITEM_ENTITY = register("solid_item", () -> EntityType.Builder.<SolidItemEntity>create(SolidItemEntity::new, EntityClassification.MISC).size(0.25F, 0.25F));
    public static final RegistryObject<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>create(EffectEntity::new, EntityClassification.MISC).size(0.1F, 0.1F));

    public static void initAttributes() {
        GlobalEntityTypeAttributes.put(SUIT_STAND.get(), LivingEntity.registerAttributes().create());

        // add ThreeCore's custom attributes
        for (EntityType<?> value : ForgeRegistries.ENTITIES.getValues()) {
            AttributeModifierMap map = GlobalEntityTypeAttributes.getAttributesForEntity((EntityType<? extends LivingEntity>) value);
            if (map != null) {
                Map<Attribute, ModifiableAttributeInstance> oldAttributes = map.attributeMap;
                AttributeModifierMap.MutableAttribute newMap = AttributeModifierMap.createMutableAttribute();
                newMap.attributeMap.putAll(oldAttributes);
                newMap.createMutableAttribute(TCAttributes.STEP_HEIGHT.get(), 1D);
                newMap.createMutableAttribute(TCAttributes.FALL_RESISTANCE.get());
                newMap.createMutableAttribute(TCAttributes.JUMP_HEIGHT.get());
                newMap.createMutableAttribute(TCAttributes.SPRINT_SPEED.get(), 1D);
                newMap.createMutableAttribute(TCAttributes.SIZE_WIDTH.get());
                newMap.createMutableAttribute(TCAttributes.SIZE_HEIGHT.get());
                GlobalEntityTypeAttributes.put((EntityType<? extends LivingEntity>) value, newMap.create());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(SUIT_STAND.get(), SuitStandRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PROJECTILE.get(), manager -> new ProjectileEntityRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(SOLID_ITEM_ENTITY.get(), SolidItemEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EFFECT.get(), EffectEntityRenderer::new);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(ThreeCore.MODID + ":" + id));
    }

}
