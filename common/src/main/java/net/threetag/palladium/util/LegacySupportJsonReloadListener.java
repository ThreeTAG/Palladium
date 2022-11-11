package net.threetag.palladium.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public abstract class LegacySupportJsonReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PATH_SUFFIX = ".json";
    private static final int PATH_SUFFIX_LENGTH = ".json".length();
    private final Gson gson;
    private final String directory;
    private final String legacyDirectory;

    public LegacySupportJsonReloadListener(Gson gson, String directory, String legacyDirectory) {
        this.gson = gson;
        this.directory = directory;
        this.legacyDirectory = legacyDirectory;
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        boolean foundLegacy = false;

        for(int m = 0; m < 2; m++) {
            String path = m == 0 ? this.legacyDirectory : this.directory;
            int i = path.length() + 1;
            for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(path, (p_215600_) -> p_215600_.getPath().endsWith(PATH_SUFFIX)).entrySet()) {
                if(m == 0) {
                    foundLegacy = true;
                }
                ResourceLocation resourcelocation = entry.getKey();
                String s = resourcelocation.getPath();
                ResourceLocation id = new ResourceLocation(resourcelocation.getNamespace(), s.substring(i, s.length() - PATH_SUFFIX_LENGTH));

                try {
                    Reader reader = entry.getValue().openAsReader();

                    try {
                        JsonElement jsonelement = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
                        if (jsonelement != null) {
                            JsonElement jsonelement1 = map.put(id, jsonelement);
                            if (jsonelement1 != null) {
                                throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                            }
                        } else {
                            LOGGER.error("Couldn't load data file {} from {} as it's null or empty", id, resourcelocation);
                        }
                    } catch (Throwable throwable1) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable throwable) {
                                throwable1.addSuppressed(throwable);
                            }
                        }

                        throw throwable1;
                    }

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IllegalArgumentException | IOException | JsonParseException jsonparseexception) {
                    LOGGER.error("Couldn't parse data file {} from {}", id, resourcelocation, jsonparseexception);
                }
            }
        }

        if(foundLegacy) {
            AddonPackLog.warning("Files were found in deprecated folder: '" + this.legacyDirectory + "'. Please switch to '" + this.directory + "'!");
        }

        return map;
    }
}
