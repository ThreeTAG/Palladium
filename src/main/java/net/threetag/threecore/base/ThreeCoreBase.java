package net.threetag.threecore.base;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.block.*;
import net.threetag.threecore.base.client.renderer.entity.SuitStandRenderer;
import net.threetag.threecore.base.entity.SuitStandEntity;
import net.threetag.threecore.base.entity.TCBaseEntityTypes;
import net.threetag.threecore.base.inventory.*;
import net.threetag.threecore.base.item.*;
import net.threetag.threecore.base.recipe.FluidComposingRecipe;
import net.threetag.threecore.base.recipe.GrinderRecipe;
import net.threetag.threecore.base.recipe.PressingRecipe;
import net.threetag.threecore.base.recipe.TCBaseRecipeSerializers;
import net.threetag.threecore.base.tileentity.*;
import net.threetag.threecore.util.item.ItemGroupRegistry;


public class ThreeCoreBase {

    public ThreeCoreBase() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseBlocks());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseContainerTypes());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseItems());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseEntityTypes());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseRecipeSerializers());
    }

}
