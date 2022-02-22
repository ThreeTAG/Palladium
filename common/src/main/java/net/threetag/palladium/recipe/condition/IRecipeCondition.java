package net.threetag.palladium.recipe.condition;

import net.minecraft.resources.ResourceLocation;

public interface IRecipeCondition {

    ResourceLocation getID();

    boolean test();

}
