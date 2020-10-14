package net.threetag.threecore.scripts.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.threetag.threecore.scripts.accessors.EntityAccessor;

/**
 * Created by Nictogen on 2020-06-25.
 */
public class EntityInteractSpecificScriptEvent extends PlayerInteractScriptEvent
{
	public EntityInteractSpecificScriptEvent(PlayerInteractEvent.EntityInteractSpecific event)
	{
		super(event);
	}

	public EntityAccessor getTarget(){
		return new EntityAccessor(((PlayerInteractEvent.EntityInteractSpecific)event).getTarget());
	}

	@Override public boolean isCancelable()
	{
		return true;
	}
}
