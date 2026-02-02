package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;

public final class ButtonUiComponent extends UiComponent {

    public static final MapCodec<ButtonUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(ButtonUiComponent::text),
            Codec.BOOL.optionalFieldOf("close_on_press", false).forGetter(ButtonUiComponent::closeOnPress),
            propertiesCodec()
    ).apply(instance, ButtonUiComponent::new));

    private final Component text;
    private final boolean closeOnPress;

    public ButtonUiComponent(Component text, boolean closeOnPress, UiComponentProperties properties) {
        super(properties);
        this.text = text;
        this.closeOnPress = closeOnPress;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.BUTTON;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen) {
        var builder = Button.builder(this.text(), button -> {
                    this.getProperties().action()
                            .flatMap(a -> a.createAction(Collections.emptyMap()))
                            .ifPresent(clickevent -> Screen.defaultHandleGameClickEvent(clickevent, Minecraft.getInstance(), screen));

                    if (this.closeOnPress()) {
                        screen.onClose();
                    }
                })
                .bounds(this.getX(screen.getInnerRectangle()), this.getY(screen.getInnerRectangle()), this.getWidth(), this.getHeight());

        this.getProperties().tooltip().ifPresent(component -> builder.tooltip(Tooltip.create(component)));

        return builder.build();
    }

    public Component text() {
        return text;
    }

    public boolean closeOnPress() {
        return closeOnPress;
    }

    public static class Serializer extends UiComponentSerializer<ButtonUiComponent> {

        @Override
        public MapCodec<ButtonUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, ButtonUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Button")
                    .setDescription("Adds a button.")
                    .add("text", TYPE_TEXT_COMPONENT, "Text of the button")
                    .addOptional("close_on_press", TYPE_BOOLEAN, "Whether or not the screen will be closed when pressing the button.");
        }
    }
}
