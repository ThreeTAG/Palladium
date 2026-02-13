package net.threetag.palladium.client.gui.ui.layout;

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
import java.util.Set;

public class UiLayoutManager extends SimpleJsonResourceReloadListener<UiLayout> {

    public static final Identifier ID = Palladium.id("screens");
    public static final UiLayoutManager INSTANCE = new UiLayoutManager();

    private final BiMap<Identifier, UiLayout> byName = HashBiMap.create();

    public UiLayoutManager() {
        super(UiLayout.Codecs.CODEC, FileToIdConverter.json("palladium/screens"));
    }

    @Override
    protected void apply(Map<Identifier, UiLayout> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " screens");
    }

    public UiLayout get(Identifier id) {
        return this.byName.get(id);
    }

    public Set<Identifier> getIds() {
        return this.byName.keySet();
    }
}
