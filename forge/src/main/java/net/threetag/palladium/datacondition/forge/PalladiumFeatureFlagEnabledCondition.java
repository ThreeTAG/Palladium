package net.threetag.palladium.datacondition.forge;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.threetag.palladium.feature.PalladiumFeatureFlags;

public class PalladiumFeatureFlagEnabledCondition implements ICondition {

    private final PalladiumFeatureFlags.Type featureFlag;

    public PalladiumFeatureFlagEnabledCondition(PalladiumFeatureFlags.Type featureFlag) {
        this.featureFlag = featureFlag;
    }

    @Override
    public ResourceLocation getID() {
        return PalladiumFeatureFlags.DATA_CONDITION_ID;
    }

    @Override
    public boolean test(IContext iContext) {
        return PalladiumFeatureFlags.isEnabled(this.featureFlag);
    }

    public static class Serializer implements IConditionSerializer<PalladiumFeatureFlagEnabledCondition> {

        @Override
        public void write(JsonObject jsonObject, PalladiumFeatureFlagEnabledCondition condition) {
            jsonObject.addProperty("feature_flag", condition.featureFlag.name());
        }

        @Override
        public PalladiumFeatureFlagEnabledCondition read(JsonObject jsonObject) {
            var type = PalladiumFeatureFlags.getFeatureFlag(GsonHelper.getAsString(jsonObject, "feature_flag"));

            if (type == null) {
                throw new JsonParseException("Unknown Palladium feature flag: " + GsonHelper.getAsString(jsonObject, "feature_flag"));
            }

            return new PalladiumFeatureFlagEnabledCondition(type);
        }

        @Override
        public ResourceLocation getID() {
            return PalladiumFeatureFlags.DATA_CONDITION_ID;
        }
    }
}
