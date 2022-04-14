package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.script.ScriptFile;
import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptPack;
import dev.latvian.mods.kubejs.script.ScriptSource;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;

public class AddonPackScriptFile extends ScriptFile {

    private Throwable error;

    public AddonPackScriptFile(ScriptPack p, ScriptFileInfo i, ScriptSource s) {
        super(p, i, s);
    }

    @Override
    @Nullable
    public Throwable getError() {
        return error;
    }

    @Override
    public boolean load() {
        error = null;
        AddonPackScriptFileInfo fileInfo = (AddonPackScriptFileInfo) this.info;

        try (var stream = fileInfo.inputStreamSupplier.get()) {
            var script = new String(IOUtils.toByteArray(new BufferedInputStream(stream)), StandardCharsets.UTF_8);
            pack.context.evaluateString(pack.scope, script, info.location, 1, null);
            return true;
        } catch (Throwable ex) {
            error = ex;
            return false;
        }
    }
}
