package net.threetag.palladium.attachment.neoforge;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.threetag.palladium.addonpack.DataAttachmentLoader;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
public class PlatformAttachmentTypeImpl {

    public static void initEvents() {
        NeoForge.EVENT_BUS.addListener(PlayerEvent.Clone.class, event -> {
            if (event.isWasDeath()) {
                DataAttachmentLoader.INSTANCE.all().forEach((id, type) -> {
                    if (event.getOriginal().hasData(getNeoForgeType(type))) {
                        PlatformAttachmentType t = type;
                        set(event.getEntity(), t, get(event.getOriginal(), t));
                    }
                });
            }
        });
    }

    public static <T> AttachmentType<T> getNeoForgeType(PlatformAttachmentType<T> type) {
        if (type.getPlatformObject() instanceof AttachmentType<?> neoType) {
            return (AttachmentType<T>) neoType;
        } else {
            throw new IllegalStateException("AttachmentType is not registered on the platform");
        }
    }

    public static <T> void set(Entity entity, PlatformAttachmentType<T> type, @Nullable T value) {
        if (value == null) {
            entity.removeData(getNeoForgeType(type));
        } else {
            entity.setData(getNeoForgeType(type), value);
        }
    }

    public static <T> T get(Entity entity, PlatformAttachmentType<T> type) {
        return entity.getData(getNeoForgeType(type));
    }

}
