package net.threetag.threecore.base.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseRecipeSerializers {

    @ObjectHolder("grinding")
    public static final IRecipeSerializer<GrinderRecipe> GRINDING = null;

    @ObjectHolder("pressing")
    public static final IRecipeSerializer<PressingRecipe> PRESSING = null;

    @ObjectHolder("fluid_composing")
    public static final IRecipeSerializer<FluidComposingRecipe> FLUID_COMPOSING = null;

    @SubscribeEvent
    public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        e.getRegistry().register(new GrinderRecipe.Serializer().setRegistryName(ThreeCore.MODID, "grinding"));
        e.getRegistry().register(new PressingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "pressing"));
        e.getRegistry().register(new FluidComposingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "fluid_composing"));
    }

}
