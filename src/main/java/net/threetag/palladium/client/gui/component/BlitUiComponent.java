package net.threetag.palladium.client.gui.component;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public record BlitUiComponent(Identifier texture, int u, int v, int uOffset, int vOffset, int texWidth,
                              int texHeight) implements UiComponent {

    @Override
    public int getWidth() {
        return this.uOffset;
    }

    @Override
    public int getHeight() {
        return this.vOffset;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        gui.blit(RenderPipelines.GUI_TEXTURED,this.texture, x, y, this.u, this.v, this.uOffset, this.vOffset, this.texWidth, this.texHeight);
    }
}
