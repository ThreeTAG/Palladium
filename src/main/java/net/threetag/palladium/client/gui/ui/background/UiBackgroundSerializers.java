package net.threetag.palladium.client.gui.ui.background;

import net.threetag.palladium.Palladium;

public class UiBackgroundSerializers {

    public static final UiBackgroundSerializer<EmptyBackground> EMPTY = register("empty", new EmptyBackground.Serializer());
    public static final UiBackgroundSerializer<TextureBackground> TEXTURE = register("texture", new TextureBackground.Serializer());
    public static final UiBackgroundSerializer<RepeatingTextureBackground> REPEATING_TEXTURE = register("repeating_texture", new RepeatingTextureBackground.Serializer());
    public static final UiBackgroundSerializer<SpriteBackground> SPRITE = register("sprite", new SpriteBackground.Serializer());

    private static <T extends UiBackground> UiBackgroundSerializer<T> register(String id, UiBackgroundSerializer<T> serializer) {
        UiBackgroundSerializer.register(Palladium.id(id), serializer);
        return serializer;
    }

    public static void init() {
        // nothing
    }
}
