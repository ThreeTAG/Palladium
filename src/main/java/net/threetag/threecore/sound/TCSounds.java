package net.threetag.threecore.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;

public class TCSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, ThreeCore.MODID);

    public static final RegistryObject<SoundEvent> GRINDER = register("grinder");
    public static final RegistryObject<SoundEvent> MULTIVERSE_SEARCH = register("multiverse_search");

    public static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(ThreeCore.MODID, name)));
    }

}
