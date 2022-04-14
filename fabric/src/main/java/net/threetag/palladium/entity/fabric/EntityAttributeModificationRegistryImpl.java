package net.threetag.palladium.entity.fabric;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.mixin.fabric.AttributeSupplierBuilderMixin;
import net.threetag.palladium.mixin.fabric.AttributeSupplierMixin;

import java.util.*;
import java.util.function.Supplier;

public class EntityAttributeModificationRegistryImpl {

    private static Map<EntityType<? extends LivingEntity>, AttributeSupplier> MODIFIED = new HashMap<>();
    private static final List<Modification> MODIFICATIONS = new ArrayList<>();

    public static void registerModification(Supplier<EntityType<? extends LivingEntity>> typeSupplier, Supplier<Attribute> attributeSupplier, Double value) {
        MODIFICATIONS.add(new Modification(typeSupplier, attributeSupplier, value));
        Palladium.LOGGER.info("HALLO i registerd a modification :3");
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier> getAttributesView() {
        return Collections.unmodifiableMap(MODIFIED);
    }

    public static void modifyAttributes() {
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> builders = new HashMap<>();
        for (Modification modification : MODIFICATIONS) {
            AttributeSupplier.Builder attributes = builders.computeIfAbsent(modification.typeSupplier.get(),
                    (type) -> new AttributeSupplier.Builder());
            attributes.add(modification.attributeSupplier.get(), modification.value == null ? modification.attributeSupplier.get().getDefaultValue() : modification.value);
        }

        builders.forEach((k, v) ->
        {
            AttributeSupplier supplier = DefaultAttributes.getSupplier(k);
            AttributeSupplier.Builder newBuilder = new AttributeSupplier.Builder();
            if (supplier != null) {
                ((AttributeSupplierBuilderMixin) newBuilder).getBuilder().putAll(((AttributeSupplierMixin) supplier).getInstances());
            }
            ((AttributeSupplierBuilderMixin) newBuilder).getBuilder().putAll(((AttributeSupplierBuilderMixin) v).getBuilder());
            MODIFIED.put(k, newBuilder.build());
        });
    }

    public record Modification(Supplier<EntityType<? extends LivingEntity>> typeSupplier,
                               Supplier<Attribute> attributeSupplier,
                               Double value) {

    }
}
