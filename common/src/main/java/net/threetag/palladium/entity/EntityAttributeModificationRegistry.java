package net.threetag.palladium.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class EntityAttributeModificationRegistry {

    @ExpectPlatform
    public static void registerModification(Supplier<EntityType<? extends LivingEntity>> typeSupplier, Supplier<Attribute> attributeSupplier, Double value) {
        throw new AssertionError();
    }

    public static void registerModification(Supplier<EntityType<? extends LivingEntity>> typeSupplier, Supplier<Attribute> attributeSupplier) {
        registerModification(typeSupplier, attributeSupplier, null);
    }

}
