package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;
import java.util.Optional;

public record ButtonUiComponent(UiComponentPosition position, Component text, Optional<Action> action,
                                boolean closeOnPress, int width,
                                int height) implements UiComponent {

    public static final MapCodec<ButtonUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UiComponentPosition.CODEC.optionalFieldOf("position", UiComponentPosition.TOP_LEFT).forGetter(ButtonUiComponent::position),
            ComponentSerialization.CODEC.fieldOf("text").forGetter(ButtonUiComponent::text),
            Action.CODEC.optionalFieldOf("action").forGetter(ButtonUiComponent::action),
            Codec.BOOL.optionalFieldOf("close_on_press", false).forGetter(ButtonUiComponent::closeOnPress),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(ButtonUiComponent::width),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("height", 18).forGetter(ButtonUiComponent::height)
    ).apply(instance, ButtonUiComponent::new));

    @Override
    public UiComponentPosition getPosition() {
        return this.position();
    }

    @Override
    public int getWidth() {
        return this.width();
    }

    @Override
    public int getHeight() {
        return this.height();
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.BUTTON;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen) {
        return Button.builder(this.text(), button -> {
                    this.action()
                            .flatMap(a -> a.createAction(Collections.emptyMap()))
                            .ifPresent(clickevent -> Screen.defaultHandleGameClickEvent(clickevent, Minecraft.getInstance(), screen));

                    if (this.closeOnPress()) {
                        screen.onClose();
                    }
                })
                .bounds(this.getX(screen), this.getY(screen), this.width(), this.height())
                .build();
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
                    .addOptional("position", TYPE_UI_POSITION, "Position of this component", new int[]{0, 0})
                    .add("text", TYPE_TEXT_COMPONENT, "Text of the button")
                    .addOptional("action", TYPE_DIALOG_ACTION, "Action that is happening when pressing the button. Check MC wiki for dialog actions.")
                    .addOptional("close_on_press", TYPE_BOOLEAN, "Whether or not the screen will be closed when pressing the button.")
                    .add("width", TYPE_POSITIVE_INT, "Width of the button.")
                    .addOptional("height", TYPE_POSITIVE_INT, "Height of the button", 18);
        }
    }
}
