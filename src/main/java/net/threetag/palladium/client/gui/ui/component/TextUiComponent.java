package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.util.GuiUtil;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class TextUiComponent implements RenderableUiComponent {

    public static final MapCodec<TextUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UiComponentPosition.CODEC.optionalFieldOf("position", UiComponentPosition.TOP_LEFT).forGetter(t -> t.position),
            ComponentSerialization.CODEC.fieldOf("text").forGetter(t -> t.text),
            Codec.INT.optionalFieldOf("color", RenderUtil.DEFAULT_GRAY).forGetter(t -> t.color),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_width", 0).forGetter(t -> t.maxWidth),
            Codec.BOOL.optionalFieldOf("outline", false).forGetter(t -> t.outline)
    ).apply(instance, TextUiComponent::new));

    private final UiComponentPosition position;
    private final Component text;
    private final int color;
    private final int maxWidth;
    public boolean outline;
    private int width;

    public TextUiComponent(UiComponentPosition position, Component text, int color, int maxWidth, boolean outline) {
        this.position = position;
        this.text = text;
        this.color = color;
        this.maxWidth = maxWidth;
        this.outline = outline;
    }

    public TextUiComponent(Component text) {
        this.position = UiComponentPosition.TOP_LEFT;
        this.text = text;
        this.color = RenderUtil.FULL_WHITE;
        this.maxWidth = 0;
    }

    public TextUiComponent(Component text, boolean outline) {
        this(text);
        this.outline = outline;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.TEXT;
    }

    @Override
    public UiComponentPosition getPosition() {
        return this.position;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight - (this.outline ? 0 : 2);
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, UiAlignment alignment) {
        this.width = Math.min(Minecraft.getInstance().font.width(this.text) - 1, this.maxWidth);

        if (outline) {
            this.width += 2;
        }

        // TODO max width
        if (this.outline) {
            GuiUtil.drawStringWithBlackOutline(gui, this.text, x, y, this.color);
        } else {
            gui.drawString(minecraft.font, this.text, x, y, this.color, false);
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
                    .addOptional("position", TYPE_UI_POSITION, "Position of this component", new int[]{0, 0})
                    .addOptional("color", TYPE_INT, "Color of the rendered text", RenderUtil.DEFAULT_GRAY)
//                    .addOptional("max_width", TYPE_INT, "Maximum width of the rendered text. If text is longer it will be cut off using '...' at the end.")
                    .addOptional("outline", TYPE_BOOLEAN, "Whether or not a black outline will be drawn around the text");
        }
    }
}
