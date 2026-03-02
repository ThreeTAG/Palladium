package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.ability.enabling.ConditionalEnablingHandler;
import net.threetag.palladium.power.ability.enabling.EnablingHandler;
import net.threetag.palladium.power.ability.unlocking.ConditionalUnlockingHandler;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandler;

import java.util.ArrayList;
import java.util.List;

public final class AbilityStateManager {

    public static final AbilityStateManager EMPTY = new AbilityStateManager(ConditionalUnlockingHandler.EMPTY, ConditionalEnablingHandler.EMPTY);

    public static final Codec<AbilityStateManager> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UnlockingHandler.CODEC.optionalFieldOf("unlocking", ConditionalUnlockingHandler.EMPTY).forGetter(AbilityStateManager::getUnlockingHandler),
                    EnablingHandler.CODEC.optionalFieldOf("enabling", ConditionalEnablingHandler.EMPTY).forGetter(AbilityStateManager::getEnablingHandler)
            ).apply(instance, AbilityStateManager::new));

    private final UnlockingHandler unlocking;
    private final EnablingHandler enabling;
    public final List<AbilityReference> dependencies;

    public AbilityStateManager(UnlockingHandler unlocking, EnablingHandler activation) {
        this.unlocking = unlocking;
        this.enabling = activation;
        this.dependencies = new ArrayList<>();
    }

    public UnlockingHandler getUnlockingHandler() {
        return this.unlocking;
    }

    public EnablingHandler getEnablingHandler() {
        return this.enabling;
    }

    public boolean isKeyBound() {
        return this.enabling.isKeyBound();
    }

}
