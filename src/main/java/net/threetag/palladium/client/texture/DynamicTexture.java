package net.threetag.palladium.client.texture;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.logic.context.DataContext;

public abstract class DynamicTexture {

    public abstract Identifier getTexture(DataContext context);

    public abstract DynamicTextureSerializer<?> getSerializer();

    public static class Codecs {

        public static final Codec<DynamicTexture> DIRECT_CODEC = Codec.withAlternative(DynamicTextureSerializer.TYPE_CODEC.dispatch(DynamicTexture::getSerializer, DynamicTextureSerializer::codec), ConfiguredTexture.CODEC.codec());

        public static final Codec<DynamicTexture> SIMPLE_CODEC = Codec.either(DIRECT_CODEC, StaticTexture.CODEC.codec()).xmap(
                either -> either.map(
                        dynamicTexture -> dynamicTexture,
                        staticTexture -> staticTexture
                ),
                dynamicTexture -> dynamicTexture instanceof StaticTexture staticT ? Either.right(staticT) : Either.left(dynamicTexture)
        );
    }

}
