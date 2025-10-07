package net.threetag.palladium.addonpack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DataAttachmentLoader extends SimpleJsonResourceReloadListener<PlatformAttachmentType<?>> {

    public static final DataAttachmentLoader INSTANCE = new DataAttachmentLoader();
    private final BiMap<ResourceLocation, PlatformAttachmentType<?>> dataAttachments = HashBiMap.create();

    public DataAttachmentLoader() {
        super(PlatformAttachmentType.CODEC, FileToIdConverter.json("data_attachment"));
    }

    @Override
    protected void apply(Map<ResourceLocation, PlatformAttachmentType<?>> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.dataAttachments.clear();
        this.dataAttachments.putAll(object);
        AddonPackLog.info("Registered " + object.size() + " addonpack data attachments");
        registerOnPlatform(this.dataAttachments);
    }

    @Nullable
    public PlatformAttachmentType<?> get(ResourceLocation id) {
        return this.dataAttachments.get(id);
    }

    @Nullable
    public ResourceLocation getId(PlatformAttachmentType<?> type) {
        return this.dataAttachments.inverse().get(type);
    }

    public Map<ResourceLocation, PlatformAttachmentType<?>> all() {
        return ImmutableMap.copyOf(this.dataAttachments);
    }

    @ExpectPlatform
    public static void registerOnPlatform(Map<ResourceLocation, PlatformAttachmentType<?>> dataAttachments) {
        throw new AssertionError();
    }
}
