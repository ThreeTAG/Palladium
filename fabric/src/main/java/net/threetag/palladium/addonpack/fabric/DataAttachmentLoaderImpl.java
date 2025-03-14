package net.threetag.palladium.addonpack.fabric;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.attachment.PlatformAttachmentType;

import java.util.Map;
import java.util.function.Supplier;

public class DataAttachmentLoaderImpl {

    @SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
    public static void registerOnPlatform(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments) {
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

}
