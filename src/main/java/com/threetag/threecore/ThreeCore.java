package com.threetag.threecore;

import com.threetag.threecore.materials.ThreeCoreMaterials;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ThreeCore.MODID)
public class ThreeCore {

    public static final String MODID = "threecore";
    public static final Logger LOGGER = LogManager.getLogger();

    public ThreeCore() {
        RecipeUtil.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThreeCoreCommonConfig.generateConfig());

        new ThreeCoreMaterials();
    }

    public static ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
        }
    };

}
