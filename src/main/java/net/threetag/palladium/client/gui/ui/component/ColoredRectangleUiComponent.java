package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public class ColoredRectangleUiComponent extends RenderableUiComponent {

    public static final MapCodec<ColoredRectangleUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.COLOR_CODEC.fieldOf("color").forGetter(c -> c.color),
            propertiesCodec()
    ).apply(instance, ColoredRectangleUiComponent::new));

    private final Color color;

    public ColoredRectangleUiComponent(Color color, UiComponentProperties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        gui.fill(x, y, x + width, y + height, this.color.getRGB());
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.COLORED_RECTANGLE;
    }

    public static class Serializer extends UiComponentSerializer<ColoredRectangleUiComponent> {

        @Override
        public MapCodec<ColoredRectangleUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, ColoredRectangleUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Colored Rectangle")
                    .setDescription("Renders a colored rectangle.")
                    .add("color", TYPE_COLOR, "The color for the rectangle.");
        }
    }
}
