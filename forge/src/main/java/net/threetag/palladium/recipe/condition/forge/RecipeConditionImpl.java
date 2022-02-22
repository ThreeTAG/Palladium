package net.threetag.palladium.recipe.condition.forge;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public class RecipeConditionImpl {

    @SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable"})
    public static IRecipeConditionSerializer<?> register(IRecipeConditionSerializer<?> serializer) {
        IRecipeConditionSerializer serializer1 = serializer;
        CraftingHelper.register(new IConditionSerializer() {
            @SuppressWarnings("unchecked")
            @Override
            public void write(JsonObject jsonObject, ICondition iCondition) {
                IRecipeCondition condition = toCustom(iCondition);
                serializer1.write(jsonObject, condition);
            }

            @Override
            public ICondition read(JsonObject jsonObject) {
                return toForge(serializer.read(jsonObject));
            }

            @Override
            public ResourceLocation getID() {
                return serializer.getID();
            }
        });
        return serializer;
    }

    public static ICondition toForge(IRecipeCondition condition) {
        return new ICondition() {
            @Override
            public ResourceLocation getID() {
                return condition.getID();
            }

            @Override
            public boolean test() {
                return condition.test();
            }
        };
    }

    public static IRecipeCondition toCustom(ICondition condition) {
        return new IRecipeCondition() {
            @Override
            public ResourceLocation getID() {
                return condition.getID();
            }

            @Override
            public boolean test() {
                return condition.test();
            }
        };
    }

}
