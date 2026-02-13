package net.threetag.palladium.client.gui.ui.layout;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;

import java.util.function.BiConsumer;

public abstract class UiLayout {

    public final void open() {
        Minecraft.getInstance().setScreen(new UiScreen(this));
    }

    public abstract UiLayoutSerializer<?> getSerializer();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void renderBackground(GuiGraphics guiGraphics, int x, int y);

    public void addComponents(UiScreen screen, int x, int y, BiConsumer<UiComponent, AbstractWidget> consumer) {

    }

    public static class Codecs {

        public static final Codec<UiLayout> CODEC = Codec.withAlternative(
                UiLayoutSerializer.TYPE_CODEC.dispatch(UiLayout::getSerializer, UiLayoutSerializer::codec),
                SimpleUiLayout.CODEC.codec()
        );

    }

}
