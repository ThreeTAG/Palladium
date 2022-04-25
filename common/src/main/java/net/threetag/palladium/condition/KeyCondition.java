package net.threetag.palladium.condition;

public abstract class KeyCondition extends Condition {

    public final int cooldown;

    public KeyCondition(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }
}
