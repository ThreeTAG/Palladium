package net.threetag.palladium.entity;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;

import java.util.function.Supplier;

public class PalladiumEntityTypes {

    public static final DeferredRegistry<EntityType<?>> ENTITIES = DeferredRegistry.create(Palladium.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>of(EffectEntity::new, MobCategory.MISC).sized(0.1F, 0.1F));
    public static final RegistrySupplier<EntityType<CustomProjectile>> CUSTOM_PROJECTILE = register("custom_projectile", () -> EntityType.Builder.of(CustomProjectile::new, MobCategory.MISC).sized(0.1F, 0.1F));

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(Palladium.MOD_ID + ":" + id));
    }
}
