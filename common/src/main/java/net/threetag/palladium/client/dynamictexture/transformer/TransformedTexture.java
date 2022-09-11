package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

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
    public void load(ResourceManager manager) {
        Minecraft.getInstance().execute(() -> {
            releaseId();
            InputStream textureStream = null;

            try {
                NativeImage image = NativeImage.read(textureStream = manager.getResource(location).get().open());

                for (ITextureTransformer transformer : this.transformers) {
                    image = transformer.transform(image, manager, this.stringConverter);
                }

                TextureUtil.prepareImage(this.getId(), image.getWidth(), image.getHeight());
                image.upload(0, 0, 0, false);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (textureStream != null) {
                    try {
                        textureStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
