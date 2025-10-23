package net.threetag.palladium.addonpack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.attachment.PackAttachmentBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class DataAttachmentLoader extends SimpleJsonResourceReloadListener<PackAttachmentBuilder<?>> {

    public static final DataAttachmentLoader INSTANCE = new DataAttachmentLoader();
    private final BiMap<ResourceLocation, PackAttachmentBuilder<?>> dataAttachments = HashBiMap.create();

    public DataAttachmentLoader() {
        super(PackAttachmentBuilder.CODEC, FileToIdConverter.json("data_attachment"));
    }

    @Override
    protected void apply(Map<ResourceLocation, PackAttachmentBuilder<?>> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.dataAttachments.clear();
        this.dataAttachments.putAll(object);
        AddonPackLog.info("Registered " + object.size() + " addonpack data attachments");
    }

    @SubscribeEvent
    static void register(RegisterEvent e) {
        INSTANCE.dataAttachments.forEach((id, builder) ->
                e.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), Objects.requireNonNull(id), () -> Objects.requireNonNull(builder).build(id))
        );
    }

    @Nullable
    public PackAttachmentBuilder<?> get(ResourceLocation id) {
        return this.dataAttachments.get(id);
    }

    @Nullable
    public ResourceLocation getId(PackAttachmentBuilder<?> type) {
        return this.dataAttachments.inverse().get(type);
    }

    public Map<ResourceLocation, PackAttachmentBuilder<?>> all() {
        return ImmutableMap.copyOf(this.dataAttachments);
    }
}
