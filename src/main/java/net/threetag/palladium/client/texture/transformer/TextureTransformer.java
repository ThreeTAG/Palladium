package net.threetag.palladium.client.texture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.function.Function;

public abstract class TextureTransformer {

    public static final Codec<TextureTransformer> CODEC = TextureTransformerSerializer.TYPE_CODEC.dispatch(TextureTransformer::getSerializer, TextureTransformerSerializer::codec);

    public abstract NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException;

    public abstract TextureTransformerSerializer<?> getSerializer();

}
