package net.threetag.threecore.compat.jei.constructiontable;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.item.recipe.HelmetCraftingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;

public class HelmetCraftingCategory extends AbstractConstructionTableCategory<HelmetCraftingRecipe> {

    public HelmetCraftingCategory(IGuiHelper guiHelper) {
        super(I18n.format("gui.jei.category.threecore.helmet_crafting"),
                guiHelper.drawableBuilder(GUI_TEXTURE_0, 0, 0, 151, 104).build(),
                guiHelper.createDrawableIngredient(new ItemStack(Items.IRON_HELMET)));
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.HELMET_CRAFTING_CATEGORY;
    }

    @Override
    public Class<? extends HelmetCraftingRecipe> getRecipeClass() {
        return HelmetCraftingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, HelmetCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                itemStacks.init(x + y * 4, true, 9 + x * 18, 5 + y * 18);
            }
        }

        itemStacks.init(16, true, 118, 29);
        itemStacks.init(17, false, 129, 82);

        itemStacks.set(ingredients);

    }
}
