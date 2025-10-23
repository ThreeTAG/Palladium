package net.threetag.palladium.client.texture.transformer;

import net.threetag.palladium.Palladium;

public class TextureTransformerSerializers {

    public static final TextureTransformerSerializer<AlphaMaskTransformer> ALPHA_MASK = register("alpha_mask", new AlphaMaskTransformer.Serializer());
    public static final TextureTransformerSerializer<OverlayTransformer> OVERLAY = register("overlay", new OverlayTransformer.Serializer());

    private static <T extends TextureTransformer> TextureTransformerSerializer<T> register(String id, TextureTransformerSerializer<T> serializer) {
        return TextureTransformerSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
