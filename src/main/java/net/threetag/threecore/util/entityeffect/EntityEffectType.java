package net.threetag.threecore.util.entityeffect;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.threetag.threecore.ThreeCore;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEffectType extends ForgeRegistryEntry<EntityEffectType> {

    public static IForgeRegistry<EntityEffectType> REGISTRY;

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<EntityEffectType>().setName(new ResourceLocation(ThreeCore.MODID, "entity_effect_types")).setType(EntityEffectType.class).setIDRange(0, 2048).create();
    }

    private Supplier<EntityEffect> supplier;

    public EntityEffectType(Supplier<EntityEffect> supplier) {
        this.supplier = supplier;
    }

    public EntityEffectType(Supplier<EntityEffect> supplier, String modid, String name) {
        this.supplier = supplier;
        this.setRegistryName(modid, name);
    }

    public EntityEffect create() {
        return this.supplier.get();
    }

    public EntityEffect create(CompoundNBT nbt) {
        EntityEffect entityEffect = this.create();
        entityEffect.deserializeNBT(nbt);
        return entityEffect;
    }

}
