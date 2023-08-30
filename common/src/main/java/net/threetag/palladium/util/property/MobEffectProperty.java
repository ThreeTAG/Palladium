package net.threetag.palladium.util.property;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class MobEffectProperty extends RegistryObjectProperty<MobEffect> {

    public MobEffectProperty(String key) {
        super(key, BuiltInRegistries.MOB_EFFECT);
    }

    @Override
    public String getPropertyType() {
        return "mob_effect";
    }
}
