package net.threetag.threecore.scripts.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Nictogen on 2020-06-25.
 */
public class RightClickEmptyScriptEvent extends PlayerInteractScriptEvent
{
	public RightClickEmptyScriptEvent(PlayerInteractEvent event)
	{
		super(event);
	}

	@Override public boolean isCancelable()
	{
		return false;
	}

}
