package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public record NumberDataAttachmentCondition(ResourceKey<AttachmentType<?>> attachmentId, double fallback, double min,
                                            double max) implements Condition {

    public static final MapCodec<NumberDataAttachmentCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    ResourceKey.codec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES).fieldOf("attachment").forGetter(NumberDataAttachmentCondition::attachmentId),
                    Codec.DOUBLE.optionalFieldOf("fallback", 0D).forGetter(NumberDataAttachmentCondition::fallback),
                    Codec.DOUBLE.optionalFieldOf("min", Double.MIN_VALUE).forGetter(NumberDataAttachmentCondition::min),
                    Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE).forGetter(NumberDataAttachmentCondition::max)
            ).apply(instance, NumberDataAttachmentCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, NumberDataAttachmentCondition> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES), NumberDataAttachmentCondition::attachmentId,
            ByteBufCodecs.DOUBLE, NumberDataAttachmentCondition::fallback,
            ByteBufCodecs.DOUBLE, NumberDataAttachmentCondition::min,
            ByteBufCodecs.DOUBLE, NumberDataAttachmentCondition::max,
            NumberDataAttachmentCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getEntity();
        var attachmentType = NeoForgeRegistries.ATTACHMENT_TYPES.get(this.attachmentId).orElse(null);
        var value = this.fallback;

        if (entity != null && attachmentType != null) {
            var data = entity.getExistingDataOrNull(attachmentType.value());
            value = data instanceof Number n ? n.doubleValue() : this.fallback;
        }

        return value >= this.min && value <= this.max;
    }

    @Override
    public ConditionSerializer<NumberDataAttachmentCondition> getSerializer() {
        return ConditionSerializers.NUMBER_DATA_ATTACHMENT.get();
    }

    public static class Serializer extends ConditionSerializer<NumberDataAttachmentCondition> {

        @Override
        public MapCodec<NumberDataAttachmentCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NumberDataAttachmentCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, NumberDataAttachmentCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Number Data Attachment")
                    .setDescription("Checks if the value a number data attachment is within a given range.")
                    .add("attachment", TYPE_ATTACHMENT_TYPE, "The ID of the data attachment.")
                    .addOptional("min", TYPE_DOUBLE, "The minimum required value of the data attachment.")
                    .addOptional("max", TYPE_DOUBLE, "The maximum required value of the data attachment.")
                    .addExampleObject(new NumberDataAttachmentCondition(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "number_attachment_id")), 0, 5, 10));
        }
    }
}
