package net.threetag.palladium.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.customization.EyeSelectionUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.util.PalladiumCodecs;

public class EyeSelectionTexture extends DynamicTexture {

    public static final MapCodec<EyeSelectionTexture> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.COLOR_INT_CODEC.optionalFieldOf("color", RenderUtil.FULL_WHITE).forGetter(t -> t.color)
    ).apply(instance, EyeSelectionTexture::new));

    public final int color;

    public EyeSelectionTexture(int color) {
        this.color = color;
    }

    @Override
    public Identifier getTexture(DataContext context) {
        var eyeSelection = context.get(DataContextKeys.EYE_SELECTION);
        var texturePath = Palladium.id("eye_selection_texture/" + eyeSelection + "/" + this.color);

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(texturePath)) {
            Minecraft.getInstance().getTextureManager().registerAndLoad(texturePath, new Texture(texturePath, eyeSelection, this.color));
        }

        return texturePath;
    }

    @Override
    public DynamicTextureSerializer<?> getSerializer() {
        return DynamicTextureSerializers.EYE_SELECTION;
    }

    public static class Serializer extends DynamicTextureSerializer<EyeSelectionTexture> {

        @Override
        public MapCodec<EyeSelectionTexture> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<DynamicTexture, EyeSelectionTexture> builder, HolderLookup.Provider provider) {
            builder.setName("Eye Selection Texture").setDescription("Creates a texture based the eye selection of the player.")
                    .addOptional("color", TYPE_COLOR, "The color that will be used");
        }
    }

    public static class Texture extends SimpleTexture {

        private final long eyeSelection;
        private final int color;

        public Texture(Identifier id, long eyeSelection, int color) {
            super(id);
            this.eyeSelection = eyeSelection;
            this.color = color;
        }

        @Override
        public TextureContents loadContents(ResourceManager resourceManager) {
            NativeImage nativeImage = new NativeImage(64, 64, true);

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (EyeSelectionUtil.isPixelSelected(this.eyeSelection, x, y)) {
                        nativeImage.setPixel(8 + x, 8 + y, this.color);
                    }
                }
            }

            return new TextureContents(nativeImage, null);
        }
    }
}
