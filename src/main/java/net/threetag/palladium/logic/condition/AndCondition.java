package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Arrays;
import java.util.List;

public record AndCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<AndCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Condition.FALSE_TRUE_WRAPPED_CODEC.listOf().fieldOf("conditions").forGetter(AndCondition::conditions)
            ).apply(instance, AndCondition::new)
    );

    @Override
    public boolean test(DataContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.test(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<AndCondition> getSerializer() {
        return ConditionSerializers.AND.get();
    }

    public static class Serializer extends ConditionSerializer<AndCondition> {

        @Override
        public MapCodec<AndCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AndCondition> builder, HolderLookup.Provider provider) {
            builder.setName("AND")
                    .setDescription("Allows you to group multiple conditions into one using the AND logic. All of the given conditions must be true for this one to be true aswell.")
                    .add("conditions", TYPE_CONDITION_LIST, "List of conditions")
                    .addExampleObject(new AndCondition(Arrays.asList(new CrouchingCondition(), new DayCondition())));
        }
    }
}
