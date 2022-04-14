package net.threetag.palladium.entity.fabric;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.threetag.palladium.mixin.fabric.AttributeSupplierBuilderMixin;
import net.threetag.palladium.mixin.fabric.AttributeSupplierMixin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityAttributeModificationRegistryImpl {

    private static Map<EntityType<? extends LivingEntity>, AttributeSupplier> MODIFICATIONS;
    private static final Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> MODIFICATIONS_BUILDER = new HashMap<>();

    public static void registerModification(Supplier<EntityType<? extends LivingEntity>> typeSupplier, Supplier<Attribute> attributeSupplier, Double value) {
        AttributeSupplier.Builder attributes = MODIFICATIONS_BUILDER.computeIfAbsent(typeSupplier.get(),
                (type) -> new AttributeSupplier.Builder());
        attributes.add(attributeSupplier.get(), value == null ? attributeSupplier.get().getDefaultValue() : value);
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier> getAttributesView() {
        if (MODIFICATIONS == null) {
            MODIFICATIONS = new HashMap<>();
            MODIFICATIONS_BUILDER.forEach((k, v) ->
            {
                AttributeSupplier supplier = DefaultAttributes.getSupplier(k);
                AttributeSupplier.Builder newBuilder = new AttributeSupplier.Builder();
                if (supplier != null) {
                    ((AttributeSupplierBuilderMixin) newBuilder).getBuilder().putAll(((AttributeSupplierMixin) supplier).getInstances());
                }
                ((AttributeSupplierBuilderMixin) newBuilder).getBuilder().putAll(((AttributeSupplierBuilderMixin) v).getBuilder());
                MODIFICATIONS.put(k, newBuilder.build());
            });
        }
        return Collections.unmodifiableMap(MODIFICATIONS);
    }
}
