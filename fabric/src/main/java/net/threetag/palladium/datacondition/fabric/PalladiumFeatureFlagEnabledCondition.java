package net.threetag.palladium.datacondition.fabric;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.feature.PalladiumFeatureFlags;

import java.util.function.Predicate;

public class PalladiumFeatureFlagEnabledCondition implements Predicate<JsonObject> {

    @Override
    public boolean test(JsonObject jsonObject) {
        var type = PalladiumFeatureFlags.getFeatureFlag(GsonHelper.getAsString(jsonObject, "feature_flag"));

        if (type == null) {
            throw new JsonParseException("Unknown Palladium feature flag: " + GsonHelper.getAsString(jsonObject, "feature_flag"));
        }

        return PalladiumFeatureFlags.isEnabled(type);
    }
}
