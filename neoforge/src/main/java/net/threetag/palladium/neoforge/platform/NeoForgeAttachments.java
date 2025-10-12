package net.threetag.palladium.neoforge.platform;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import net.threetag.palladium.platform.AttachmentService;
import net.threetag.palladium.platform.PlatformHelper;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NeoForgeAttachments implements AttachmentService {

    @Override
    public void register(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments) {
        dataAttachments.forEach((id, type) -> {
            Codec<Object> codec = (Codec<Object>) type.getCodec();
            Supplier defaultSupplier = type.getDefaultSupplier();
            net.neoforged.neoforge.attachment.AttachmentType.Builder builder = net.neoforged.neoforge.attachment.AttachmentType.builder(defaultSupplier).serialize(codec.fieldOf(id.toString()));

            if (type.isSyncedWith() != PlatformAttachmentType.SyncWith.NONE) {
                StreamCodec streamCodec = type.getStreamCodec();
                builder.sync((iAttachmentHolder, player) -> iAttachmentHolder == player || type.isSyncedWith() == PlatformAttachmentType.SyncWith.ALL, streamCodec);
            }

            if (type.copiesOnDeath()) {
                builder.copyOnDeath();
            }

            var attachmentType = builder.build();
            type.setPlatformObject(attachmentType);
            PlatformHelper.PLATFORM.getRegistries().register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, id, attachmentType);
        });
    }

    public static <T> net.neoforged.neoforge.attachment.AttachmentType<T> getNeoForgeType(PlatformAttachmentType<T> type) {
        if (type.getPlatformObject() instanceof net.neoforged.neoforge.attachment.AttachmentType<?> neoType) {
            return (net.neoforged.neoforge.attachment.AttachmentType<T>) neoType;
        } else {
            throw new IllegalStateException("AttachmentType is not registered on the platform");
        }
    }

    @Override
    public <T> void set(Entity entity, PlatformAttachmentType<T> type, T value) {
        if (value == null) {
            entity.removeData(getNeoForgeType(type));
        } else {
            entity.setData(getNeoForgeType(type), value);
        }
    }

    @Override
    public <T> T get(Entity entity, PlatformAttachmentType<T> type) {
        return entity.getData(getNeoForgeType(type));
    }
}
