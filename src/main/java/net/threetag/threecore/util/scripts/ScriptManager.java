package net.threetag.threecore.util.scripts;

import com.google.common.collect.Maps;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.scripts.accessors.LivingEntityAccessor;
import net.threetag.threecore.util.scripts.accessors.ScriptAccessor;
import org.apache.commons.io.IOUtils;

import javax.script.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class ScriptManager extends ReloadListener<Map<ResourceLocation, String>> {

    public static ScriptManager INSTANCE;
    public static ScriptEngineManager manager = new ScriptEngineManager();
    private static final int JSON_EXTENSION_LENGTH = ".json".length();
    private final String folder;

    private static final String[] BLOCKED_FUNCTIONS = {
            "load",
            "loadWithNewGlobal",
            "exit",
            "quit"
    };

    public ScriptManager() {
        INSTANCE = this;
        this.folder = "scripts";
    }

    @Override
    protected Map<ResourceLocation, String> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, String> map = Maps.newHashMap();
        int i = this.folder.length() + 1;

        for (ResourceLocation resourcelocation : resourceManagerIn.getAllResourceLocations(this.folder, (s) -> {
            return s.endsWith(".js");
        })) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(i, s.length() - JSON_EXTENSION_LENGTH));

            try (
                    IResource iresource = resourceManagerIn.getResource(resourcelocation);
                    InputStream inputstream = iresource.getInputStream();
                    Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))
            ) {
                String script = IOUtils.toString(reader);
                if (script != null) {
                    String script1 = map.put(resourcelocation1, script);
                    if (script1 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + resourcelocation1);
                    }
                } else {
                    ThreeCore.LOGGER.error("Couldn't load data file {} from {} as it's null or empty", resourcelocation1, resourcelocation);
                }
            } catch (IllegalArgumentException | IOException exception) {
                ThreeCore.LOGGER.error("Couldn't parse data file {} from {}", resourcelocation1, resourcelocation, exception);
            }
        }

        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, String> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        ScriptEventManager.reset();

        for (Map.Entry<ResourceLocation, String> entry : splashList.entrySet()) {
            try {
                ScriptEngine engine = manager.getEngineByName("javascript");
                ScriptContext context = engine.getContext();
                if (!(engine instanceof Invocable)) {
                    ThreeCore.LOGGER.error("Engine is not invocable?");
                    continue;
                }

                for (String s : BLOCKED_FUNCTIONS)
                    context.removeAttribute(s, context.getAttributesScope(s));

                engine.put("namespace", entry.getKey().toString());
                engine.put("eventManager", new ScriptEventManager.EventManagerAccessor());
                engine.eval(entry.getValue());
                ThreeCore.LOGGER.info("Executed script file {}!", entry.getKey());
            } catch (ScriptException e) {
                ThreeCore.LOGGER.error("Error when executing script file {}", entry.getKey(), e);
            }
        }
    }

    @SubscribeEvent
    public static void serverAboutToStart(FMLServerAboutToStartEvent e) {
        e.getServer().getResourceManager().addReloadListener(new ScriptManager());
    }

}
