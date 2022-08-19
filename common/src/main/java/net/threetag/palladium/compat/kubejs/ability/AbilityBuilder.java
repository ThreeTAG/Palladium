package net.threetag.palladium.compat.kubejs.ability;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;

public class AbilityBuilder extends BuilderBase<Ability> {

    public transient IIcon icon;
    public transient TickFunction firstTick, tick, lastTick;

    public AbilityBuilder(ResourceLocation id) {
        super(id);
        this.icon = new ItemIcon(Items.BARRIER);
        this.firstTick = null;
        this.tick = null;
        this.lastTick = null;
    }

    @Override
    public RegistryObjectBuilderTypes<? super Ability> getRegistryType() {
        return PalladiumKubeJSPlugin.ABILITY;
    }

    @Override
    public Ability createObject() {
        return new ScriptableAbility(this);
    }

    public AbilityBuilder icon(IIcon icon) {
        this.icon = icon;
        return this;
    }

    public AbilityBuilder firstTick(TickFunction firstTick) {
        this.firstTick = firstTick;
        return this;
    }

    public AbilityBuilder tick(TickFunction tick) {
        this.tick = tick;
        return this;
    }

    public AbilityBuilder lastTick(TickFunction lastTick) {
        this.lastTick = lastTick;
        return this;
    }

    @FunctionalInterface
    public interface TickFunction {
        void tick(LivingEntityJS entity, AbilityEntry entry, IPowerHolder holder, boolean enabled);
    }
}
