package net.threetag.palladium.icon;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientIcon(Ingredient ingredient) implements Icon {

    public static final MapCodec<IngredientIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientIcon::ingredient))
            .apply(instance, IngredientIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientIcon> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, IngredientIcon::ingredient, IngredientIcon::new
    );

    @Override
    public IconSerializer<IngredientIcon> getSerializer() {
        return IconSerializers.INGREDIENT.get();
    }

    @Override
    public String toString() {
        return "IngredientIcon{" +
                "ingredient=" + Ingredient.CODEC.encodeStart(JsonOps.COMPRESSED, ingredient).getOrThrow().toString() +
                '}';
    }

    public static class Serializer extends IconSerializer<IngredientIcon> {

        @Override
        public MapCodec<IngredientIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IngredientIcon> streamCodec() {
            return STREAM_CODEC;
        }

    }

}
