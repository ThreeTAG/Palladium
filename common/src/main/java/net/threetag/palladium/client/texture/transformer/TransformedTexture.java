package net.threetag.palladium.client.texture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class TransformedTexture extends SimpleTexture {

    private final ResourceLocation baseTexture;
    private final List<TextureTransformer> transformers;
    private final Function<String, String> stringConverter;

    public TransformedTexture(ResourceLocation resourceId, ResourceLocation baseTexture, List<TextureTransformer> transformers, Function<String, String> stringConverter) {
        super(resourceId);
        this.baseTexture = baseTexture;
        this.transformers = transformers;
        this.stringConverter = stringConverter;
    }

    @Override
    public @NotNull TextureContents loadContents(ResourceManager manager) throws IOException {
        Resource resource = manager.getResourceOrThrow(this.baseTexture);
        TextureMetadataSection metadata = resource.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);
        NativeImage nativeImage;

        try (InputStream inputStream = resource.open()) {
            nativeImage = NativeImage.read(inputStream);

            for (TextureTransformer transformer : this.transformers) {
                nativeImage = transformer.transform(nativeImage, manager, this.stringConverter);
            }
        }

        return new TextureContents(nativeImage, metadata);
    }

}
