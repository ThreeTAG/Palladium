package net.threetag.palladium.client.gui.ui.layout;

import net.threetag.palladium.Palladium;

public class UiLayoutSerializers {

    public static final UiLayoutSerializer<SimpleUiLayout> SIMPLE = register("simple", new SimpleUiLayout.Serializer());
    public static final UiLayoutSerializer<MultiColumnLayout> MULTI_COLUMN = register("multi_column", new MultiColumnLayout.Serializer());

    private static <T extends UiLayout> UiLayoutSerializer<T> register(String id, UiLayoutSerializer<T> serializer) {
        UiLayoutSerializer.register(Palladium.id(id), serializer);
        return serializer;
    }

    public static void init() {
        // nothing
    }
}
