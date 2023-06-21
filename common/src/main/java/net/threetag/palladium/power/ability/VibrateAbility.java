package net.threetag.palladium.power.ability;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;

public class VibrateAbility extends Ability implements AnimationTimer {

    public static final PalladiumProperty<Integer> VALUE = new IntegerProperty("value").sync(SyncType.NONE).disablePersistence();
    public static final PalladiumProperty<Integer> PREV_VALUE = new IntegerProperty("prev_value").sync(SyncType.NONE).disablePersistence();

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(VALUE, 0);
        manager.register(PREV_VALUE, 0);
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        int value = entry.getProperty(VALUE);
        entry.setUniqueProperty(PREV_VALUE, value);

        if (enabled && value < 10) {
            entry.setUniqueProperty(VALUE, value + 1);
        } else if (!enabled && value > 0) {
            entry.setUniqueProperty(VALUE, value - 1);
        }
    }

    @Override
    public float getAnimationValue(AbilityEntry entry, float partialTick) {
        return Mth.lerp(partialTick, entry.getProperty(PREV_VALUE), entry.getProperty(VALUE)) / 10;
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
