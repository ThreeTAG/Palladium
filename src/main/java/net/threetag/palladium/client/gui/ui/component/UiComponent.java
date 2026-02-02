package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;

public abstract class UiComponent {

    public static final Codec<UiComponent> CODEC = UiComponentSerializer.TYPE_CODEC.dispatch(UiComponent::getSerializer, UiComponentSerializer::codec);

    private final UiComponentProperties properties;

    protected UiComponent(UiComponentProperties properties) {
        this.properties = properties;
    }

    public final UiComponentProperties getProperties() {
        return this.properties;
    }

    public final int getWidth() {
        return this.getProperties().width();
    }

    public final int getHeight() {
        return this.getProperties().height();
    }

    public abstract UiComponentSerializer<?> getSerializer();

    public int getX(UiScreen screen) {
        var rectangle = screen.getInnerRectangle();
        var pos = this.getProperties();
        return (pos.alignment().isLeft() ? rectangle.left() : rectangle.right() - this.getWidth()) + pos.x();
    }

    public int getY(UiScreen screen) {
        var rectangle = screen.getInnerRectangle();
        var pos = this.getProperties();
        return (pos.alignment().isTop() ? rectangle.top() : rectangle.bottom() - this.getHeight()) + pos.y();
    }

    public abstract AbstractWidget buildWidget(UiScreen screen);

    protected static <B extends UiComponent> RecordCodecBuilder<B, UiComponentProperties> propertiesCodec() {
        return UiComponentProperties.CODEC.optionalFieldOf("properties", UiComponentProperties.DEFAULT).forGetter(UiComponent::getProperties);
    }
}
