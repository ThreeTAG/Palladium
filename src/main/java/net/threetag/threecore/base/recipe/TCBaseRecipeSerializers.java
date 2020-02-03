package net.threetag.threecore.base.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseRecipeSerializers {

    @ObjectHolder("helmet_crafting")
    public static final IRecipeSerializer<HelmetCraftingRecipe> HELMET_CRAFTING = null;

    @ObjectHolder("chestplate_crafting")
    public static final IRecipeSerializer<ChestplateCraftingRecipe> CHESTPLATE_CRAFTING = null;

    @ObjectHolder("leggings_crafting")
    public static final IRecipeSerializer<LeggingsCraftingRecipe> LEGGINGS_CRAFTING = null;

    @ObjectHolder("boots_crafting")
    public static final IRecipeSerializer<BootsCraftingRecipe> BOOTS_CRAFTING = null;

    @ObjectHolder("grinding")
    public static final IRecipeSerializer<GrindingRecipe> GRINDING = null;

    @ObjectHolder("pressing")
    public static final IRecipeSerializer<PressingRecipe> PRESSING = null;

    @ObjectHolder("fluid_composing")
    public static final IRecipeSerializer<FluidComposingRecipe> FLUID_COMPOSING = null;

    @SubscribeEvent
    public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        e.getRegistry().register(new HelmetCraftingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "helmet_crafting"));
        e.getRegistry().register(new ChestplateCraftingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "chestplate_crafting"));
        e.getRegistry().register(new LeggingsCraftingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "leggings_crafting"));
        e.getRegistry().register(new BootsCraftingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "boots_crafting"));
        e.getRegistry().register(new GrindingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "grinding"));
        e.getRegistry().register(new PressingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "pressing"));
        e.getRegistry().register(new FluidComposingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "fluid_composing"));
    }
}