package net.threetag.palladium.sound;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;

public class PalladiumSoundEvents {

    public static final DeferredRegistry<SoundEvent> SOUNDS = DeferredRegistry.create(Palladium.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> HEAT_VISION = make("entity.ability.heat_vision");

    public static RegistrySupplier<SoundEvent> make(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Palladium.MOD_ID, name)));
    }

}
