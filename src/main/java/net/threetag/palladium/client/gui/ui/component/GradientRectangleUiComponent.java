package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public class GradientRectangleUiComponent extends RenderableUiComponent {

    public static final MapCodec<GradientRectangleUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.COLOR_CODEC.fieldOf("color_from").forGetter(c -> c.colorFrom),
            PalladiumCodecs.COLOR_CODEC.fieldOf("color_to").forGetter(c -> c.colorTo),
            propertiesCodec()
    ).apply(instance, GradientRectangleUiComponent::new));

    private final Color colorFrom;
    private final Color colorTo;

    public GradientRectangleUiComponent(Color colorFrom, Color colorTo, UiComponentProperties properties) {
        super(properties);
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        gui.fillGradient(x, y, x + width, y + height, this.colorFrom.getRGB(), this.colorTo.getRGB());
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.GRADIENT_RECTANGLE;
    }

    public static class Serializer extends UiComponentSerializer<GradientRectangleUiComponent> {

        @Override
        public MapCodec<GradientRectangleUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, GradientRectangleUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Gradient Rectangle")
                    .setDescription("Renders a gradient rectangle.")
                    .add("color_from", TYPE_COLOR, "The color for the start of the gradient.")
                    .add("color_to", TYPE_COLOR, "The color for the end of the gradient.");
        }
    }
}
