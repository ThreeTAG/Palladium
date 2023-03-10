package net.threetag.palladium.power.ability;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.*;

public class AimAbility extends Ability {

    public static final PalladiumProperty<Integer> TIME = new IntegerProperty("time").configurable("Determines how many ticks it takes until the arm is fully aimed");
    public static final PalladiumProperty<ArmTypeProperty.ArmType> ARM = new ArmTypeProperty("arm").configurable("Determines which arm(s) should point");
    public static final PalladiumProperty<Integer> TIMER = new IntegerProperty("timer").sync(SyncType.NONE);
    public static final PalladiumProperty<Integer> PREV_TIMER = new IntegerProperty("prev_timer").sync(SyncType.NONE);

    public AimAbility() {
        this.withProperty(TIME, 10);
        this.withProperty(ARM, ArmTypeProperty.ArmType.MAIN_ARM);
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(TIMER, 0);
        manager.register(PREV_TIMER, 0);
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (entity.level.isClientSide) {
            int timer = entry.getProperty(TIMER);
            entry.setUniqueProperty(PREV_TIMER, timer);
            if (enabled && timer < entry.getProperty(TIME)) {
                entry.setUniqueProperty(TIMER, timer + 1);
            } else if (!enabled && timer > 0) {
                entry.setUniqueProperty(TIMER, timer - 1);
            }
        }
    }

    public static float getTimer(LivingEntity entity, float partialTicks, boolean right) {
        float f = 0;

        for (AbilityEntry entry : AbilityUtil.getEntries(entity, Abilities.AIM.get())) {
            var armType = entry.getProperty(ARM);

            if(!armType.isNone()) {
                if(armType.isRight(entity) && right) {
                    f = Math.max(f, Mth.lerp(partialTicks, entry.getProperty(PREV_TIMER), entry.getProperty(TIMER)) / entry.getProperty(TIME));
                } else if(armType.isLeft(entity) && !right) {
                    f = Math.max(f, Mth.lerp(partialTicks, entry.getProperty(PREV_TIMER), entry.getProperty(TIMER)) / entry.getProperty(TIME));
                }
            }
        }

        return f;
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows the player to aim their arms.";
    }
}
