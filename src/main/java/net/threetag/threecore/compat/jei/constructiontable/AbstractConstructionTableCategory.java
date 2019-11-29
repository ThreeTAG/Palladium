package net.threetag.threecore.compat.jei.constructiontable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.recipe.AbstractConstructionTableRecipe;

public abstract class AbstractConstructionTableCategory<T extends AbstractConstructionTableRecipe> implements IRecipeCategory<T> {

    public static final ResourceLocation GUI_TEXTURE_0 = new ResourceLocation(ThreeCore.MODID, "textures/gui/jei_construction_table_0.png");
    public static final ResourceLocation GUI_TEXTURE_1 = new ResourceLocation(ThreeCore.MODID, "textures/gui/jei_construction_table_1.png");

    private final String title;
    private final IDrawable background;
    private final IDrawable icon;

    public AbstractConstructionTableCategory(String title, IDrawable background, IDrawable icon) {
        this.title = title;
        this.background = background;
        this.icon = icon;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

}
