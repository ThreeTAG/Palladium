package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.util.GuiUtil;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public class TextUiComponent extends RenderableUiComponent {

    public static final MapCodec<TextUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(t -> t.text),
            PalladiumCodecs.COLOR_CODEC.optionalFieldOf("color", RenderUtil.DEFAULT_GRAY_COLOR).forGetter(t -> t.color),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_width", 0).forGetter(t -> t.maxWidth),
            Codec.BOOL.optionalFieldOf("outline", false).forGetter(t -> t.outline),
            propertiesCodec()
    ).apply(instance, TextUiComponent::new));

    private final Component text;
    private final Color color;
    private final int maxWidth;
    public boolean outline;

    public TextUiComponent(Component text, Color color, int maxWidth, boolean outline, UiComponentProperties properties) {
        super(properties);
        this.text = text;
        this.color = color;
        this.maxWidth = maxWidth;
        this.outline = outline;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.TEXT;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        var text = Language.getInstance().getVisualOrder(minecraft.font.ellipsize(this.text, this.getWidth()));

        if (this.outline) {
            GuiUtil.drawStringWithBlackOutline(gui, text, x, y, this.color.getRGB());
        } else {
            gui.drawString(minecraft.font, text, x, y, this.color.getRGB(), false);
        }
    }

    public static class Serializer extends UiComponentSerializer<TextUiComponent> {

        @Override
        public MapCodec<TextUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, TextUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Text")
                    .setDescription("Renders a text component")
                    .add("text", TYPE_TEXT_COMPONENT, "The text to be drawn")
                    .addOptional("color", TYPE_INT, "Color of the rendered text", RenderUtil.DEFAULT_GRAY)
                    .addOptional("outline", TYPE_BOOLEAN, "Whether or not a black outline will be drawn around the text");
        }
    }
}
