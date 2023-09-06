package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.script.ScriptFile;
import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptPack;
import dev.latvian.mods.kubejs.script.ScriptSource;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AddonPackScriptFile extends ScriptFile {

    public AddonPackScriptFile(ScriptPack p, ScriptFileInfo i, ScriptSource s) {
        super(p, i, s);
    }

    @Override
    public void load() throws IOException {
        AddonPackScriptFileInfo fileInfo = (AddonPackScriptFileInfo) this.info;
        var stream = fileInfo.inputStreamSupplier.get();
        var script = new String(IOUtils.toByteArray(new BufferedInputStream(stream)), StandardCharsets.UTF_8);
        pack.manager.context.evaluateString(pack.scope, script, info.location, 1, null);
    }
}
