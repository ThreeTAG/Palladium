package net.threetag.palladium.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityReference;

public class AbilitySound extends AbstractTickableSoundLocationInstance {

    private final AbilityReference abilityReference;
    private final LivingEntity entity;
    private final float maxVolume;
    private boolean wasStopped = false;

    public AbilitySound(AbilityReference abilityReference, LivingEntity entity, ResourceLocation soundEvent, SoundSource soundSource, float volume, float pitch) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.abilityReference = abilityReference;
        this.entity = entity;
        this.volume = 0F;
        this.maxVolume = volume;
        this.pitch = pitch;
        this.looping = true;
    }

    @Override
    public void tick() {
        if (this.entity.isDeadOrDying()) {
            this.stop();
            return;
        }

        this.x = this.entity.getX();
        this.y = this.entity.getEyeY();
        this.z = this.entity.getZ();

        if (this.wasStopped) {
            if (this.volume > 0F) {
                this.volume = Mth.clamp(this.volume - 0.05F, 0F, this.maxVolume);
            }

            if (this.volume <= 0F) {
                this.stop();
            }
            return;
        }

        var ability = this.abilityReference.getInstance(this.entity);

        if (ability == null || !ability.isEnabled()) {
            this.wasStopped = true;
        } else if (!this.wasStopped) {
            if (this.volume < this.maxVolume) {
                this.volume = Mth.clamp(this.volume + 0.05F, 0F, this.maxVolume);
            }
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
