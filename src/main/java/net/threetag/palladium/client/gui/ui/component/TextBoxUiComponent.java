package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.gui.widget.BackgroundlessTextBoxWidget;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class TextBoxUiComponent extends UiComponent {

    public static final MapCodec<TextBoxUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(t -> t.text),
            propertiesCodec()
    ).apply(instance, TextBoxUiComponent::new));

    private final Component text;

    public TextBoxUiComponent(Component text, UiComponentProperties properties) {
        super(properties);
        this.text = text;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.TEXT_BOX;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        return new BackgroundlessTextBoxWidget(
                this.getX(rectangle),
                this.getY(rectangle),
                this.getWidth(),
                this.getHeight(),
                this.text,
                screen.getFont()
        );
    }

    public static class Serializer extends UiComponentSerializer<TextBoxUiComponent> {

        @Override
        public MapCodec<TextBoxUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, TextBoxUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Text Box")
                    .setDescription("Renders a scrollable text box")
                    .add("text", TYPE_TEXT_COMPONENT, "The text to be drawn");
        }
    }
}
