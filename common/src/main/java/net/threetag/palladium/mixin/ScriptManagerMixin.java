package net.threetag.palladium.mixin;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptPack;
import dev.latvian.mods.kubejs.script.ScriptPackInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.compat.kubejs.AddonPackScriptFile;
import net.threetag.palladium.compat.kubejs.AddonPackScriptFileInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Mixin(ScriptManager.class)
public class ScriptManagerMixin {

    @Shadow(remap = false)
    @Final
    public Map<String, ScriptPack> packs;

    @Shadow @Final public ScriptType scriptType;

    @Inject(at = @At("RETURN"), method = "loadFromDirectory", remap = false)
    public void loadFromDirectory(CallbackInfo ci) {
        AddonPackManager.getInstance().getPackList().reload();
        AddonPackManager.getInstance().getPackList().setSelected(AddonPackManager.getInstance().getPackList().getAvailableIds());
        for (Pack pack : AddonPackManager.getInstance().getPackList().getAvailablePacks()) {
            var packType = this.scriptType == ScriptType.CLIENT ? PackType.CLIENT_RESOURCES : (this.scriptType == ScriptType.SERVER ? PackType.SERVER_DATA : AddonPackManager.getPackType());
            var packResources = pack.open();

            for (String namespace : packResources.getNamespaces(packType)) {
                var scriptPack = new ScriptPack((ScriptManager) (Object) this, new ScriptPackInfo("addonpack_" + namespace, ""));
                for (ResourceLocation scriptId : packResources.getResources(packType, namespace, "kubejs_scripts", id -> id.getPath().endsWith(".js"))) {
                    scriptPack.info.scripts.add(new AddonPackScriptFileInfo(scriptPack.info, scriptId.getPath(), () -> {
                        try {
                            var packResources1 = pack.open();
                            InputStream inputStream = packResources1.getResource(packType, scriptId);
                            packResources1.close();
                            return inputStream;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }));
                }

                for (var fileInfo : scriptPack.info.scripts) {
                    var error = fileInfo.preload(null);

                    if (fileInfo.skipLoading()) {
                        continue;
                    }

                    if (error == null) {
                        scriptPack.scripts.add(new AddonPackScriptFile(scriptPack, fileInfo, null));
                    } else {
                        KubeJS.LOGGER.error("Failed to pre-load script file " + fileInfo.location + ": " + error);
                    }
                }

                packResources.close();
                scriptPack.scripts.sort(null);
                this.packs.put(scriptPack.info.namespace, scriptPack);
            }
        }
    }

}
