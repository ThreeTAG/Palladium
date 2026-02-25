package net.threetag.palladium.logic.triggers;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

public class PalladiumCriteriaTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Palladium.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, PowerGainedTrigger> POWER_GAINED = CRITERIA_TRIGGERS.register("power_gained", PowerGainedTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PowerLostTrigger> POWER_LOST = CRITERIA_TRIGGERS.register("power_lost", PowerLostTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, AbilityEnabledTrigger> ABILITY_ENABLED = CRITERIA_TRIGGERS.register("ability_enabled", AbilityEnabledTrigger::new);
}
