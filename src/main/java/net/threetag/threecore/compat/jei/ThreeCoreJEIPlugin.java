package net.threetag.threecore.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.client.gui.inventory.ConstructionTableScreen;
import net.threetag.threecore.client.gui.inventory.FluidComposerScreen;
import net.threetag.threecore.client.gui.inventory.GrinderScreen;
import net.threetag.threecore.client.gui.inventory.HydraulicPressScreen;
import net.threetag.threecore.item.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;
import net.threetag.threecore.compat.jei.constructiontable.BootsCraftingCategory;
import net.threetag.threecore.compat.jei.constructiontable.ChestplateCraftingCategory;
import net.threetag.threecore.compat.jei.constructiontable.HelmetCraftingCategory;
import net.threetag.threecore.compat.jei.constructiontable.LeggingsCraftingCategory;
import net.threetag.threecore.compat.jei.fluidcomposing.FluidComposingCategory;
import net.threetag.threecore.compat.jei.grinding.GrindingCategory;
import net.threetag.threecore.compat.jei.pressing.PressingCategory;
import net.threetag.threecore.container.*;

import java.util.Objects;

@JeiPlugin
public class ThreeCoreJEIPlugin implements IModPlugin {

    public static final ResourceLocation RECIPE_GUI_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/jei.png");
    public static final ResourceLocation HELMET_CRAFTING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "helmet_crafting");
    public static final ResourceLocation CHESTPLATE_CRAFTING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "chestplate_crafting");
    public static final ResourceLocation LEGGINGS_CRAFTING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "leggings_crafting");
    public static final ResourceLocation BOOTS_CRAFTING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "boots_crafting");
    public static final ResourceLocation GRINDING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "grinding");
    public static final ResourceLocation PRESSING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "pressing");
    public static final ResourceLocation FLUID_COMPOSING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "fluid_composing");

    private HelmetCraftingCategory helmetCraftingCategory;
    private ChestplateCraftingCategory chestplateCraftingCategory;
    private LeggingsCraftingCategory leggingsCraftingCategory;
    private BootsCraftingCategory bootsCraftingCategory;
    private GrindingCategory grindingCategory;
    private PressingCategory pressingCategory;
    private FluidComposingCategory fluidComposingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ThreeCore.MODID, "plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                helmetCraftingCategory = new HelmetCraftingCategory(guiHelper),
                chestplateCraftingCategory = new ChestplateCraftingCategory(guiHelper),
                leggingsCraftingCategory = new LeggingsCraftingCategory(guiHelper),
                bootsCraftingCategory = new BootsCraftingCategory(guiHelper),
                grindingCategory = new GrindingCategory(guiHelper),
                pressingCategory = new PressingCategory(guiHelper),
                fluidComposingCategory = new FluidComposingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Objects.requireNonNull(helmetCraftingCategory);
        Objects.requireNonNull(chestplateCraftingCategory);
        Objects.requireNonNull(leggingsCraftingCategory);
        Objects.requireNonNull(bootsCraftingCategory);
        Objects.requireNonNull(grindingCategory);
        Objects.requireNonNull(pressingCategory);
        Objects.requireNonNull(fluidComposingCategory);

        registration.addRecipes(ThreeCoreRecipeFactory.getHelmetCraftingRecipes(), HELMET_CRAFTING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getChestplateCraftingRecipes(), CHESTPLATE_CRAFTING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getLeggingsCraftingRecipes(), LEGGINGS_CRAFTING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getBootsCraftingRecipes(), BOOTS_CRAFTING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getGrinderRecipes(), GRINDING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getPressingRecipes(), PRESSING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getFluidComposingRecipes(), FLUID_COMPOSING_CATEGORY);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ConstructionTableScreen.Helmet.class, 94, 96, 45, 21, HELMET_CRAFTING_CATEGORY);
        registration.addRecipeClickArea(ConstructionTableScreen.Chestplate.class, 94, 93, 45, 24, CHESTPLATE_CRAFTING_CATEGORY);
        registration.addRecipeClickArea(ConstructionTableScreen.Leggings.class, 88, 96, 51, 21, LEGGINGS_CRAFTING_CATEGORY);
        registration.addRecipeClickArea(ConstructionTableScreen.Boots.class, 94, 93, 45, 24, BOOTS_CRAFTING_CATEGORY);
        registration.addRecipeClickArea(GrinderScreen.class, 66, 36, 28, 23, GRINDING_CATEGORY);
        registration.addRecipeClickArea(HydraulicPressScreen.class, 96, 36, 28, 23, PRESSING_CATEGORY);
        registration.addRecipeClickArea(FluidComposerScreen.class, 107, 44, 28, 23, FLUID_COMPOSING_CATEGORY);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        this.addConstructionTableTransferHandler(registration, HELMET_CRAFTING_CATEGORY, HelmetCraftingContainer.class, TCRecipeSerializers.HELMET_CRAFTING.get());
        this.addConstructionTableTransferHandler(registration, CHESTPLATE_CRAFTING_CATEGORY, ChestplateCraftingContainer.class, TCRecipeSerializers.CHESTPLATE_CRAFTING.get());
        this.addConstructionTableTransferHandler(registration, LEGGINGS_CRAFTING_CATEGORY, LeggingsCraftingContainer.class, TCRecipeSerializers.LEGGINGS_CRAFTING.get());
        this.addConstructionTableTransferHandler(registration, BOOTS_CRAFTING_CATEGORY, BootsCraftingContainer.class, TCRecipeSerializers.BOOTS_CRAFTING.get());
        registration.addRecipeTransferHandler(GrinderContainer.class, GRINDING_CATEGORY, 1, 1, 4, 36);
        registration.addRecipeTransferHandler(HydraulicPressContainer.class, PRESSING_CATEGORY, 1, 2, 4, 36);
        registration.addRecipeTransferHandler(FluidComposerContainer.class, FLUID_COMPOSING_CATEGORY, 5, 9, 14, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.CONSTRUCTION_TABLE.get()), HELMET_CRAFTING_CATEGORY, CHESTPLATE_CRAFTING_CATEGORY, LEGGINGS_CRAFTING_CATEGORY, BOOTS_CRAFTING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.GRINDER.get()), GRINDING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.HYDRAULIC_PRESS.get()), PRESSING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.FLUID_COMPOSER.get()), FLUID_COMPOSING_CATEGORY);
    }

    public void addConstructionTableTransferHandler(IRecipeTransferRegistration registration, ResourceLocation id, Class<? extends AbstractConstructionTableContainer> clazz, IRecipeSerializer recipeSerializer) {
        AbstractConstructionTableRecipe.Serializer serializer = (AbstractConstructionTableRecipe.Serializer) recipeSerializer;
        registration.addRecipeTransferHandler(clazz, id, 0, serializer.size + 1, serializer.size, 36);
    }
}