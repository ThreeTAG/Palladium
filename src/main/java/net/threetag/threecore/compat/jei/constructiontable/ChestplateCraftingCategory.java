package net.threetag.threecore.compat.jei.constructiontable;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.item.recipe.ChestplateCraftingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;

public class ChestplateCraftingCategory extends AbstractConstructionTableCategory<ChestplateCraftingRecipe> {

    public ChestplateCraftingCategory(IGuiHelper guiHelper) {
        super(I18n.format("gui.jei.category.threecore.chestplate_crafting"),
                guiHelper.drawableBuilder(GUI_TEXTURE_0, 0, 104, 162, 104).build(),
                guiHelper.createDrawableIngredient(new ItemStack(Items.IRON_CHESTPLATE)));
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.CHESTPLATE_CRAFTING_CATEGORY;
    }

    @Override
    public Class<? extends ChestplateCraftingRecipe> getRecipeClass() {
        return ChestplateCraftingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ChestplateCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(0, true, 11, 11);
        itemStacks.init(1, true, 29, 11);
        itemStacks.init(2, true, 47, 11);
        itemStacks.init(3, true, 65, 11);
        itemStacks.init(4, true, 83, 11);

        itemStacks.init(5, true, 5, 29);
        itemStacks.init(6, true, 29, 29);
        itemStacks.init(7, true, 47, 29);
        itemStacks.init(8, true, 65, 29);
        itemStacks.init(9, true, 89, 29);

        itemStacks.init(10, true, 5, 47);
        itemStacks.init(11, true, 29, 47);
        itemStacks.init(12, true, 47, 47);
        itemStacks.init(13, true, 65, 47);
        itemStacks.init(14, true, 89, 47);

        itemStacks.init(15, true, 29, 65);
        itemStacks.init(16, true, 47, 65);
        itemStacks.init(17, true, 65, 65);

        itemStacks.init(18, true, 129, 29);
        itemStacks.init(19, false, 140, 82);

        itemStacks.set(ingredients);
    }
}
