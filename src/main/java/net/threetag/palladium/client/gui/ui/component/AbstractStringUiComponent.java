package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.gui.widget.StringWidget;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public abstract class AbstractStringUiComponent extends UiComponent {

    public static final Codec<net.minecraft.client.gui.components.StringWidget.TextOverflow> TEXT_OVERFLOW_CODEC = Codec.STRING.validate(s -> {
        if (s.equalsIgnoreCase("clamped") || s.equalsIgnoreCase("scrolling")) {
            return DataResult.success(s);
        } else {
            return DataResult.error(() -> "Unknown text overflow");
        }
    }).xmap(s -> s.equalsIgnoreCase("clamped") ? net.minecraft.client.gui.components.StringWidget.TextOverflow.CLAMPED : net.minecraft.client.gui.components.StringWidget.TextOverflow.SCROLLING,
            textOverflow -> textOverflow == net.minecraft.client.gui.components.StringWidget.TextOverflow.CLAMPED ? "clamped" : "scrolling");

    public static final Codec<TextAlignment> TEXT_ALIGNMENT_CODEC = Codec.STRING.validate(s -> {
        if (s.equalsIgnoreCase("left") || s.equalsIgnoreCase("center") || s.equalsIgnoreCase("right")) {
            return DataResult.success(s);
        } else {
            return DataResult.error(() -> "Unknown text alignment");
        }
    }).xmap(s -> s.equalsIgnoreCase("left") ? TextAlignment.LEFT : s.equalsIgnoreCase("center") ? TextAlignment.CENTER : TextAlignment.RIGHT,
            textAlignment -> textAlignment == TextAlignment.LEFT ? "left" : textAlignment == TextAlignment.CENTER ? "center" : "right");

    private final int color;
    private final boolean shadow;
    private final TextAlignment alignment;
    private final net.minecraft.client.gui.components.StringWidget.TextOverflow textOverflow;

    public AbstractStringUiComponent(int color, boolean shadow, TextAlignment alignment, net.minecraft.client.gui.components.StringWidget.TextOverflow textOverflow, UiComponentProperties properties) {
        super(properties);
        this.color = color;
        this.shadow = shadow;
        this.alignment = alignment;
        this.textOverflow = textOverflow;
    }

    public abstract Component getText(UiScreen screen);

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        var text = getText(screen);
        var widget = new StringWidget(this.getX(rectangle), this.getY(rectangle), this.getWidth(), this.getHeight(), text, screen.getFont())
                .setColor(this.color)
                .setShadow(this.shadow)
                .setAlignment(this.alignment);
        widget.setMaxWidth(this.getWidth(), this.textOverflow);
        return widget;
    }

    public int getColor() {
        return color;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public TextAlignment getTextAlignment() {
        return alignment;
    }

    public net.minecraft.client.gui.components.StringWidget.TextOverflow getTextOverflow() {
        return textOverflow;
    }

    public abstract static class AbstractStringUiComponentSerializer<T extends AbstractStringUiComponent> extends UiComponentSerializer<T> {

        @Override
        public CodecDocumentationBuilder<UiComponent, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider)
                    .addOptional("color", TYPE_COLOR, "Color of this text", RenderUtil.DEFAULT_GRAY_COLOR)
                    .addOptional("shadow", TYPE_BOOLEAN, "Whether or not this text has a shadow", false)
                    .addOptional("alignment", TYPE_TEXT_ALIGNMENT, "Alignment of this text", "left")
                    .addOptional("overflow", TYPE_TEXT_OVERFLOW, "How to behave if the text is longer than the component width", "clamped");
        }

    }
}
