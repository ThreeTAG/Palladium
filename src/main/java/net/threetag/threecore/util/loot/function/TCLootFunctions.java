package net.threetag.threecore.util.loot.function;

import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class TCLootFunctions {

    public static void register() {
        LootFunctionManager.registerFunction(new CopyEnergyFunction.Serializer());
    }

}
