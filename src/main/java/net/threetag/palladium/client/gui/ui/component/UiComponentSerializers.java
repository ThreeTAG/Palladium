package net.threetag.palladium.client.gui.ui.component;

import net.threetag.palladium.Palladium;

public class UiComponentSerializers {

    public static final UiComponentSerializer<BlitUiComponent> BLIT = register("blit", new BlitUiComponent.Serializer());
    public static final UiComponentSerializer<TextUiComponent> TEXT = register("text", new TextUiComponent.Serializer());
    public static final UiComponentSerializer<IconUiComponent> ICON = register("icon", new IconUiComponent.Serializer());
    public static final UiComponentSerializer<ButtonUiComponent> BUTTON = register("button", new ButtonUiComponent.Serializer());

    private static <T extends UiComponent> UiComponentSerializer<T> register(String id, UiComponentSerializer<T> serializer) {
        UiComponentSerializer.register(Palladium.id(id), serializer);
        return serializer;
    }

    public static void init() {
        // nothing
    }
}
