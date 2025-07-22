package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.texture.DynamicTexture;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.client.variable.DynamicTextureManager;
import net.threetag.palladium.data.DataContext;

import java.util.Objects;

public class PackRenderLayerTexture {

    public static final Codec<PackRenderLayerTexture> CODEC = Codec.either(
            DynamicTexture.Codecs.SIMPLE_CODEC,
            TextureReference.CODEC
    ).xmap(either -> either.map(
            PackRenderLayerTexture::new,
            PackRenderLayerTexture::new
    ), t -> t.textureReference != null ? Either.right(t.textureReference) : Either.left(t.dynamicTexture));

    private DynamicTexture dynamicTexture;
    private final TextureReference textureReference;

    public PackRenderLayerTexture(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
        this.textureReference = null;
    }

    public PackRenderLayerTexture(TextureReference textureReference) {
        this.dynamicTexture = null;
        this.textureReference = textureReference;
    }

    public PackRenderLayerTexture(ResourceLocation texture) {
        this.dynamicTexture = null;
        this.textureReference = TextureReference.normal(texture);
    }

    public ResourceLocation getTexture(DataContext context) {
        if (this.textureReference != null && this.textureReference.isDynamic()) {
            this.dynamicTexture = DynamicTextureManager.INSTANCE.get(this.textureReference.getPath());
        }

        return this.dynamicTexture != null ? this.dynamicTexture.getTexture(context) : Objects.requireNonNull(this.textureReference).getTexture(context);
    }
}
