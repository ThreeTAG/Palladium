package net.threetag.threecore.client.renderer.entity.modellayer.texture.transformer;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResourceManager;

import java.io.IOException;
import java.util.function.Function;

public interface ITextureTransformer {

    NativeImage transform(NativeImage texture, IResourceManager manager, Function<String, String> stringConverter) throws IOException;

}
