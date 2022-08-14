package net.threetag.palladium.compat.kubejs.ability;

import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ScriptableAbilityEventJS extends EventJS {

    private final ResourceLocation key;
    private final ScriptableAbility ability;

    public ScriptableAbilityEventJS(ResourceLocation key, ScriptableAbility ability) {
        this.key = key;
        this.ability = ability;
    }

    public boolean setTickHandler(ResourceLocation key, Consumer<LivingEntityJS> function) {
        if (this.key.equals(key)) {
            this.ability.tick = function;
            return true;
        }
        return false;
    }

    public boolean setFirstTickHandler(ResourceLocation key, Consumer<LivingEntityJS> function) {
        if (this.key.equals(key)) {
            this.ability.firstTick = function;
            return true;
        }
        return false;
    }

    public boolean setLastTickHandler(ResourceLocation key, Consumer<LivingEntityJS> function) {
        if (this.key.equals(key)) {
            this.ability.lastTick = function;
            return true;
        }
        return false;
    }
}
