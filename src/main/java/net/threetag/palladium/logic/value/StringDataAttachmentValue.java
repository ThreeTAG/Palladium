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

public class StringDataAttachmentValue extends StringValue {

    public static final MapCodec<StringDataAttachmentValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES).fieldOf("attachment").forGetter(v -> v.attachmentId),
            Codec.STRING.optionalFieldOf("fallback", "").forGetter(v -> v.fallback),
            modifyFunctionCodec()
    ).apply(instance, StringDataAttachmentValue::new));

    private final ResourceKey<AttachmentType<?>> attachmentId;
    private final String fallback;

    public StringDataAttachmentValue(ResourceKey<AttachmentType<?>> attachmentId, String fallback, String molang) {
        super(molang);
        this.attachmentId = attachmentId;
        this.fallback = fallback;
    }

    @Override
    public String getString(DataContext context) {
        var entity = context.getEntity();
        var attachmentType = NeoForgeRegistries.ATTACHMENT_TYPES.get(this.attachmentId).orElse(null);

        if (entity == null || attachmentType == null) {
            return this.fallback;
        }

        var data = entity.getExistingDataOrNull(attachmentType.value());
        return data != null ? data.toString() : this.fallback;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.STRING_DATA_ATTACHMENT.get();
    }

    public static class Serializer extends StringSerializer<StringDataAttachmentValue> {

        @Override
        public MapCodec<StringDataAttachmentValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, StringDataAttachmentValue> builder, HolderLookup.Provider provider) {
            builder.setName("String Data Attachment").setDescription("Returns the value of a data attachment.")
                    .add("attachment", TYPE_ATTACHMENT_TYPE, "The ID of the data attachment.")
                    .addOptional("fallback", TYPE_STRING, "If the data attachment doesn't exist or is not existent in the entity, this fallback value will be used.")
                    .addExampleObject(new StringDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "string_attachment_id")), "", ""))
                    .addExampleObject(new StringDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "string_attachment_id")), "example_fallback", "this * 2"));
        }
    }
}
