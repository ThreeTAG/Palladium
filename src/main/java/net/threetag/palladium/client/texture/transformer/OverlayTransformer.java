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

public class OverlayTransformer extends TextureTransformer {

    public static final MapCodec<OverlayTransformer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("overlay").forGetter(t -> t.overlayLocation),
            Codec.BOOL.optionalFieldOf("ignore_blank", false).forGetter(t -> t.ignoreBlank)
    ).apply(instance, OverlayTransformer::new));

    private final String overlayLocation;
    private final boolean ignoreBlank;

    public OverlayTransformer(String overlayLocation, boolean ignoreBlank) {
        this.overlayLocation = overlayLocation;
        this.ignoreBlank = ignoreBlank;
    }

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var maskTexture = manager.getResourceOrThrow(Identifier.parse(stringConverter.apply(this.overlayLocation)));

        try (InputStream inputStream = maskTexture.open()) {
            NativeImage overlay = NativeImage.read(inputStream);

            for (int y = 0; y < overlay.getHeight(); ++y) {
                for (int x = 0; x < overlay.getWidth(); ++x) {
                    blendPixel(texture, x, y, overlay.getPixel(x, y));
                }
            }

            overlay.close();
        }

        return texture;
    }

    public void blendPixel(NativeImage texture, int x, int y, int overlayPixel) {
        if (texture.format() != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int i = texture.getPixel(x, y);
            float overlayA = (float) ((overlayPixel >> 24) & 0xFF) / 255.0F;
            float overlayB = (float) (overlayPixel & 0xFF) / 255.0F;
            float overlayG = (float) ((overlayPixel >> 8) & 0xFF) / 255.0F;
            float overlayR = (float) ((overlayPixel >> 16) & 0xFF) / 255.0F;
            float baseA = (float) ((i >> 24) & 0xFF) / 255.0F;
            float baseB = (float) (i & 0xFF) / 255.0F;
            float baseG = (float) ((i >> 8) & 0xFF) / 255.0F;
            float baseR = (float) ((i >> 16) & 0xFF) / 255.0F;
            if (baseA == 0 & ignoreBlank) return;
            float p = 1.0F - overlayA;
            float newA = overlayA * overlayA + baseA * p;
            float newB = overlayB * overlayA + baseB * p;
            float newG = overlayG * overlayA + baseG * p;
            float newR = overlayR * overlayA + baseR * p;

            if (newA > 1.0F) {
                newA = 1.0F;
            }

            if (newB > 1.0F) {
                newB = 1.0F;
            }

            if (newG > 1.0F) {
                newG = 1.0F;
            }

            if (newR > 1.0F) {
                newR = 1.0F;
            }

            int u = (int) (newA * 255.0F);
            int v = (int) (newB * 255.0F);
            int w = (int) (newG * 255.0F);
            int z = (int) (newR * 255.0F);
            int newPixel = (u << 24) | (z << 16) | (w << 8) | v;
            texture.setPixel(x, y, newPixel);
        }
    }

    @Override
    public TextureTransformerSerializer<?> getSerializer() {
        return TextureTransformerSerializers.OVERLAY;
    }

    public static class Serializer extends TextureTransformerSerializer<OverlayTransformer> {

        @Override
        public MapCodec<OverlayTransformer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<TextureTransformer, OverlayTransformer> builder, HolderLookup.Provider provider) {
            builder.setName("Overlay").setDescription("Puts another texture on top of the base texture.")
                    .add("overlay", TYPE_RESOURCE_LOCATION, "The location of the overlay texture. Can use variables")
                    .addOptional("ignore_blank", TYPE_BOOLEAN, "If true, the overlay will not be applied to blank pixels.", false)
                    .setExampleObject(new OverlayTransformer("example:mask", false));
        }
    }
}
