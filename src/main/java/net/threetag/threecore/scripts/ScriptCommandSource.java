package net.threetag.threecore.scripts;

import net.minecraft.command.ICommandSource;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;

import java.util.UUID;

public class ScriptCommandSource implements ICommandSource {

    @Override public void sendMessage(ITextComponent component, UUID p_145747_2_)
    {
        ThreeCore.LOGGER.error("Script Command Error: " + component.toString());
    }

    @Override
    public boolean shouldReceiveFeedback() {
        return false;
    }

    @Override
    public boolean shouldReceiveErrors() {
        return true;
    }

    @Override
    public boolean allowLogging() {
        return false;
    }
}
