package net.threetag.palladium.power;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.util.LegacySupportJsonReloadListener;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SuitSetPowerManager extends LegacySupportJsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static SuitSetPowerManager INSTANCE;

    private final Map<SuitSet, List<Power>> suitSetPowers = new HashMap<>();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("suit_set_powers"), INSTANCE = new SuitSetPowerManager());
    }

    public SuitSetPowerManager() {
        super(GSON, "palladium/suit_set_powers", "suit_set_powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.suitSetPowers.clear();
        object.forEach((id, json) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "$");

                List<Power> powers = new ArrayList<>();
                if (jsonObject.get("power").isJsonPrimitive()) {
                    var power = PowerManager.getInstance(null).getPower(new ResourceLocation(jsonObject.get("power").getAsString()));

                    if(power == null) {
                        AddonPackLog.warning("Unknown power used for suit set '" + jsonObject.get("power").getAsString() + "'");
                    } else {
                        powers.add(power);
                    }
                } else if (jsonObject.get("power").isJsonArray()) {
                    for (JsonElement jsonElement : GsonHelper.getAsJsonArray(jsonObject, "power")) {
                        var power = PowerManager.getInstance(null).getPower(new ResourceLocation(jsonElement.getAsString()));

                        if(power == null) {
                            AddonPackLog.warning("Unknown power used for suit set '" + jsonElement.getAsString() + "'");
                        } else {
                            powers.add(power);
                        }
                    }
                } else {
                    throw new JsonSyntaxException("Expected power to be string or array of strings");
                }

                List<SuitSet> suitSets = new ArrayList<>();
                if (jsonObject.get("suit_set").isJsonPrimitive()) {
                    ResourceLocation suitSetId = new ResourceLocation(jsonObject.get("suit_set").getAsString());

                    if (!SuitSet.REGISTRY.containsKey(suitSetId)) {
                        throw new JsonParseException("Unknown suit set '" + suitSetId + "'");
                    }

                    suitSets = List.of(Objects.requireNonNull(SuitSet.REGISTRY.get(suitSetId)));
                } else if (jsonObject.get("suit_set").isJsonArray()) {
                    for (JsonElement jsonElement : GsonHelper.getAsJsonArray(jsonObject, "suit_set")) {
                        ResourceLocation suitSetId = new ResourceLocation(jsonElement.getAsString());

                        if (!SuitSet.REGISTRY.containsKey(suitSetId)) {
                            throw new JsonParseException("Unknown suit set '" + suitSetId + "'");
                        }

                        suitSets.add(SuitSet.REGISTRY.get(suitSetId));
                    }
                } else {
                    throw new JsonSyntaxException("Expected suit set to be string or array of strings");
                }

                for (SuitSet suitSet : suitSets) {
                    this.suitSetPowers.computeIfAbsent(suitSet, suitSet1 -> new ArrayList<>()).addAll(powers);
                }
            } catch (Exception exception) {
                AddonPackLog.error("Parsing error loading suit set powers {}", id, exception);
            }
        });
        AddonPackLog.info("Loaded {} suit set powers", this.suitSetPowers.size());
    }

    @Nullable
    public List<Power> getPowerForSuitSet(SuitSet suitSet) {
        return this.suitSetPowers.get(suitSet);
    }

    public static SuitSetPowerManager getInstance() {
        return INSTANCE;
    }
}
