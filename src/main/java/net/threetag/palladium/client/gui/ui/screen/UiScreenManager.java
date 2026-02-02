package net.threetag.palladium.client.gui.ui.screen;

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

public class UiScreenManager extends SimpleJsonResourceReloadListener<UiScreenConfiguration> {

    public static final Identifier ID = Palladium.id("screens");
    public static final UiScreenManager INSTANCE = new UiScreenManager();

    private final BiMap<Identifier, UiScreenConfiguration> byName = HashBiMap.create();

    public UiScreenManager() {
        super(UiScreenConfiguration.CODEC, FileToIdConverter.json("palladium/screens"));
    }

    @Override
    protected void apply(Map<Identifier, UiScreenConfiguration> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " screens");
    }

    public UiScreenConfiguration get(Identifier id) {
        return this.byName.get(id);
    }
}
