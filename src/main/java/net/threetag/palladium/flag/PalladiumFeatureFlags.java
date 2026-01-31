package net.threetag.palladium.flag;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.neoforge.common.conditions.FeatureFlagsEnabledCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.threetag.palladium.Palladium;

public class PalladiumFeatureFlags {

    public static final FeatureFlag TAILORING = FeatureFlags.REGISTRY.getFlag(Palladium.id("tailoring"));

    public static ICondition conditionTailoring() {
        return new FeatureFlagsEnabledCondition(FeatureFlagSet.of(TAILORING));
    }

}
