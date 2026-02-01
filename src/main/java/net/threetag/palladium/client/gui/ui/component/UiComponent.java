package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.components.AbstractWidget;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;

public interface UiComponent {

    Codec<UiComponent> CODEC = UiComponentSerializer.TYPE_CODEC.dispatch(UiComponent::getSerializer, UiComponentSerializer::codec);

    UiComponentPosition getPosition();

    int getWidth();

    int getHeight();

    UiComponentSerializer<?> getSerializer();

    default int getX(UiScreen screen) {
        var rectangle = screen.getInnerRectangle();
        var pos = this.getPosition();
        return (pos.alignment().isLeft() ? rectangle.left() : rectangle.right() - this.getWidth()) + pos.xOffset();
    }

    default int getY(UiScreen screen) {
        var rectangle = screen.getInnerRectangle();
        var pos = this.getPosition();
        return (pos.alignment().isTop() ? rectangle.top() : rectangle.bottom() - this.getHeight()) + pos.yOffset();
    }

    AbstractWidget buildWidget(UiScreen screen);
}
