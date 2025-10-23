package net.threetag.palladium.power.ability;

import net.minecraft.util.Mth;

public class AnimationTimer {

    private final AnimationTimerSetting setting;
    private int value, prev;

    public AnimationTimer(AnimationTimerSetting setting, int value) {
        this.setting = setting;
        this.value = value;
        this.prev = value;
    }

    public AnimationTimer tick() {
        this.prev = this.value;
        return this;
    }

    public AnimationTimer tickAndUpdate(boolean increase) {
        this.tick();

        if (increase && this.value < this.setting.max()) {
            return this.incr();
        } else if (!increase && this.value > this.setting.min()) {
            return this.decr();
        }

        return this;
    }

    public AnimationTimer set(int value) {
        this.value = value;
        return this;
    }

    public AnimationTimer incr(int amount) {
        return this.set(this.value + amount);
    }

    public AnimationTimer incr() {
        return this.incr(1);
    }

    public AnimationTimer decr(int amount) {
        return this.set(this.value - amount);
    }

    public AnimationTimer decr() {
        return this.decr(1);
    }

    public int value() {
        return this.value;
    }

    public int prev() {
        return this.prev;
    }

    public int min() {
        return this.setting.min();
    }

    public int max() {
        return this.setting.max();
    }

    public float interpolated(float partialTick) {
        return Mth.lerp(partialTick, this.prev, this.value);
    }

    public float progress(float partialTick) {
        return (this.interpolated(partialTick) - this.setting.min()) / (this.setting.max() - this.setting.min());
    }

    public float eased(float partialTick) {
        return this.setting.easing().apply(this.progress(partialTick));
    }
}
