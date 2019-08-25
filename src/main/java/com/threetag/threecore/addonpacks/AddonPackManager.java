package com.threetag.threecore.addonpacks;

import com.threetag.threecore.ThreeCore;
import net.minecraft.resources.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class AddonPackManager {

    public static final File DIRECTORY = new File("addonpacks");
    private static final FileFilter FILE_FILTER = (file) -> {
        boolean isZip = file.isFile() && file.getName().endsWith(".zip");
        boolean hasMeta = file.isDirectory() && (new File(file, "pack.mcmeta")).isFile();
        return isZip || hasMeta;
    };

    @SubscribeEvent
    public static void serverStarting(FMLServerAboutToStartEvent e) {
        e.getServer().resourcePacks.addPackFinder(new AddonPackFinder());
        System.out.println("lol");
    }

    private static class AddonPackFinder implements IPackFinder {

        @Override
        public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> map, ResourcePackInfo.IFactory<T> iFactory) {
            if (!DIRECTORY.exists())
                DIRECTORY.mkdirs();

            File[] files = DIRECTORY.listFiles(FILE_FILTER);

            if (files != null) {
                for (File file : files) {
                    String name = "addonpack:" + file.getName();
                    T container = ResourcePackInfo.createResourcePack(name, false, this.createResourcePack(file), iFactory, ResourcePackInfo.Priority.TOP);
                    if (container != null) {
                        map.put(name, container);
                    }
                }
            }
        }

        private Supplier<IResourcePack> createResourcePack(File file) {
            return file.isDirectory() ? () -> new FolderPack(file) : () -> new FilePack(file);
        }
    }

}
