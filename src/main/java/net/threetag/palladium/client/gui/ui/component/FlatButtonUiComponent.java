package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.threetag.palladium.client.gui.widget.FlatButton;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;

public final class FlatButtonUiComponent extends UiComponent {

    public static final MapCodec<FlatButtonUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(FlatButtonUiComponent::text),
            Codec.BOOL.optionalFieldOf("close_on_press", false).forGetter(FlatButtonUiComponent::closeOnPress),
            propertiesCodec()
    ).apply(instance, FlatButtonUiComponent::new));

    private final Component text;
    private final boolean closeOnPress;

    public FlatButtonUiComponent(Component text, boolean closeOnPress, UiComponentProperties properties) {
        super(properties);
        this.text = text;
        this.closeOnPress = closeOnPress;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.FLAT_BUTTON;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        var builder = FlatButton.flatBuilder(this.text(), button -> {
                    this.getProperties().action()
                            .flatMap(a -> a.createAction(Collections.emptyMap()))
                            .ifPresent(clickevent -> Screen.defaultHandleGameClickEvent(clickevent, Minecraft.getInstance(), screen));

                    if (this.closeOnPress()) {
                        screen.onClose();
                    }
                })
                .bounds(this.getX(rectangle), this.getY(rectangle), this.getWidth(), this.getHeight());

        this.getProperties().tooltip().ifPresent(component -> builder.tooltip(Tooltip.create(component)));

        return builder.build();
    }

    public Component text() {
        return text;
    }

    public boolean closeOnPress() {
        return closeOnPress;
    }

    public static class Serializer extends UiComponentSerializer<FlatButtonUiComponent> {

        @Override
        public MapCodec<FlatButtonUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, FlatButtonUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Flat Button")
                    .setDescription("Adds a flat button.")
                    .add("text", TYPE_TEXT_COMPONENT, "Text of the button")
                    .addOptional("close_on_press", TYPE_BOOLEAN, "Whether or not the screen will be closed when pressing the button.");
        }
    }
}
