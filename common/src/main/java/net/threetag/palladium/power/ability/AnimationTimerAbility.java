package net.threetag.palladium.power.ability;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;

public class AnimationTimerAbility extends Ability implements AnimationTimer {

    public static final PalladiumProperty<Integer> START_VALUE = new IntegerProperty("start_value").configurable("The value for the integer when the ability is disabled");
    public static final PalladiumProperty<Integer> MAX_VALUE = new IntegerProperty("max_value").configurable("The value for the integer when the ability is enabled");
    public static final PalladiumProperty<Integer> VALUE = new IntegerProperty("value").sync(SyncType.NONE).disablePersistence();
    public static final PalladiumProperty<Integer> PREV_VALUE = new IntegerProperty("prev_value").sync(SyncType.NONE).disablePersistence();

    public AnimationTimerAbility() {
        this.withProperty(START_VALUE, 0);
        this.withProperty(MAX_VALUE, 20);
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(VALUE, 0);
        manager.register(PREV_VALUE, 0);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            var start = entry.getProperty(START_VALUE);
            entry.setUniqueProperty(VALUE, start);
            entry.setUniqueProperty(PREV_VALUE, start);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        int value = entry.getProperty(VALUE);
        entry.setUniqueProperty(PREV_VALUE, value);

        if (entry.isEnabled() && value < entry.getProperty(MAX_VALUE)) {
            entry.setUniqueProperty(VALUE, value + 1);
        } else if (!entry.isEnabled() && value > entry.getProperty(START_VALUE)) {
            entry.setUniqueProperty(VALUE, value - 1);
        }
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    public String getDocumentationDescription() {
        return "This ability is used to create a timer that can be used for animations. It is not meant to be used directly. <a href=\"https://media.discordapp.net/attachments/954053032570683402/1120044643342635118/image.png\">Click here to see how this animation works exactly.</a>";
    }

    @Override
    public float getAnimationValue(AbilityEntry entry, float partialTick) {
        return Mth.lerp(partialTick, entry.getProperty(PREV_VALUE), entry.getProperty(VALUE)) / entry.getProperty(MAX_VALUE);
    }

    @Override
    public float getAnimationTimer(AbilityEntry entry, float partialTick, boolean maxedOut) {
        if (maxedOut) {
            return entry.getProperty(MAX_VALUE);
        }
        return Mth.lerp(partialTick, entry.getProperty(PREV_VALUE), entry.getProperty(VALUE));
    }

}
