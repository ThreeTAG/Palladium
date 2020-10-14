package net.threetag.threecore.item.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;

public class TCRecipeSerializers {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ThreeCore.MODID);

    public static final RegistryObject<IRecipeSerializer<HelmetCraftingRecipe>> HELMET_CRAFTING = RECIPE_SERIALIZERS.register("helmet_crafting", HelmetCraftingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<ChestplateCraftingRecipe>> CHESTPLATE_CRAFTING = RECIPE_SERIALIZERS.register("chestplate_crafting", ChestplateCraftingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<LeggingsCraftingRecipe>> LEGGINGS_CRAFTING = RECIPE_SERIALIZERS.register("leggings_crafting", LeggingsCraftingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<BootsCraftingRecipe>> BOOTS_CRAFTING = RECIPE_SERIALIZERS.register("boots_crafting", BootsCraftingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<GrindingRecipe>> GRINDING = RECIPE_SERIALIZERS.register("grinding", GrindingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<PressingRecipe>> PRESSING = RECIPE_SERIALIZERS.register("pressing", PressingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<FluidComposingRecipe>> FLUID_COMPOSING = RECIPE_SERIALIZERS.register("fluid_composing", FluidComposingRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<MultiversalRecipe>> MULTIVERSAL = RECIPE_SERIALIZERS.register("multiversal", MultiversalRecipe.Serializer::new);
}
