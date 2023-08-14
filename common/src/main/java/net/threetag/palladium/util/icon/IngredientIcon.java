package net.threetag.palladium.util.icon;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.context.DataContext;

public record IngredientIcon(Ingredient ingredient) implements IIcon {

    @Override
    public void draw(Minecraft mc, DataContext context, PoseStack stack, int x, int y, int width, int height) {
        stack.pushPose();
        stack.translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F, s / 16F);
        }

        int stackIndex = (int) ((System.currentTimeMillis() / 1000) % this.ingredient.getItems().length);
        GuiUtil.drawItem(stack, this.ingredient.getItems()[stackIndex], 0, true, null);
        stack.popPose();
    }

    @Override
    public IconSerializer<IngredientIcon> getSerializer() {
        return IconSerializers.INGREDIENT.get();
    }

    @Override
    public String toString() {
        return "IngredientIcon{" +
                "ingredient=" + ingredient.toJson() +
                '}';
    }

    public static class Serializer extends IconSerializer<IngredientIcon> {

        @Override
        public IngredientIcon fromJSON(JsonObject json) {
            return new IngredientIcon(Ingredient.fromJson(json.get("ingredient")));
        }

        @Override
        public IngredientIcon fromNBT(CompoundTag nbt) {
            return new IngredientIcon(Ingredient.fromJson(GsonHelper.parse(nbt.getString("Ingredient"))));
        }

        @Override
        public JsonObject toJSON(IngredientIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("ingredient", icon.ingredient.toJson());
            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(IngredientIcon icon) {
            CompoundTag tag = new CompoundTag();
            tag.putString("Ingredient", icon.ingredient.toJson().toString());
            return tag;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Ingredient Icon");
            builder.setDescription("Circles through the items of an ingredient.");

            builder.addProperty("ingredient", Ingredient.class)
                    .description("Ingredient for the item")
                    .required().exampleJson(Ingredient.of(ItemTags.ARROWS).toJson());
        }
    }

}
