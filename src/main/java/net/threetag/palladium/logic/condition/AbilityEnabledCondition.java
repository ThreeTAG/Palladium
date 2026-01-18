package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;

public record AbilityEnabledCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityEnabledCondition::ability)
            ).apply(instance, AbilityEnabledCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityEnabledCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityEnabledCondition::ability, AbilityEnabledCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);
        return dependency != null && dependency.isEnabled();
    }

    @Override
    public ConditionSerializer<AbilityEnabledCondition> getSerializer() {
        return ConditionSerializers.ABILITY_ENABLED.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityEnabledCondition> {

        @Override
        public MapCodec<AbilityEnabledCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityEnabledCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityEnabledCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability enabled")
                    .setDescription("Checks if the ability is enabled.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that needs to be enabled")
                    .addExampleObject(new AbilityEnabledCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    )));
        }
    }
}
