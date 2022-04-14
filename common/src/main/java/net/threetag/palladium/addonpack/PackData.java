package net.threetag.palladium.addonpack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionInterval;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.fabricmc.loader.impl.util.version.VersionParser;
import net.minecraft.util.GsonHelper;

import java.util.*;

public class PackData {

    private final String id;
    private final Version version;
    private final Map<String, List<Dependency>> dependencies;

    public PackData(String id, Version version, Map<String, List<Dependency>> dependencies) {
        this.id = id;
        this.version = version;
        this.dependencies = dependencies;
    }

    public String getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    public static PackData fromJSON(JsonObject json) throws VersionParsingException, JsonParseException {
        JsonObject pack = GsonHelper.getAsJsonObject(json, "pack");
        String id = GsonHelper.getAsString(pack, "id");
        Version version = VersionParser.parseSemantic(GsonHelper.getAsString(pack, "version"));
        Map<String, List<Dependency>> dependenciesMap = new HashMap<>();

        if (GsonHelper.isValidNode(json, "dependencies")) {
            JsonObject dependencies = GsonHelper.getAsJsonObject(json, "dependencies");

            for (int i = 0; i < 3; i++) {
                String type = i == 0 ? "common" : (i == 1 ? "forge" : "fabric");

                if (GsonHelper.isValidNode(dependencies, type)) {
                    JsonObject typedDependency = GsonHelper.getAsJsonObject(dependencies, type);

                    for (Map.Entry<String, JsonElement> entry : typedDependency.entrySet()) {
                        List<String> matcherStringList = new ArrayList<>();

                        if (entry.getValue().isJsonPrimitive()) {
                            matcherStringList.add(entry.getValue().getAsString());
                        } else if (entry.getValue().isJsonArray()) {
                            for (JsonElement versionJson : entry.getValue().getAsJsonArray()) {
                                matcherStringList.add(versionJson.getAsString());
                            }
                        } else {
                            throw new JsonParseException("Version dependency must be either a string or an array of strings");
                        }

                        dependenciesMap.computeIfAbsent(type, s -> new ArrayList<>()).add(new Dependency(entry.getKey(), matcherStringList));
                    }
                }
            }
        }

        return new PackData(id, version, dependenciesMap);
    }

    public static class Dependency {

        private final String id;
        private final boolean isAddonPack;
        private final List<String> matcherStringList;
        private final Collection<VersionPredicate> ranges;

        public Dependency(String id, List<String> matcherStringList) throws VersionParsingException {
            if (id.startsWith("pack:")) {
                this.id = id.substring("pack:".length());
                this.isAddonPack = true;
            } else {
                this.id = id;
                this.isAddonPack = false;
            }
            this.matcherStringList = matcherStringList;
            this.ranges = VersionPredicate.parse(this.matcherStringList);
        }

        public String getId() {
            return id;
        }

        public boolean isAddonPack() {
            return isAddonPack;
        }

        public boolean matches(Version version) {
            for (VersionPredicate predicate : ranges) {
                if (predicate.test(version)) return true;
            }

            return false;
        }

        public boolean isValid() {
            if (this.isAddonPack) {
                PackData packData = AddonPackManager.getInstance().getPackData(this.id);

                if (packData == null) {
                    return false;
                }

                return this.matches(packData.getVersion());
            } else {
                Optional<Mod> mod = Platform.getOptionalMod(this.id);

                if (mod.isEmpty()) {
                    return false;
                }

                try {
                    Version version = VersionParser.parseSemantic(mod.get().getVersion());
                    return this.matches(version);
                } catch (VersionParsingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        public Collection<VersionPredicate> getVersionRequirements() {
            return ranges;
        }

        public List<VersionInterval> getVersionIntervals() {
            List<VersionInterval> ret = Collections.emptyList();

            for (VersionPredicate predicate : ranges) {
                ret = VersionInterval.or(ret, predicate.getInterval());
            }

            return ret;
        }
    }

}
