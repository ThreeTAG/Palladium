package net.threetag.palladium.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public interface DelayedRenderCallReceiver {

    void renderDelayed(Consumer<GuiGraphics> consumer);

}
