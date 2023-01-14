package net.threetag.palladium.compat.kubejs.ability;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyLookup;

public class ScriptableAbility extends Ability {

    public AbilityBuilder builder;

    public ScriptableAbility(AbilityBuilder builder) {
        this.withProperty(ICON, builder.icon);
        this.builder = builder;

		for (AbilityBuilder.DeserializePropertyInfo info : this.builder.extraProperties) {
			PalladiumProperty property = PalladiumPropertyLookup.get(info.type, info.key);

			if (info.configureDesc != null && !info.configureDesc.isEmpty())
				property.configurable(info.configureDesc);

			this.withProperty(property, PalladiumKubeJSPlugin.fixValues(property, info.defaultValue));
		}
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.firstTick != null && !entity.level.isClientSide) {
            this.builder.firstTick.tick(entity, entry, holder, enabled);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.tick != null && !entity.level.isClientSide) {
            this.builder.tick.tick(entity, entry, holder, enabled);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.lastTick != null && !entity.level.isClientSide) {
            this.builder.lastTick.tick(entity, entry, holder, enabled);
        }
    }
}
