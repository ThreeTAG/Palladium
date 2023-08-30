package net.threetag.palladium.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.EntityAttributeRegistry;
import net.threetag.palladiumcore.registry.RegistrySupplier;

import java.util.function.Supplier;

public class PalladiumEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Palladium.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>of(EffectEntity::new, MobCategory.MISC).sized(0.1F, 0.1F));
    public static final RegistrySupplier<EntityType<CustomProjectile>> CUSTOM_PROJECTILE = register("custom_projectile", () -> EntityType.Builder.of(CustomProjectile::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(4).updateInterval(10));
    public static final RegistrySupplier<EntityType<SuitStand>> SUIT_STAND = register("suit_stand", () -> EntityType.Builder.<SuitStand>of(SuitStand::new, MobCategory.MISC).sized(0.6F, 1.8F));

    public static void init() {
        EntityAttributeRegistry.register(SUIT_STAND, SuitStand::createLivingAttributes);
    }

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(Palladium.MOD_ID + ":" + id));
    }
}
