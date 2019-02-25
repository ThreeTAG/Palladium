package com.threetag.threecore;

import com.threetag.threecore.materials.ThreeCoreMaterials;
import com.threetag.threecore.proxies.ClientProxy;
import com.threetag.threecore.proxies.IProxy;
import com.threetag.threecore.proxies.ServerProxy;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

@Mod(ThreeCore.MODID)
public class ThreeCore {

    public static final String MODID = "threecore";
    public static final Logger LOGGER = LogManager.getLogger();

    public ThreeCore() {
        RecipeUtil.init();

        new ThreeCoreMaterials();
    }

    public static ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
        }
    };

}
