package net.threetag.threecore.scripts.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Nictogen on 2020-06-25.
 */
public class LeftClickBlockScriptEvent extends PlayerInteractBlockScriptEvent
{
	public LeftClickBlockScriptEvent(PlayerInteractEvent event)
	{
		super(event);
	}
}
