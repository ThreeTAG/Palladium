package net.threetag.palladium.mixin;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.script.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.IoSupplier;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.compat.kubejs.AddonPackScriptFileInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ScriptManager.class)
public class ScriptManagerMixin {

    @Shadow(remap = false)
    @Final
    public Map<String, ScriptPack> packs;

    @Shadow(remap = false)
    @Final
    public ScriptType scriptType;

    @Inject(at = @At("RETURN"), method = "loadFromDirectory", remap = false)
    public void loadFromDirectory(CallbackInfo ci) {
        AddonPackManager.getInstance().getPackList().reload();
        AddonPackManager.getInstance().getPackList().setSelected(AddonPackManager.getInstance().getPackList().getAvailableIds());

        Map<String, Pair<ScriptPackInfo, List<ScriptFileInfo>>> scriptFileInfoMap = new HashMap<>();

        for (Pack pack : AddonPackManager.getInstance().getPackList().getAvailablePacks()) {
            var packType = this.scriptType == ScriptType.CLIENT ? PackType.CLIENT_RESOURCES : (this.scriptType == ScriptType.SERVER ? PackType.SERVER_DATA : AddonPackManager.getPackType());
            var packResources = pack.open();

            for (String namespace : packResources.getNamespaces(packType)) {
                packResources.listResources(packType, namespace, "kubejs_scripts", (path, inputStreamIoSupplier) -> {
                    if (path.getPath().endsWith(".js")) {
                        scriptFileInfoMap.computeIfAbsent(namespace, s -> Pair.of(new ScriptPackInfo("addonpack_" + s, ""), new ArrayList<>())).getSecond().add(new AddonPackScriptFileInfo(scriptFileInfoMap.get(namespace).getFirst(), path.getPath(), () -> {
                            try {
                                var packResources1 = pack.open();
                                IoSupplier<InputStream> inputStream = packResources1.getResource(packType, path);
                                packResources1.close();
                                return inputStream.get();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }));
                    }
                });
            }

            packResources.close();
        }

        for (Map.Entry<String, Pair<ScriptPackInfo, List<ScriptFileInfo>>> e : scriptFileInfoMap.entrySet()) {
            var scriptPack = new ScriptPack((ScriptManager) (Object) this, e.getValue().getFirst());
            scriptPack.info.scripts.addAll(e.getValue().getSecond());

            for (var fileInfo : scriptPack.info.scripts) {
                try {
                    fileInfo.preload(null);
                    var skip = fileInfo.skipLoading();

                    if (skip.isEmpty()) {
                        scriptPack.scripts.add(new ScriptFile(scriptPack, fileInfo, null));
                    } else {
                        scriptType.console.info("Skipped " + fileInfo.location + ": " + skip);
                    }
                } catch (Throwable error) {
                    scriptType.console.error("Failed to pre-load script file " + fileInfo.location + ": " + error);
                }
            }

            scriptPack.scripts.sort(null);
            this.packs.put(scriptPack.info.namespace, scriptPack);
        }
    }

}
