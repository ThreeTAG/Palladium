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

public class FloatDataAttachmentValue extends FloatValue {

    public static final MapCodec<FloatDataAttachmentValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES).fieldOf("attachment").forGetter(v -> v.attachmentId),
            Codec.FLOAT.optionalFieldOf("fallback", 0F).forGetter(v -> v.fallback),
            modifyFunctionCodec()
    ).apply(instance, FloatDataAttachmentValue::new));

    private final ResourceKey<AttachmentType<?>> attachmentId;
    private final float fallback;

    public FloatDataAttachmentValue(ResourceKey<AttachmentType<?>> attachmentId, float fallback, String molang) {
        super(molang);
        this.attachmentId = attachmentId;
        this.fallback = fallback;
    }

    @Override
    public float getFloat(DataContext context) {
        var entity = context.getEntity();
        var attachmentType = NeoForgeRegistries.ATTACHMENT_TYPES.get(this.attachmentId).orElse(null);

        if (entity == null || attachmentType == null) {
            return this.fallback;
        }

        var data = entity.getExistingDataOrNull(attachmentType.value());
        return data instanceof Number n ? n.floatValue() : this.fallback;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.FLOAT_DATA_ATTACHMENT.get();
    }

    public static class Serializer extends FloatSerializer<FloatDataAttachmentValue> {

        @Override
        public MapCodec<FloatDataAttachmentValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, FloatDataAttachmentValue> builder, HolderLookup.Provider provider) {
            builder.setName("Float Data Attachment").setDescription("Returns the value of a data attachment, which must be a float.")
                    .add("attachment", TYPE_ATTACHMENT_TYPE, "The ID of the data attachment.")
                    .addOptional("fallback", TYPE_FLOAT, "If the data attachment doesn't exist or is not existent in the entity, this fallback value will be used.")
                    .addExampleObject(new FloatDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "float_attachment_id")), 0F, ""))
                    .addExampleObject(new FloatDataAttachmentValue(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath("example", "float_attachment_id")), 4.2F, "this * 2"));
        }
    }
}
