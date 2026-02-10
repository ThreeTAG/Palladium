package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
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

    public int getX(ScreenRectangle parent) {
        var props = this.getProperties();

        return switch (props.alignment().getHorizontalAlignment()) {
            case LEFT -> parent.left();
            case CENTER -> parent.left() + (parent.width() / 2) - (this.getWidth() / 2);
            case RIGHT -> parent.right() - this.getWidth();
        } + props.x();
    }

    public int getY(ScreenRectangle parent) {
        var props = this.getProperties();

        return switch (props.alignment().getVerticalAlignment()) {
            case LEFT -> parent.top();
            case CENTER -> parent.top() + (parent.height() / 2) - (this.getHeight() / 2);
            case RIGHT -> parent.bottom() - this.getHeight();
        } + props.y();
    }

    public abstract AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle);

    protected static <B extends UiComponent> RecordCodecBuilder<B, UiComponentProperties> propertiesCodec() {
        return UiComponentProperties.CODEC.optionalFieldOf("properties", UiComponentProperties.DEFAULT).forGetter(UiComponent::getProperties);
    }

    protected static <B extends UiComponent> RecordCodecBuilder<B, UiComponentProperties> propertiesCodec16X16() {
        return UiComponentProperties.CODEC_16X16.optionalFieldOf("properties", UiComponentProperties.DEFAULT_16X16).forGetter(UiComponent::getProperties);
    }

    protected static <B extends UiComponent> RecordCodecBuilder<B, UiComponentProperties> propertiesCodec(int width, int height) {
        return UiComponentProperties.codecWithDefaultSize(width, height).optionalFieldOf("properties", UiComponentProperties.withSize(width, height)).forGetter(UiComponent::getProperties);
    }
}
