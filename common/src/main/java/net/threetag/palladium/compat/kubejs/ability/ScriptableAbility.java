package net.threetag.palladium.compat.kubejs.ability;

import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;

public class ScriptableAbility extends Ability {

    public AbilityBuilder builder;

    public ScriptableAbility(AbilityBuilder builder) {
        this.withProperty(ICON, builder.icon);
        this.builder = builder;
    }

    public ScriptableAbility(ResourceLocation resourceLocation) {

    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.firstTick != null && !entity.level.isClientSide) {
            this.builder.firstTick.tick(UtilsJS.getLevel(entity.level).getLivingEntity(entity), entry, holder, enabled);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.tick != null && !entity.level.isClientSide) {
            this.builder.tick.tick(UtilsJS.getLevel(entity.level).getLivingEntity(entity), entry, holder, enabled);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.builder.lastTick != null && !entity.level.isClientSide) {
            this.builder.lastTick.tick(UtilsJS.getLevel(entity.level).getLivingEntity(entity), entry, holder, enabled);
        }
    }
}
