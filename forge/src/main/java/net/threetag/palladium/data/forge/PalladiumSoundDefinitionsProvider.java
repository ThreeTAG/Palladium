package net.threetag.palladium.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public PalladiumSoundDefinitionsProvider(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, Palladium.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(PalladiumSoundEvents.HEAT_VISION, definition().with(sound(Palladium.id("energy_blast"))).subtitle(subtitle(PalladiumSoundEvents.HEAT_VISION)));
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }

    public static String subtitle(RegistrySupplier<SoundEvent> supplier) {
        return "subtitles." + Palladium.MOD_ID + "." + supplier.getId().getPath();
    }
}
