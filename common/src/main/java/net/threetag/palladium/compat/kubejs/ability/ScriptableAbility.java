package net.threetag.palladium.compat.kubejs.ability;

import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.compat.kubejs.PalladiumJSEvents;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

import java.util.function.Consumer;

public class ScriptableAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> KEY = new ResourceLocationProperty("key").configurable("The unique key for your KubeJS code. You'll need to use this key when defining the handler code in the \"" + PalladiumJSEvents.SCRIPTABLE_ABILITIES + "\" event.");

    public Consumer<LivingEntityJS> firstTick, tick, lastTick;

    public ScriptableAbility() {
        this.withProperty(ICON, new ItemIcon(Items.COMMAND_BLOCK));
        this.withProperty(KEY, new ResourceLocation("example:key"));
    }

    @Override
    public void postParsing(AbilityConfiguration configuration) {
        new ScriptableAbilityEventJS(configuration.get(KEY), this).post(ScriptType.SERVER, PalladiumJSEvents.SCRIPTABLE_ABILITIES);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.firstTick != null && !entity.level.isClientSide) {
            this.firstTick.accept(UtilsJS.getLevel(entity.level).getLivingEntity(entity));
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.tick != null && enabled && !entity.level.isClientSide) {
            this.tick.accept(UtilsJS.getLevel(entity.level).getLivingEntity(entity));
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (this.lastTick != null && !entity.level.isClientSide) {
            this.lastTick.accept(UtilsJS.getLevel(entity.level).getLivingEntity(entity));
        }
    }
}
