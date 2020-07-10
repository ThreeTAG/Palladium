package net.threetag.threecore.loot.function;

import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.threetag.threecore.ThreeCore;

public class TCLootFunctions {

    public static LootFunctionType COPY_ENERGY;
    //TODO move to forge registry when one is created
    @SubscribeEvent
    public void register(FMLCommonSetupEvent event) {
        COPY_ENERGY = Registry.register(Registry.field_239694_aZ_, new ResourceLocation(ThreeCore.MODID, "copy_energy"), new LootFunctionType(new CopyEnergyFunction.Serializer()));
    }

}
