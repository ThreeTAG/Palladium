package net.threetag.palladium.sound;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public abstract class AbstractTickableSoundLocationInstance extends AbstractSoundInstance implements TickableSoundInstance {

    private boolean stopped;

    protected AbstractTickableSoundLocationInstance(ResourceLocation soundEvent, SoundSource soundSource, RandomSource randomSource) {
        super(soundEvent, soundSource, randomSource);
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    protected final void stop() {
        this.stopped = true;
        this.looping = false;
    }
}
