package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.value.StaticValue;
import net.threetag.palladium.logic.value.StringDataAttachmentValue;
import net.threetag.palladium.logic.value.Value;
import net.threetag.palladium.util.StringComparator;

public record StringComparisonCondition(Value value, StringComparator operator, Value target) implements Condition {

    public static final MapCodec<StringComparisonCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Value.CODEC.fieldOf("value").forGetter(StringComparisonCondition::value),
                    StringComparator.CODEC.fieldOf("operator").forGetter(StringComparisonCondition::operator),
                    Value.CODEC.fieldOf("target").forGetter(StringComparisonCondition::target)
            ).apply(instance, StringComparisonCondition::new)
    );

    @Override
    public boolean test(DataContext context) {
        var value = this.value.get(context);
        var target = this.target.get(context);

        if (value instanceof String s1 && target instanceof String s2) {
            return this.operator.compare(s1, s2);
        }

        return false;
    }

    @Override
    public ConditionSerializer<?> getSerializer() {
        return ConditionSerializers.STRING_COMPARISON.get();
    }

    public static class Serializer extends ConditionSerializer<StringComparisonCondition> {

        @Override
        public MapCodec<StringComparisonCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, StringComparisonCondition> builder, HolderLookup.Provider provider) {
            builder.setName("String Comparison")
                    .setDescription("Compares two string values using the given operator.")
                    .add("value", TYPE_VALUE, "The main value in the comparison")
                    .add("operator", TYPE_STRING_COMPARATOR, "The comparison operator being used")
                    .add("target", TYPE_VALUE, "The value that the main value is being compared against")
                    .addExampleObject(new StringComparisonCondition(new StaticValue("example"), StringComparator.CONTAINS, new StaticValue("amp")))
                    .addExampleObject(new StringComparisonCondition(new StringDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "string_attachment_id")), "", ""), StringComparator.EQUALS, new StaticValue("example_content")));
        }
    }
}
