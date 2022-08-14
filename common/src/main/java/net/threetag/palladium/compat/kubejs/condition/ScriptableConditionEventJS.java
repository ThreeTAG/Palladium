package net.threetag.palladium.compat.kubejs.condition;

import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ScriptableConditionEventJS extends EventJS {

    private final ResourceLocation key;
    private final ScriptableCondition condition;

    public ScriptableConditionEventJS(ResourceLocation key, ScriptableCondition condition) {
        this.key = key;
        this.condition = condition;
    }

    public boolean setHandler(ResourceLocation key, Function<LivingEntityJS, Boolean> function) {
        if (this.key.equals(key)) {
            this.condition.function = function;
            return true;
        }
        return false;
    }
}
