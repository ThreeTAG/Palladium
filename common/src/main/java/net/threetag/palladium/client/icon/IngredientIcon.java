package net.threetag.palladium.client.icon;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.logic.context.DataContext;

public record IngredientIcon(Ingredient ingredient) implements Icon {

    public static final MapCodec<IngredientIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientIcon::ingredient))
            .apply(instance, IngredientIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientIcon> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, IngredientIcon::ingredient, IngredientIcon::new
    );

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var stack = guiGraphics.pose();
        stack.pushMatrix();
        stack.translate(x + width / 2F, y + height / 2F);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F);
        }

        var items = this.ingredient.items().toList();
        int stackIndex = (int) ((System.currentTimeMillis() / 1000) % items.size());
        guiGraphics.renderItem(items.get(stackIndex).value().getDefaultInstance(), -width / 2, -height / 2);
        stack.popMatrix();
    }

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
