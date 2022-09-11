package net.threetag.palladium.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.EnergyBlastAbility;

public class EnergyBlastSound extends AbstractTickableSoundInstance {

    private final LivingEntity entity;
    private final ResourceLocation powerId;
    private final String abilityId;

    public EnergyBlastSound(SoundEvent soundEvent, SoundSource soundSource, LivingEntity entity, ResourceLocation powerId, String abilityId) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.entity = entity;
        this.powerId = powerId;
        this.abilityId = abilityId;
    }

    @Override
    public void tick() {
        this.x = (float) this.entity.getX();
        this.y = (float) this.entity.getY();
        this.z = (float) this.entity.getZ();

        AbilityEntry entry = Ability.getEntry(this.entity, this.powerId, this.abilityId);

        if (entry == null) {
            this.stop();
            return;
        }

        this.volume = entry.getProperty(EnergyBlastAbility.ANIMATION_TIMER) / 5F;

        if (!this.entity.isAlive() || (this.volume <= 0F && !entry.isEnabled())) {
            this.stop();
        }
    }
}
