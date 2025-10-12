package net.threetag.palladium.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;

import java.util.function.Supplier;

public class PalladiumEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Palladium.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistryHolder<EntityType<SuitStand>> SUIT_STAND = register("suit_stand", () -> EntityType.Builder.<SuitStand>of(SuitStand::new, MobCategory.MISC).sized(0.6F, 1.8F));
    public static final RegistryHolder<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>of(EffectEntity::new, MobCategory.MISC).sized(0.1F, 0.1F));
    public static final RegistryHolder<EntityType<SwingAnchor>> SWING_ANCHOR = register("swing_anchor", () -> EntityType.Builder.<SwingAnchor>of(SwingAnchor::new, MobCategory.MISC).sized(0.1F, 0.1F));

    public static void init() {
        EntityAttributeRegistry.register(SUIT_STAND, SuitStand::createAttributes);
    }

    private static <T extends Entity> RegistryHolder<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(ResourceKey.create(Registries.ENTITY_TYPE, Palladium.id(id))));
    }
}
