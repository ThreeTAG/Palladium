package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.value.MoonPhaseValue;
import net.threetag.palladium.logic.value.StaticValue;
import net.threetag.palladium.logic.value.Value;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeModifierAbility extends Ability {

    public static final MapCodec<AttributeModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    NeoForgeExtraCodecs.singularOrPluralCodec(DynamicModifier.CODEC, "modifier").forGetter(ab -> ab.modifiers),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, AttributeModifierAbility::new));

    public final Set<DynamicModifier> modifiers;

    public AttributeModifierAbility(Set<DynamicModifier> modifiers, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.modifiers = modifiers;
    }

    @Override
    public AbilitySerializer<AttributeModifierAbility> getSerializer() {
        return AbilitySerializers.ATTRIBUTE_MODIFIER.get();
    }

    public static Identifier getModifierId(AbilityInstance<AttributeModifierAbility> abilityInstance, DynamicModifier modifier, int index) {
        return modifier.id().orElseGet(() -> {
            var ref = abilityInstance.getReference();
            return Identifier.fromNamespaceAndPath(Objects.requireNonNull(ref.powerId()).getNamespace(), ref.powerId().getPath() + "_" + ref.abilityKey() + "_" + index);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean tick(LivingEntity entity, AbilityInstance<?> ability, boolean enabled) {
        if (!entity.level().isClientSide()) {
            int i = 0;
            for (DynamicModifier dynamicModifier : this.modifiers) {
                AttributeInstance attributeInstance = entity.getAttribute(dynamicModifier.attribute());

                if (attributeInstance == null) {
                    continue;
                }

                var id = getModifierId((AbilityInstance<AttributeModifierAbility>) ability, dynamicModifier, i);
                AttributeModifier modifier = attributeInstance.getModifier(id);

                // Remove modifier if amount or operation don't match
                float scale = ability.getAnimationTimerProgressEased(1F);
                double amount = dynamicModifier.amount().getAsDouble(DataContext.forAbility(entity, ability)) * scale;
                if (scale <= 0.0F || (modifier != null && (modifier.amount() != amount || modifier.operation() != dynamicModifier.operation()))) {
                    attributeInstance.removeModifier(id);
                    modifier = null;
                }

                if (modifier == null && amount > 0.0F) {
                    modifier = new AttributeModifier(id, amount, dynamicModifier.operation());
                    attributeInstance.addTransientModifier(modifier);
                }

                i++;
            }
        }

        return super.tick(entity, ability, enabled);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> instance) {
        int i = 0;
        for (DynamicModifier dynamicModifier : this.modifiers) {
            var attributeInstance = entity.getAttribute(dynamicModifier.attribute());
            var id = getModifierId((AbilityInstance<AttributeModifierAbility>) instance, dynamicModifier, i);

            if (attributeInstance != null && attributeInstance.getModifier(id) != null) {
                attributeInstance.removeModifier(id);
            }

            i++;
        }
    }

    public static String getAttributeList() {
        return BuiltInRegistries.ATTRIBUTE.keySet().stream().map(Identifier::toString).sorted(Comparator.naturalOrder()).collect(Collectors.joining(", "));
    }

    public record DynamicModifier(Holder<Attribute> attribute, Optional<Identifier> id, Value amount,
                                  AttributeModifier.Operation operation) {
        public static final Codec<DynamicModifier> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                Attribute.CODEC.fieldOf("attribute").forGetter(DynamicModifier::attribute),
                                Identifier.CODEC.optionalFieldOf("id").forGetter(DynamicModifier::id),
                                Value.CODEC.fieldOf("amount").forGetter(DynamicModifier::amount),
                                AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(DynamicModifier::operation)
                        )
                        .apply(instance, DynamicModifier::new)
        );
    }

    public static class Serializer extends AbilitySerializer<AttributeModifierAbility> {

        @Override
        public MapCodec<AttributeModifierAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, AttributeModifierAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Adds an attribute modifier to the entity while the ability is enabled.")
                    .add("modifier(s)", TYPE_ATTRIBUTE_MODIFIER, "The attribute modifier(s) that will be applied")
                    .addExampleObject(new AttributeModifierAbility(new HashSet<>(List.of(
                            new DynamicModifier(Attributes.ARMOR, Optional.empty(), new StaticValue(1D), AttributeModifier.Operation.ADD_VALUE)
                    )), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()))
                    .addExampleObject(new AttributeModifierAbility(new HashSet<>(Arrays.asList(
                            new DynamicModifier(Attributes.MOVEMENT_SPEED, Optional.empty(), new StaticValue(5D), AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new DynamicModifier(Attributes.JUMP_STRENGTH, Optional.empty(), new MoonPhaseValue(""), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    )), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
