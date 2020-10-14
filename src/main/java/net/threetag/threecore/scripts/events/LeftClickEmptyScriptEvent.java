package net.threetag.threecore.scripts.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Nictogen on 2020-06-25.
 */
public class LeftClickEmptyScriptEvent extends PlayerInteractScriptEvent
{
	public LeftClickEmptyScriptEvent(PlayerInteractEvent.LeftClickEmpty event)
	{
		super(event);
	}

	@Override public boolean isCancelable()
	{
		return true;
	}

	@Override public boolean fire()
	{
		return super.fire();
	}
}
