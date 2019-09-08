package com.threetag.threecore.util.scripts;

import com.google.common.collect.Maps;
import com.threetag.threecore.ThreeCore;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class ScriptManager implements IResourceManagerReloadListener {

    public static ScriptManager INSTANCE;
    public static final int resourcePrefix = "scripts/".length();
    public static final int resourceSuffix = ".js".length();
    public static ScriptEngineManager manager = new ScriptEngineManager();
    private Map<String, ScriptEngine> engines = Maps.newHashMap();

    public ScriptManager() {
        INSTANCE = this;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.engines.clear();

        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("scripts", (name) -> name.endsWith(".js"))) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));
            try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                ScriptEngine engine = getEngine(resourcelocation1.getNamespace());
                InputStreamReader reader = new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8);
                engine.eval(reader);
                IOUtils.closeQuietly(reader);
                ThreeCore.LOGGER.info("Executed script file {}!", resourcelocation1);
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't execute script file {} from {}", resourcelocation1, resourcelocation, throwable);
            }
        }
    }

    public ScriptEngine getEngine(String namespace) {
        if (this.engines.containsKey(namespace))
            return this.engines.get(namespace);
        else {
            ScriptEngine engine = manager.getEngineByName("javascript");
            if (!(engine instanceof Invocable)) {
                ThreeCore.LOGGER.error("Engine is not invocable?");
                return null;
            }
            engine.put("namespace", namespace);
            this.engines.put(namespace, engine);
            return engine;
        }
    }

    public Object invoke(String function, Object... args) {
        String[] strings = function.split(":", 2);

        if (strings.length < 2)
            return null;

        try {

            return ((Invocable) getEngine(strings[0])).invokeFunction(strings[1], args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent e) {
        e.getServer().getResourceManager().addReloadListener(new ScriptManager());
    }

}
