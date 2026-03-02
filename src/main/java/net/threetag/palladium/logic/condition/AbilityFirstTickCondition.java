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

public record AbilityFirstTickCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityFirstTickCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityFirstTickCondition::ability)
            ).apply(instance, AbilityFirstTickCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityFirstTickCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityFirstTickCondition::ability, AbilityFirstTickCondition::new
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
            return dependency.getEnabledTicks() == 1;
        }
    }

    @Override
    public ConditionSerializer<AbilityFirstTickCondition> getSerializer() {
        return ConditionSerializers.ABILITY_FIRST_TICK.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityFirstTickCondition> {

        @Override
        public MapCodec<AbilityFirstTickCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityFirstTickCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability on First Tick")
                    .setDescription("Checks if the ability is on its first tick.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that needs to be on its first tick.")
                    .addExampleObject(new AbilityFirstTickCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    )));
        }
    }
}
