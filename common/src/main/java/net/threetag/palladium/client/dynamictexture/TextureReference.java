package net.threetag.palladium.client.dynamictexture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TextureReference {

    private final boolean dynamic;
    private final ResourceLocation path;

    private TextureReference(boolean dynamic, ResourceLocation path) {
        this.dynamic = dynamic;
        this.path = path;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture(Entity entity) {
        if (this.dynamic) {
            var dyn = DynamicTextureManager.INSTANCE.get(this.path);
            return dyn != null ? dyn.getTexture(entity) : null;
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
            return dynamic(new ResourceLocation(path.substring(1)));
        }

        return normal(new ResourceLocation(path));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeBoolean(this.dynamic);
        buf.writeResourceLocation(this.path);
    }

    public static TextureReference fromBuffer(FriendlyByteBuf buf) {
        return new TextureReference(buf.readBoolean(), buf.readResourceLocation());
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
