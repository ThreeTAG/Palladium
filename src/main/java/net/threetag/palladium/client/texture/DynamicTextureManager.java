package net.threetag.palladium.client.texture;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.Map;

public class DynamicTextureManager extends SimpleJsonResourceReloadListener<DynamicTexture> {

    public static final Identifier ID = Palladium.id("dynamic_textures");
    public static final DynamicTextureManager INSTANCE = new DynamicTextureManager();

    private final BiMap<Identifier, DynamicTexture> byName = HashBiMap.create();

    protected DynamicTextureManager() {
        super(DynamicTexture.Codecs.DIRECT_CODEC, FileToIdConverter.json("palladium/dynamic_textures"));
    }

    @Override
    protected void apply(Map<Identifier, DynamicTexture> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " dynamic textures");
    }

    public DynamicTexture get(Identifier id) {
        return this.byName.get(id);
    }

    public Identifier getId(DynamicTexture texture) {
        return this.byName.inverse().get(texture);
    }
}
