package net.threetag.palladium.client.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TextureReference {

    public static final Codec<TextureReference> CODEC = Codec.STRING.comapFlatMap(TextureReference::read, TextureReference::toString).stable();
    public static final StreamCodec<ByteBuf, TextureReference> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, tr -> tr.dynamic,
            ResourceLocation.STREAM_CODEC, tr -> tr.path,
            TextureReference::new
    );

    private final boolean dynamic;
    private final ResourceLocation path;

    private TextureReference(boolean dynamic, ResourceLocation path) {
        this.dynamic = dynamic;
        this.path = path;
    }

    public ResourceLocation withPath(DataContext context, String prefix, String suffix) {
        var texture = this.getTexture(context);

        if (texture == null) {
            return null;
        }

        if (texture.getPath().endsWith(".png")) {
            return texture;
        }

        return texture.withPrefix(prefix).withSuffix(suffix);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture(DataContext context) {
        if (this.dynamic) {
            var dyn = DynamicTextureManager.INSTANCE.get(this.path);
            return dyn != null ? dyn.getTexture(context) : null;
        } else {
            return this.path;
        }
    }

    public static TextureReference normal(ResourceLocation path) {
        return new TextureReference(false, path);
    }

    public static TextureReference dynamic(ResourceLocation path) {
        return new TextureReference(true, path);
    }

    public static TextureReference parse(String path) {
        if (path.startsWith("#")) {
            return dynamic(ResourceLocation.parse(path.substring(1)));
        }

        return normal(ResourceLocation.parse(path));
    }

    public static DataResult<TextureReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid texture reference: " + path + " " + e.getMessage());
        }
    }

    public ResourceLocation getPath() {
        return this.path;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    @Override
    public String toString() {
        return (this.dynamic ? "#" : "") + this.path.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureReference that = (TextureReference) o;
        return dynamic == that.dynamic && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dynamic, path);
    }

}
