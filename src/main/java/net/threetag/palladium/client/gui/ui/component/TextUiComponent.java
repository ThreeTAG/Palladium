package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

public class TextUiComponent extends AbstractStringUiComponent {

    public static final MapCodec<TextUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(t -> t.text),
            PalladiumCodecs.COLOR_INT_CODEC.optionalFieldOf("color", RenderUtil.DEFAULT_GRAY).forGetter(TextUiComponent::getColor),
            Codec.BOOL.optionalFieldOf("shadow", false).forGetter(TextUiComponent::hasShadow),
            TEXT_ALIGNMENT_CODEC.optionalFieldOf("alignment", TextAlignment.LEFT).forGetter(TextUiComponent::getTextAlignment),
            TEXT_OVERFLOW_CODEC.optionalFieldOf("overflow", StringWidget.TextOverflow.CLAMPED).forGetter(TextUiComponent::getTextOverflow),
            propertiesCodec()
    ).apply(instance, TextUiComponent::new));

    private final Component text;

    public TextUiComponent(Component text, int color, boolean shadow, TextAlignment alignment, StringWidget.TextOverflow textOverflow, UiComponentProperties properties) {
        super(color, shadow, alignment, textOverflow, properties);
        this.text = text;
    }

    @Override
    public Component getText(UiScreen screen) {
        return this.text;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.TEXT;
    }

    public static class Serializer extends AbstractStringUiComponentSerializer<TextUiComponent> {

        @Override
        public MapCodec<TextUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, TextUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Text")
                    .setDescription("Renders a text component")
                    .add("text", TYPE_TEXT_COMPONENT, "The text to be drawn");
        }
    }
}
