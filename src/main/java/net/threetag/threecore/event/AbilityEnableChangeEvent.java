package net.threetag.threecore.event;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.threetag.threecore.ability.Ability;

@Cancelable
public class AbilityEnableChangeEvent extends LivingEvent
{
	public Ability ability;
	public Type type;
	public AbilityEnableChangeEvent(Ability ability, LivingEntity entity, Type type)
	{
		super(entity);
		this.ability = ability;
		this.type = type;
	}

	public enum Type {
		ENABLED,
		DISABLED
	}
}
