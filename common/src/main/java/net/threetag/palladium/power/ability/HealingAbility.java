package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.util.threedata.FloatThreeData;
import net.threetag.palladium.util.threedata.IntegerThreeData;
import net.threetag.palladium.util.threedata.ThreeData;

public class HealingAbility extends Ability {

    public static final ThreeData<Integer> FREQUENCY = new IntegerThreeData("frequency").configurable("Sets the frequency of healing (in ticks)");
    public static final ThreeData<Float> AMOUNT = new FloatThreeData("frequency").configurable("Sets the amount of hearts for each healing");

    public HealingAbility() {
        this.registerData(FREQUENCY, 20);
        this.registerData(AMOUNT, 3F);
    }

    @Override
    public void tick(LivingEntity entity, AbilityConfiguration entry, Power power, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            int frequency = entry.get(FREQUENCY);
            if (frequency != 0 && entity.tickCount % frequency == 0) {
                entity.heal(entry.get(AMOUNT));
            }
        }
    }
}
