package net.threetag.palladium.fabric.platform;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import net.threetag.palladium.platform.AttachmentService;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
public class FabricAttachments implements AttachmentService {

    @Override
    public void register(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments) {
        dataAttachments.forEach((id, type) -> {
            type.setPlatformObject(AttachmentRegistry.create(id, builder -> {
                Codec<Object> codec = (Codec<Object>) type.getCodec();
                Supplier defaultSupplier = type.getDefaultSupplier();
                builder.initializer(defaultSupplier).persistent(codec);

                if (type.isSyncedWith() != PlatformAttachmentType.SyncWith.NONE) {
                    StreamCodec streamCodec = type.getStreamCodec();
                    builder.syncWith(streamCodec, type.isSyncedWith() == PlatformAttachmentType.SyncWith.SELF ?
                            AttachmentSyncPredicate.targetOnly() : AttachmentSyncPredicate.all());
                }

                if (type.copiesOnDeath()) {
                    builder.copyOnDeath();
                }
            }));
        });
    }

    public <T> AttachmentType<T> getFabricType(PlatformAttachmentType<T> type) {
        if (type.getPlatformObject() instanceof AttachmentType<?> fabricType) {
            return (AttachmentType<T>) fabricType;
        } else {
            throw new IllegalStateException("AttachmentType is not registered on the platform");
        }
    }

    @Override
    public <T> void set(Entity entity, PlatformAttachmentType<T> type, T value) {
        entity.setAttached(getFabricType(type), value);
    }

    @Override
    public <T> T get(Entity entity, PlatformAttachmentType<T> type) {
        return entity.getAttached(getFabricType(type));
    }
}
