package net.threetag.palladium.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlatformAttachmentType<T> {

    public static final Codec<PlatformAttachmentType<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlatformAttachmentCodecs.TYPE_CODEC.fieldOf("type").forGetter(t -> t.codec),
            SyncWith.CODEC.fieldOf("sync_with").forGetter(t -> t.syncWith),
            Codec.BOOL.optionalFieldOf("copy_on_death", true).forGetter(t -> t.copyOnDeath)
    ).apply(instance, PlatformAttachmentType::new));

    private final PlatformAttachmentCodecs.Entry<T> codec;
    private final SyncWith syncWith;
    private final boolean copyOnDeath;
    private Object platformObject;

    public PlatformAttachmentType(PlatformAttachmentCodecs.Entry<T> codec, SyncWith syncWith, boolean copyOnDeath) {
        this.codec = codec;
        this.syncWith = syncWith;
        this.copyOnDeath = copyOnDeath;
    }

    public void setPlatformObject(Object platformAttachmentType) {
        this.platformObject = platformAttachmentType;
    }

    public Object getPlatformObject() {
        return this.platformObject;
    }

    public Codec<T> getCodec() {
        return this.codec.codec();
    }

    public StreamCodec<? extends ByteBuf, T> getStreamCodec() {
        return this.codec.streamCodec();
    }

    public Supplier<T> getDefaultSupplier() {
        return this.codec.defaultSupplier();
    }

    public SyncWith isSyncedWith() {
        return this.syncWith;
    }

    public boolean copiesOnDeath() {
        return copyOnDeath;
    }

    public T get(Entity entity) {
        return get(entity, this);
    }

    public void set(Entity entity, T value) {
        set(entity, this, value);
    }

    @ExpectPlatform
    public static <T> void set(Entity entity, PlatformAttachmentType<T> type, @Nullable T value) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> T get(Entity entity, PlatformAttachmentType<T> type) {
        throw new AssertionError();
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
