package net.threetag.threecore.scripts.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.threetag.threecore.scripts.ScriptParameterName;

/**
 * Created by Nictogen on 2020-06-25.
 */
public abstract class PlayerInteractBlockScriptEvent extends PlayerInteractScriptEvent
{
	public PlayerInteractBlockScriptEvent(PlayerInteractEvent event)
	{
		super(event);
	}

	@Override public boolean isCancelable()
	{
		return true;
	}

	public String getUseBlock(){
		return ((PlayerInteractEvent.RightClickBlock)this.event).getUseBlock().name();
	}

	public String getUseItem(){
		return ((PlayerInteractEvent.RightClickBlock)this.event).getUseItem().name();
	}

	public void setUseBlock(@ScriptParameterName("useBlockResult") String s){
		((PlayerInteractEvent.RightClickBlock)this.event).setUseBlock(Event.Result.valueOf(s.toUpperCase()));
	}

	public void setUseItem(@ScriptParameterName("useItemResult") String s){
		((PlayerInteractEvent.RightClickBlock)this.event).setUseItem(Event.Result.valueOf(s.toUpperCase()));
	}
}
