package net.threetag.palladium.client.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.IdentifierException;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ModelLayerLocationCodec(Identifier model, String layer) {

    public static final Codec<ModelLayerLocationCodec> CODEC = Codec.STRING.comapFlatMap(ModelLayerLocationCodec::read, ModelLayerLocationCodec::toString).stable();

    public static ModelLayerLocationCodec parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new ModelLayerLocationCodec(Identifier.parse(s[0]), "main");
        } else {
            return new ModelLayerLocationCodec(Identifier.parse(s[0]), s[1]);
        }
    }

    public static DataResult<ModelLayerLocationCodec> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (IdentifierException e) {
            return DataResult.error(() -> "Not a valid model layer location: " + path + " " + e.getMessage());
        }
    }

    public ModelLayerLocation get() {
        return new ModelLayerLocation(this.model, this.layer);
    }

    @Override
    public @NotNull String toString() {
        if (this.layer.equals("main")) {
            return this.model.toString();
        }

        return this.model.toString() + "#" + this.layer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ModelLayerLocationCodec(Identifier model1, String layer1))) return false;
        return Objects.equals(layer, layer1) && Objects.equals(model, model1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, layer);
    }
}
