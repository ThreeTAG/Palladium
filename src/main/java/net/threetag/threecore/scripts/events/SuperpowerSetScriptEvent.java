package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;

/**
 * Created by Nictogen on 2020-06-23.
 */
public class SuperpowerSetScriptEvent extends LivingScriptEvent {

	private String superpower;

	public SuperpowerSetScriptEvent(LivingEntity entity, String superpower) {
		super(entity);
		this.superpower = superpower;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	public String getSuperpower() {
		return superpower;
	}
}
