package net.threetag.threecore.compat.jei.constructiontable;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.base.recipe.LeggingsCraftingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;

public class LeggingsCraftingCategory extends AbstractConstructionTableCategory<LeggingsCraftingRecipe> {

    public LeggingsCraftingCategory(IGuiHelper guiHelper) {
        super(I18n.format("gui.jei.category.threecore.leggings_crafting"),
                guiHelper.drawableBuilder(GUI_TEXTURE_1, 0, 0, 141, 104).build(),
                guiHelper.createDrawableIngredient(new ItemStack(Items.IRON_LEGGINGS)));
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.LEGGINGS_CRAFTING_CATEGORY;
    }

    @Override
    public Class<? extends LeggingsCraftingRecipe> getRecipeClass() {
        return LeggingsCraftingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LeggingsCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(0, true, 8, 8);
        itemStacks.init(1, true, 26, 8);
        itemStacks.init(2, true, 44, 8);

        itemStacks.init(3, true, 8, 26);
        itemStacks.init(4, true, 44, 26);

        itemStacks.init(5, true, 8, 44);
        itemStacks.init(6, true, 44, 44);

        itemStacks.init(7, true, 8, 62);
        itemStacks.init(8, true, 44, 62);

        itemStacks.init(9, true, 108, 29);
        itemStacks.init(10, false, 119, 82);

        itemStacks.set(ingredients);
    }
}
