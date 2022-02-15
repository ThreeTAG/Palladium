package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;

public class MobEffectProperty extends RegistryObjectProperty<MobEffect> {

    public MobEffectProperty(String key) {
        super(key, Registry.MOB_EFFECT);
    }
}
