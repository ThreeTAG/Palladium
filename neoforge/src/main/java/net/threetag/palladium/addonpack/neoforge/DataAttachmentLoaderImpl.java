package net.threetag.palladium.addonpack.neoforge;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import net.threetag.palladium.core.registry.neoforge.SimpleRegisterImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataAttachmentLoaderImpl {

    public static final Map<AttachmentType<?>, PlatformAttachmentType<?>> TYPE_MAP = new HashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void registerOnPlatform(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments) {
        dataAttachments.forEach((id, type) -> {
            Codec<Object> codec = (Codec<Object>) type.getCodec();
            Supplier defaultSupplier = type.getDefaultSupplier();
            AttachmentType attachmentType = AttachmentType.builder(defaultSupplier).serialize(codec).build();
            type.setPlatformObject(attachmentType);
            TYPE_MAP.put(attachmentType, type);
            SimpleRegisterImpl.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, id, attachmentType);
        });
    }

}
