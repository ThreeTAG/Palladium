package net.threetag.threecore.loot.function;

import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.threetag.threecore.ThreeCore;

public class TCLootFunctions {

    public static LootFunctionType COPY_ENERGY;
    //TODO move to forge registry when one is created

    public static void register() {
        COPY_ENERGY = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(ThreeCore.MODID, "copy_energy"), new LootFunctionType(new CopyEnergyFunction.Serializer()));
    }

}
