package net.threetag.palladium.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

public class FlightSound extends AbstractTickableSoundInstance {

    public final LivingEntity entity;
    private int time;
    public boolean stop;

    public FlightSound(LivingEntity entity, SoundEvent soundEvent, SoundSource category) {
        super(soundEvent, category, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.looping = true;
        this.delay = 0;
        this.volume = 0F;
    }

    @Override
    public void tick() {
        if (this.stop) {
            this.stop();
            return;
        }
        ++this.time;
        if (this.entity.isAlive() && this.entity instanceof PalladiumPlayerExtension extension && extension.palladium_getFlightAnimation(1F) > 1F) {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
            float f = (float) this.entity.getDeltaMovement().lengthSqr() / 4F;
            if ((double) f >= 1.0E-7D) {
                this.volume = Mth.clamp(f / 4.0F, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }

            if (this.time < 20) {
                this.volume = 0.0F;
            } else if (this.time < 40) {
                this.volume = (float) ((double) this.volume * ((double) (this.time - 20) / 20.0D));
            }

            float f1 = 0.8F;
            if (this.volume > f1) {
                this.pitch = 1.0F + (this.volume - f1);
            } else {
                this.pitch = 1.0F;
            }

        } else {
            this.stop();
            FlightHandler.CACHED_SOUND = null;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
