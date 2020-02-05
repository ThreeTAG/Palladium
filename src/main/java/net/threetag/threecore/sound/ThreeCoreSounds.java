package net.threetag.threecore.sound;

import net.threetag.threecore.ThreeCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ThreeCoreSounds {

    public static SoundEvent GRINDER;

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(GRINDER = makeSound(ThreeCore.MODID, "grinder"));
    }

    public static SoundEvent makeSound(String modid, String name) {
        return new SoundEvent(new ResourceLocation(modid, name)).setRegistryName(modid, name);
    }

}
