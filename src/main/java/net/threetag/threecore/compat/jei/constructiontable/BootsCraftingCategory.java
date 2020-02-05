package net.threetag.threecore.compat.jei.constructiontable;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.item.recipe.BootsCraftingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;

public class BootsCraftingCategory extends AbstractConstructionTableCategory<BootsCraftingRecipe> {

    public BootsCraftingCategory(IGuiHelper guiHelper) {
        super(I18n.format("gui.jei.category.threecore.boots_crafting"),
                guiHelper.drawableBuilder(GUI_TEXTURE_1, 0, 104, 155, 104).build(),
                guiHelper.createDrawableIngredient(new ItemStack(Items.IRON_BOOTS)));
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.BOOTS_CRAFTING_CATEGORY;
    }

    @Override
    public Class<? extends BootsCraftingRecipe> getRecipeClass() {
        return BootsCraftingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BootsCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(0, true, 21, 7);
        itemStacks.init(1, true, 59, 7);

        itemStacks.init(2, true, 21, 25);
        itemStacks.init(3, true, 59, 25);

        itemStacks.init(4, true, 3, 50);
        itemStacks.init(5, true, 21, 43);

        itemStacks.init(6, true, 59, 43);
        itemStacks.init(7, true, 77, 50);

        itemStacks.init(8, true, 122, 24);
        itemStacks.init(9, false, 133, 77);

        itemStacks.set(ingredients);
    }
}
