package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

public class SoundEventProperty extends RegistryObjectProperty<SoundEvent> {

    public SoundEventProperty(String key) {
        super(key, Registry.SOUND_EVENT);
    }
}
