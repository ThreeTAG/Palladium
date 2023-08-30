package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class HealingAbility extends Ability {

    public static final PalladiumProperty<Integer> FREQUENCY = new IntegerProperty("frequency").configurable("Sets the frequency of healing (in ticks)");
    public static final PalladiumProperty<Float> AMOUNT = new FloatProperty("amount").configurable("Sets the amount of hearts for each healing");

    public HealingAbility() {
        this.withProperty(FREQUENCY, 20);
        this.withProperty(AMOUNT, 3F);
        this.withProperty(ICON, new ItemIcon(Items.APPLE));
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && !entity.level().isClientSide) {
            int frequency = entry.getProperty(FREQUENCY);
            if (frequency != 0 && entity.tickCount % frequency == 0) {
                entity.heal(entry.getProperty(AMOUNT));
            }
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Heals the entity.";
    }
}
