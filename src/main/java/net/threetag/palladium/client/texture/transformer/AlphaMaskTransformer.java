package net.threetag.palladium.client.texture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class AlphaMaskTransformer extends TextureTransformer {

    public static final MapCodec<AlphaMaskTransformer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("mask").forGetter(t -> t.maskLocation)
    ).apply(instance, AlphaMaskTransformer::new));

    private final String maskLocation;

    public AlphaMaskTransformer(String maskLocation) {
        this.maskLocation = maskLocation;
    }

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var maskTexture = manager.getResourceOrThrow(Identifier.parse(stringConverter.apply(this.maskLocation)));

        try (InputStream inputStream = maskTexture.open()) {
            NativeImage overlay = NativeImage.read(inputStream);

            for (int y = 0; y < overlay.getHeight(); ++y) {
                for (int x = 0; x < overlay.getWidth(); ++x) {
                    int pixelOrig = texture.getPixel(x, y);
                    int pixelMask = overlay.getPixel(x, y);

                    if (pixelOrig != 0) {
                        int r = (pixelMask >> 16) & 0xFF;
                        int g = (pixelMask >> 8) & 0xFF;
                        int b = pixelMask & 0xFF;
                        float hue = 1F - (r + g + b) / (3F * 255F);

                        int aOrig = (pixelOrig >> 24) & 0xFF;
                        int rOrig = (pixelOrig >> 16) & 0xFF;
                        int gOrig = (pixelOrig >> 8) & 0xFF;
                        int bOrig = pixelOrig & 0xFF;

                        int newAlpha = pixelMask == 0 ? 0 : Math.min(255, Math.max(0, (int) (aOrig * hue)));

                        int newPixel = (newAlpha << 24) | (rOrig << 16) | (gOrig << 8) | bOrig;
                        texture.setPixel(x, y, newPixel);
                    }
                }
            }

            overlay.close();
        }

        return texture;
    }

    @Override
    public TextureTransformerSerializer<?> getSerializer() {
        return TextureTransformerSerializers.ALPHA_MASK;
    }

    public static class Serializer extends TextureTransformerSerializer<AlphaMaskTransformer> {

        @Override
        public MapCodec<AlphaMaskTransformer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<TextureTransformer, AlphaMaskTransformer> builder, HolderLookup.Provider provider) {
            builder.setName("Alpha Mask").setDescription("Applies an alpha mask to the texture.")
                    .add("mask", TYPE_RESOURCE_LOCATION, "The location of the mask texture. Can use variables")
                    .setExampleObject(new AlphaMaskTransformer("example:mask"));
        }
    }
}
