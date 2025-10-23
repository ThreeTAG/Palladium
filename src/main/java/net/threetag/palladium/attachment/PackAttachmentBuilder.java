package net.threetag.palladium.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class PackAttachmentBuilder<T> {

    public static final Codec<PackAttachmentBuilder<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PackAttachmentCodecs.TYPE_CODEC.fieldOf("type").forGetter(PackAttachmentBuilder::codec),
            SyncWith.CODEC.fieldOf("sync_with").forGetter(PackAttachmentBuilder::syncWith),
            Codec.BOOL.optionalFieldOf("copy_on_death", true).forGetter(PackAttachmentBuilder::copyOnDeath)
    ).apply(instance, PackAttachmentBuilder::new));

    private final PackAttachmentCodecs.Entry<T> codec;
    private final SyncWith syncWith;
    private final boolean copyOnDeath;
    private AttachmentType<T> type;

    public PackAttachmentBuilder(PackAttachmentCodecs.Entry<T> codec, SyncWith syncWith,
                                 boolean copyOnDeath) {
        this.codec = codec;
        this.syncWith = syncWith;
        this.copyOnDeath = copyOnDeath;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public AttachmentType<T> build(ResourceLocation id) {
        Codec<T> codec = this.codec.codec();
        Supplier<T> defaultSupplier = this.codec.defaultSupplier();
        AttachmentType.Builder<T> builder = AttachmentType.builder(defaultSupplier).serialize(codec.fieldOf(id.toString()));

        if (this.syncWith != SyncWith.NONE) {
            StreamCodec streamCodec = this.codec.streamCodec();
            builder.sync((holder, player) -> holder == player || this.syncWith() == SyncWith.ALL, streamCodec);
        }

        if (this.copyOnDeath) {
            builder.copyOnDeath();
        }

        return this.type = builder.build();
    }

    public AttachmentType<T> getType() {
        return type;
    }

    public PackAttachmentCodecs.Entry<T> codec() {
        return codec;
    }

    public SyncWith syncWith() {
        return syncWith;
    }

    public boolean copyOnDeath() {
        return copyOnDeath;
    }

    public enum SyncWith implements StringRepresentable {

        NONE("none"),
        SELF("self"),
        ALL("all");

        public static final Codec<SyncWith> CODEC = StringRepresentable.fromEnum(SyncWith::values);

        private final String name;

        SyncWith(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
