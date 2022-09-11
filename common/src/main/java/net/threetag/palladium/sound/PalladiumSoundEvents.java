package net.threetag.palladium.sound;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Palladium.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> HEAT_VISION = make("entity.ability.heat_vision");

    public static RegistrySupplier<SoundEvent> make(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Palladium.MOD_ID, name)));
    }

}
