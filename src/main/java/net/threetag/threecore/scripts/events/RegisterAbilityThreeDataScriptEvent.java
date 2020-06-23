package net.threetag.threecore.scripts.events;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.scripts.accessors.AbilityAccessor;
import net.threetag.threecore.util.threedata.ThreeData;

public class RegisterAbilityThreeDataScriptEvent extends ScriptEvent {

	private final AbilityAccessor abilityAccessor;

	public RegisterAbilityThreeDataScriptEvent(Ability ability) {
		this.abilityAccessor = new AbilityAccessor(ability);
	}

	public <T> void register(@ScriptParameterName("data") ThreeData<T> data, @ScriptParameterName("defaultValue") T defaultValue) {
		this.getAbility().value.register(data, defaultValue);
	}

	public AbilityAccessor getAbility() {
		return abilityAccessor;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
