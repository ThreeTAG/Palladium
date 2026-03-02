package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class IntegerDataAttachmentValue extends IntegerValue {

    public static final MapCodec<IntegerDataAttachmentValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES).fieldOf("attachment").forGetter(v -> v.attachmentId),
            Codec.INT.optionalFieldOf("fallback", 0).forGetter(v -> v.fallback),
            modifyFunctionCodec()
    ).apply(instance, IntegerDataAttachmentValue::new));

    private final ResourceKey<AttachmentType<?>> attachmentId;
    private final int fallback;

    public IntegerDataAttachmentValue(ResourceKey<AttachmentType<?>> attachmentId, int fallback, String molang) {
        super(molang);
        this.attachmentId = attachmentId;
        this.fallback = fallback;
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getEntity();
        var attachmentType = NeoForgeRegistries.ATTACHMENT_TYPES.get(this.attachmentId).orElse(null);

        if (entity == null || attachmentType == null) {
            return this.fallback;
        }

        var data = entity.getExistingDataOrNull(attachmentType.value());
        return data instanceof Number n ? n.intValue() : this.fallback;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.INTEGER_DATA_ATTACHMENT.get();
    }

    public static class Serializer extends IntSerializer<IntegerDataAttachmentValue> {

        @Override
        public MapCodec<IntegerDataAttachmentValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, IntegerDataAttachmentValue> builder, HolderLookup.Provider provider) {
            builder.setName("Integer Data Attachment").setDescription("Returns the value of a data attachment, which must be an integer.")
                    .add("attachment", TYPE_ATTACHMENT_TYPE, "The ID of the data attachment.")
                    .addOptional("fallback", TYPE_INT, "If the data attachment doesn't exist or is not existent in the entity, this fallback value will be used.")
                    .addExampleObject(new IntegerDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "integer_attachment_id")), 0, ""))
                    .addExampleObject(new IntegerDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "integer_attachment_id")), 5, "this * 2"));
        }
    }
}
