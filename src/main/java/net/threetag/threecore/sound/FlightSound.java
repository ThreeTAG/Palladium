package net.threetag.threecore.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.ability.Ability;

public class FlightSound extends TickableSound {

    public final Ability ability;
    public final LivingEntity entity;
    private int time;

    public FlightSound(Ability ability, LivingEntity entity, SoundEvent soundEvent, SoundCategory category) {
        super(soundEvent, category);
        this.ability = ability;
        this.entity = entity;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1F;
    }

    @Override
    public void tick() {
        ++this.time;
        if (this.entity.isAlive() && (this.time <= 20 || this.ability.getConditionManager().isEnabled())) {
            this.x = (float) this.entity.posX;
            this.y = (float) this.entity.posY;
            this.z = (float) this.entity.posZ;
            float f = (float) this.entity.getMotion().lengthSquared() / 4F;
            if ((double) f >= 1.0E-7D) {
                this.volume = MathHelper.clamp(f / 4.0F, 0.0F, 1.0F);
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
            this.donePlaying = true;
            if (this.ability instanceof IFlyingAbility) {
                ((IFlyingAbility) this.ability).onFinishedPlayingSound();
            }
        }
    }

    public interface IFlyingAbility {

        void onFinishedPlayingSound();

    }

}
