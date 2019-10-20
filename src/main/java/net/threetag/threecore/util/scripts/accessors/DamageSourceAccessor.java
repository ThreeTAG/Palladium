package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.util.DamageSource;

public class DamageSourceAccessor extends ScriptAccessor<DamageSource> {

    public DamageSourceAccessor(DamageSource value) {
        super(value);
    }

    public String getType() {
        return this.value.getDamageType();
    }

    public EntityAccessor getImmediateSource() {
        return this.value.getImmediateSource() != null ? (EntityAccessor) makeAccessor(this.value.getImmediateSource()) : null;
    }

    public EntityAccessor getTrueSource() {
        return this.value.getTrueSource() != null ? (EntityAccessor) makeAccessor(this.value.getTrueSource()) : null;
    }

}
