package net.threetag.palladium.client.gui.pip;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuiMultiEntityRenderState implements PictureInPictureRenderState {

    public final List<GuiEntityRenderState> renderStates;
    public int x0 = Integer.MAX_VALUE, x1 = Integer.MIN_VALUE, y0 = Integer.MAX_VALUE, y1 = Integer.MIN_VALUE;
    public float scale = 0F;

    public GuiMultiEntityRenderState(List<GuiEntityRenderState> renderStates) {
        this.renderStates = renderStates;

        for (GuiEntityRenderState renderState : this.renderStates) {
            this.x0 = Math.min(renderState.x0(), this.x0);
            this.x1 = Math.max(renderState.x1(), this.x1);
            this.y0 = Math.min(renderState.y0(), this.y0);
            this.y1 = Math.max(renderState.y1(), this.y1);
            this.scale += renderState.scale();
        }

        this.scale /= this.renderStates.size();
    }

    @Override
    public int x0() {
        return this.x0;
    }

    @Override
    public int x1() {
        return this.x1;
    }

    @Override
    public int y0() {
        return this.y0;
    }

    @Override
    public int y1() {
        return this.y1;
    }

    @Override
    public float scale() {
        return this.scale;
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }

    @Override
    public @Nullable ScreenRectangle bounds() {
        return PictureInPictureRenderState.getBounds(x0, y0, x1, y1, null);
    }
}
