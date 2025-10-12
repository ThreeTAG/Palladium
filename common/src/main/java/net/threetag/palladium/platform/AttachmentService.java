package net.threetag.palladium.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.attachment.PlatformAttachmentType;

import java.util.Map;

public interface AttachmentService {

    void register(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments);

    <T> void set(Entity entity, PlatformAttachmentType<T> type, T value);

    <T> T get(Entity entity, PlatformAttachmentType<T> type);

}
