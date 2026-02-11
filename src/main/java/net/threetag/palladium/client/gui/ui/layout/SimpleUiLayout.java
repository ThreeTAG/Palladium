package net.threetag.palladium.client.gui.ui.layout;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.screen.UiPadding;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public final class SimpleUiLayout extends UiLayout {

    public static final MapCodec<SimpleUiLayout> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(SimpleUiLayout::getWidth),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(SimpleUiLayout::getHeight),
            UiPadding.CODEC.optionalFieldOf("padding", UiPadding.SEVEN).forGetter(SimpleUiLayout::padding),
            UiScreen.BACKGROUND_CODEC.optionalFieldOf("background", UiBackground.Sprite.DEFAULT).forGetter(SimpleUiLayout::background),
            UiComponent.CODEC.listOf().optionalFieldOf("components", Collections.emptyList()).forGetter(SimpleUiLayout::components)
    ).apply(instance, SimpleUiLayout::new));

    private final int width;
    private final int height;
    private final UiPadding padding;
    private final UiBackground background;
    private final List<UiComponent> components;

    public SimpleUiLayout(int width, int height, UiPadding padding, UiBackground background, List<UiComponent> components) {
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.background = background;
        this.components = components;
    }

    @Override
    public UiLayoutSerializer<?> getSerializer() {
        return UiLayoutSerializers.SIMPLE;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int x, int y) {
        this.background().render(guiGraphics, x, y, this.width, this.height);
    }

    @Override
    public void addComponents(UiScreen screen, int x, int y, BiConsumer<UiComponent, AbstractWidget> consumer) {
        var rectangle = new ScreenRectangle(
                x + this.padding().left(),
                y + this.padding().top(),
                this.getWidth() - this.padding().left() - this.padding().right(),
                this.getHeight() - this.padding.top() - this.padding().bottom());

        for (UiComponent component : this.components) {
            var widget = component.buildWidget(screen, rectangle);
            consumer.accept(component, widget);
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public UiPadding padding() {
        return padding;
    }

    public UiBackground background() {
        return background;
    }

    public List<UiComponent> components() {
        return components;
    }

    public static class Serializer extends UiLayoutSerializer<SimpleUiLayout> {

        @Override
        public MapCodec<SimpleUiLayout> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiLayout, SimpleUiLayout> builder, HolderLookup.Provider provider) {
            builder.setName("Simple")
                    .setDescription("Used for simple layouts.")
                    .add("width", TYPE_POSITIVE_INT, "Width of this layout")
                    .add("height", TYPE_POSITIVE_INT, "Height of this layout")
                    .addOptional("padding", TYPE_UI_PADDING, "Padding for each side of the layout", 0)
                    .addOptional("background", TYPE_UI_BACKGROUND, "The background that is drawn for the layout.", UiBackground.Sprite.DEFAULT.toString())
                    .add("components", TYPE_UI_COMPONENTS, "List of UI components");
        }
    }

}
