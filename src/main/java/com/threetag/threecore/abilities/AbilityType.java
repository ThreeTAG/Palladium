package com.threetag.threecore.abilities;

import com.threetag.threecore.ThreeCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AbilityType extends ForgeRegistryEntry<AbilityType> {

    public static IForgeRegistry<AbilityType> REGISTRY;

    public static final AbilityType HEALING = new AbilityType(AbilityHealing::new, ThreeCore.MODID, "healing");

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<AbilityType>().setName(new ResourceLocation(ThreeCore.MODID, "ability_types")).setType(AbilityType.class).setIDRange(0, 2048).create();
    }

    @SubscribeEvent
    public static void onRegisterAbilityTypes(RegistryEvent.Register<AbilityType> e) {
        e.getRegistry().register(HEALING);
    }

    // ----------------------------------------------------------------------------------------------------------------

    private Supplier<Ability> supplier;

    public AbilityType(Supplier<Ability> supplier) {
        this.supplier = supplier;
    }

    public AbilityType(Supplier<Ability> supplier, String modid, String name) {
        this.supplier = supplier;
        this.setRegistryName(modid, name);
    }

    public Ability create() {
        return this.supplier.get();
    }

}
