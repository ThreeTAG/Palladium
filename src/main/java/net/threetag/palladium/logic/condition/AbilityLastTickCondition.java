package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.logic.context.DataContext;

public record AbilityLastTickCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityLastTickCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityLastTickCondition::ability)
            ).apply(instance, AbilityLastTickCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityLastTickCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityLastTickCondition::ability, AbilityLastTickCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerInstance();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);

        if (dependency == null) {
            return false;
        } else {
            return dependency.getPrevEnabledTicks() > dependency.getEnabledTicks();
        }
    }

    @Override
    public ConditionSerializer<AbilityLastTickCondition> getSerializer() {
        return ConditionSerializers.ABILITY_LAST_TICK.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityLastTickCondition> {

        @Override
        public MapCodec<AbilityLastTickCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityLastTickCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityLastTickCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability on Last Tick")
                    .setDescription("Checks if the ability was just on its last tick.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that needs to be on its last tick.")
                    .addExampleObject(new AbilityLastTickCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    )));
        }
    }
}
