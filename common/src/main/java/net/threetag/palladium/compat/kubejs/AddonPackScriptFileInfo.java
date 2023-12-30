package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptPackInfo;
import dev.latvian.mods.kubejs.script.ScriptSource;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.threetag.palladium.mixin.ScriptFileInfoMixin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class AddonPackScriptFileInfo extends ScriptFileInfo {

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("^(\\w+)\\s*[:=]?\\s*(\\w+)$");
    public final Supplier<InputStream> inputStreamSupplier;

    public AddonPackScriptFileInfo(ScriptPackInfo p, String f, Supplier<InputStream> inputStreamSupplier) {
        super(p, f);
        this.inputStreamSupplier = inputStreamSupplier;
    }

    @Override
    public void preload(ScriptSource source) {
        ScriptFileInfoMixin mixin = (ScriptFileInfoMixin) this;
        mixin.getProperties().clear();
        mixin.setPriority(0);
        mixin.setIgnored(false);
        this.lines = UtilsJS.EMPTY_STRING_ARRAY;

        try (var reader = new BufferedReader(new InputStreamReader(this.inputStreamSupplier.get(), StandardCharsets.UTF_8))) {
            List<String> linesList = new ArrayList<>();
            String tline;

            while ((tline = reader.readLine()) != null) {
                tline = tline.trim();

                if (tline.isEmpty() || tline.startsWith("import ")) {
                    tline = "";
                } else if (tline.startsWith("//")) {
                    var matcher = PROPERTY_PATTERN.matcher(tline.substring(2).trim());

                    if (matcher.find()) {
                        mixin.getProperties().computeIfAbsent(matcher.group(1).trim(), k -> new ArrayList<>()).add(matcher.group(2).trim());
                    }

                    tline = "";
                }

                linesList.add(tline);
            }

            this.lines = linesList.toArray(UtilsJS.EMPTY_STRING_ARRAY);

            mixin.setPriority(Integer.parseInt(getProperty("priority", "0")));
            mixin.setIgnored(getProperty("ignored", "false").equals("true") || getProperty("ignore", "false").equals("true"));
            mixin.setPackMode(getProperty("packmode", ""));
            mixin.getRequiredMods().addAll(getProperties("requires"));
        } catch (Throwable ignored) {

        }
    }
}
