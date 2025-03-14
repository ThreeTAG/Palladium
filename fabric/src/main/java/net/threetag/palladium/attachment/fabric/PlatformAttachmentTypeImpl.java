package net.threetag.palladium.attachment.fabric;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.attachment.PlatformAttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class PlatformAttachmentTypeImpl {

    @SuppressWarnings("unchecked")
    public static <T> AttachmentType<T> getFabricType(PlatformAttachmentType<T> type) {
        if (type.getPlatformObject() instanceof AttachmentType<?> fabricType) {
            return (AttachmentType<T>) fabricType;
        } else {
            throw new IllegalStateException("AttachmentType is not registered on the platform");
        }
    }

    public static <T> void set(Entity entity, PlatformAttachmentType<T> type, T value) {
        entity.setAttached(getFabricType(type), value);
    }

    public static <T> T get(Entity entity, PlatformAttachmentType<T> type) {
        return entity.getAttached(getFabricType(type));
    }

}
