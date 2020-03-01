package net.threetag.threecore.scripts;

import net.minecraft.command.ICommandSource;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;

public class ScriptCommandSource implements ICommandSource {

    @Override
    public void sendMessage(ITextComponent component) {
        ThreeCore.LOGGER.error("Script Command Error: " + component.getFormattedText());
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
