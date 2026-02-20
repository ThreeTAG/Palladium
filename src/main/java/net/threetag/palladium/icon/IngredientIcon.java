package net.threetag.palladium.icon;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

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

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, IngredientIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Ingredient").setDescription("An icon that will display all items of an ingredient by cycling through them")
                    .add("ingredient", TYPE_INGREDIENT, "The ingredient (check vanilla recipes for how to define ingredients)")
                    .addExampleObject(new IngredientIcon(Ingredient.of(Items.APPLE, Items.BREAD)))
                    .addExampleObject(new IngredientIcon(Ingredient.of(provider.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.PLANKS))));
        }
    }

}
