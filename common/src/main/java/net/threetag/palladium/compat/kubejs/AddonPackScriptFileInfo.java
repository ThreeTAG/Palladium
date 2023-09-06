package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptPackInfo;
import dev.latvian.mods.kubejs.script.ScriptSource;
import net.threetag.palladium.mixin.ScriptFileInfoMixin;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class AddonPackScriptFileInfo extends ScriptFileInfo {

    public final Supplier<InputStream> inputStreamSupplier;

    public AddonPackScriptFileInfo(ScriptPackInfo p, String f, Supplier<InputStream> inputStreamSupplier) {
        super(p, f);
        this.inputStreamSupplier = inputStreamSupplier;
    }

    @Override
    public @Nullable Throwable preload(ScriptSource source) {
        ScriptFileInfoMixin mixin = (ScriptFileInfoMixin) this;
        mixin.getProperties().clear();
        mixin.setPriority(0);
        mixin.setIgnored(false);

        try (var reader = new BufferedReader(new InputStreamReader(this.inputStreamSupplier.get(), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("//")) {
                    var s = line.substring(2).split(":", 2);

                    if (s.length == 2) {
                        mixin.getProperties().put(s[0].trim().toLowerCase(), s[1].trim());
                    }
                } else {
                    break;
                }
            }

            mixin.setPriority(Integer.parseInt(getProperty("priority", "0")));
            mixin.setIgnored(getProperty("ignored", "false").equals("true") || getProperty("ignore", "false").equals("true"));
            mixin.setPackMode(getProperty("packmode", "default"));
            return null;
        } catch (Throwable ex) {
            return ex;
        }
    }
}
