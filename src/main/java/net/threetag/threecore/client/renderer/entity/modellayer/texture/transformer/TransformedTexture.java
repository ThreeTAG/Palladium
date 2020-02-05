package net.threetag.threecore.client.renderer.entity.modellayer.texture.transformer;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class TransformedTexture extends SimpleTexture {

    private final List<ITextureTransformer> transformers;
    private final Function<String, String> stringConverter;

    public TransformedTexture(ResourceLocation base, List<ITextureTransformer> transformers, Function<String, String> stringConverter) {
        super(base);
        this.transformers = transformers;
        this.stringConverter = stringConverter;
    }

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {
        deleteGlTexture();
        InputStream textureStream = null;
        InputStream maskStream = null;

        try {
            NativeImage image = NativeImage.read(textureStream = manager.getResource(textureLocation).getInputStream());

            for (ITextureTransformer transformer : this.transformers) {
                image = transformer.transform(image, manager, this.stringConverter);
            }

            TextureUtil.prepareImage(this.getGlTextureId(), image.getWidth(), image.getHeight());
            image.uploadTextureSub(0, 0, 0, false);
        } finally {
            if (textureStream != null) {
                textureStream.close();
            }

            if (maskStream != null) {
                maskStream.close();
            }
        }
    }

}
