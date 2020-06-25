package net.threetag.threecore.scripts.events;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.scripts.accessors.AbilityAccessor;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class RegisterAbilityThreeDataScriptEvent extends ScriptEvent {

	private final AbilityAccessor abilityAccessor;

	public RegisterAbilityThreeDataScriptEvent(Ability ability) {
		this.abilityAccessor = new AbilityAccessor(ability);
	}

	public <T> void register(@ScriptParameterName("data") ThreeData<T> data, @ScriptParameterName("defaultValue") T defaultValue) {
		this.getAbility().value.register(data, defaultValue);
	}

	// ugly fix since JavaScript numbers are apparently always doubles?

	public void registerInteger(@ScriptParameterName("data") IntegerThreeData data, @ScriptParameterName("defaultValue") double defaultValue) {
		this.getAbility().value.register(data, new Double(defaultValue).intValue());
	}

	public void registerFloat(@ScriptParameterName("data") FloatThreeData data, @ScriptParameterName("defaultValue") double defaultValue) {
		this.getAbility().value.register(data, new Double(defaultValue).floatValue());
	}

	public AbilityAccessor getAbility() {
		return abilityAccessor;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
