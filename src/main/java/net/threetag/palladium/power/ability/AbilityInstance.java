package net.threetag.palladium.power.ability;

import net.minecraft.core.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.network.SyncAbilityComponentPacket;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class AbilityInstance<T extends Ability> implements DataComponentHolder {

    private final T ability;
    private final PowerInstance powerInstance;
    private int lifetime = 0;
    private int prevEnabledTicks = 0;
    private int enabledTicks = 0;
    private final PatchedDataComponentMap components;
    private final AbilityReference reference;
    private final AnimationTimer animationTimer;

    public AbilityInstance(T ability, PowerInstance powerInstance) {
        this.ability = ability;
        this.powerInstance = powerInstance;
        this.reference = new AbilityReference(powerInstance.getPowerId(), ability.getKey());
        this.animationTimer = ability.getProperties().getAnimationTimerSetting() != null ? ability.getProperties().getAnimationTimerSetting().create() : null;

        var componentsBuilder = DataComponentMap.builder().addAll(PalladiumDataComponents.Abilities.getCommonComponents());
        this.ability.registerDataComponents(componentsBuilder);
        this.components = new PatchedDataComponentMap(componentsBuilder.build());
    }

    public AbilityInstance(T ability, PowerInstance powerInstance, CompoundTag componentData) {
        this.ability = ability;
        this.powerInstance = powerInstance;
        this.reference = new AbilityReference(powerInstance.getPowerId(), ability.getKey());
        this.animationTimer = ability.getProperties().getAnimationTimerSetting() != null ? new AnimationTimer(ability.getProperties().getAnimationTimerSetting(), ability.getProperties().getAnimationTimerSetting().min()) : null;

        var componentsBuilder = DataComponentMap.builder().addAll(PalladiumDataComponents.Abilities.getCommonComponents());
        this.ability.registerDataComponents(componentsBuilder);
        this.components = PatchedDataComponentMap.fromPatch(componentsBuilder.build(), DataComponentPatch.CODEC.parse(this.powerInstance.entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), componentData).getOrThrow());
    }

    public T getAbility() {
        return this.ability;
    }

    public PowerInstance getPowerInstance() {
        return this.powerInstance;
    }

    public AbilityReference getReference() {
        return this.reference;
    }

    public boolean isUnlocked() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.UNLOCKED.get(), true);
    }

    public boolean isEnabled() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.ENABLED.get(), true);
    }

    public int getEnabledTicks() {
        return this.enabledTicks;
    }

    public int getPrevEnabledTicks() {
        return this.prevEnabledTicks;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void tick(LivingEntity entity, boolean dampened) {
        this.prevEnabledTicks = this.enabledTicks;
        boolean firstTicked = false;

        if (!entity.level().isClientSide()) {
            boolean unlocked = (!this.ability.getProperties().canBeDampened() || !dampened) && this.ability.getStateManager().getUnlockingHandler().check(entity, this) && !entity.isSpectator();

            if (unlocked != this.isUnlocked()) {
                this.set(PalladiumDataComponents.Abilities.UNLOCKED.get(), unlocked);

                if (!unlocked) {
                    this.ability.getStateManager().getEnablingHandler().onUnlocked(entity, this);
                }
            }

            boolean enabled = unlocked && this.ability.getStateManager().getEnablingHandler().check(entity, this);

            if (enabled != this.isEnabled()) {
                this.set(PalladiumDataComponents.Abilities.ENABLED.get(), enabled);

                if (enabled) {
                    this.ability.firstTick(entity, this);
                    firstTicked = true;
                } else if (this.lifetime > 0) {
                    this.ability.lastTick(entity, this);
                }
            }
        }

        if (this.isEnabled()) {
            if (!firstTicked && this.lifetime == 0) {
                this.ability.firstTick(entity, this);
            }

            this.enabledTicks++;
        } else if (this.enabledTicks > 0) {
            this.enabledTicks--;
        }

        this.lifetime++;
        boolean result = this.ability.tick(entity, this, this.isEnabled());
        this.ability.getStateManager().getEnablingHandler().tick(entity, this, this.isEnabled());

        if (result) {
            for (EnergyBarUsage usage : this.ability.getEnergyBarUsages()) {
                usage.consume(this.powerInstance);
            }
        }

        if (this.animationTimer != null) {
            this.ability.animationTimerTick(entity, this, this.isEnabled(), this.animationTimer);
        }
    }

    @Nullable
    public AnimationTimer getAnimationTimer() {
        return this.animationTimer;
    }

    public int getAnimationTimerValue() {
        return this.animationTimer != null ? this.animationTimer.value() : (this.isEnabled() ? 1 : 0);
    }

    public float getAnimationTimerProgress(float partialTick) {
        return this.animationTimer != null ? this.animationTimer.progress(partialTick) : (this.isEnabled() ? 1F : 0F);
    }

    public float getAnimationTimerProgressEased(float partialTick) {
        return this.animationTimer != null ? this.animationTimer.eased(partialTick) : (this.isEnabled() ? 1F : 0F);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return this.components;
    }

    @Nullable
    public <R> R set(DataComponentType<R> component, @Nullable R value) {
        var changed = this.components.set(component, value);
        if (!this.powerInstance.getEntity().level().isClientSide()) {
            var patch = DataComponentPatch.builder().set(Objects.requireNonNull(this.components.getTyped(component))).build();
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.powerInstance.getEntity(), new SyncAbilityComponentPacket(this.powerInstance.getEntity().getId(), this.reference, patch));
        }
        return changed;
    }

    @Nullable
    public <R> R setSilently(DataComponentType<R> component, @Nullable R value) {
        return this.components.set(component, value);
    }

    @Nullable
    public <R, U> R update(DataComponentType<R> component, R defaultValue, U updateValue, BiFunction<R, U, R> updater) {
        return this.set(component, updater.apply(this.getOrDefault(component, defaultValue), updateValue));
    }

    @Nullable
    public <R> R update(DataComponentType<R> component, R defaultValue, UnaryOperator<R> updater) {
        R object = this.getOrDefault(component, defaultValue);
        return this.set(component, updater.apply(object));
    }

    @Nullable
    public <R, U> R updateSilently(DataComponentType<R> component, R defaultValue, U updateValue, BiFunction<R, U, R> updater) {
        return this.setSilently(component, updater.apply(this.getOrDefault(component, defaultValue), updateValue));
    }

    @Nullable
    public <R> R remove(DataComponentType<? extends R> component) {
        return this.components.remove(component);
    }

    public void applyPatch(DataComponentPatch components) {
        this.components.applyPatch(components);
    }

    public void applyComponents(DataComponentMap components) {
        this.components.setAll(components);
    }

    public Tag save() {
        return DataComponentMap.CODEC.encodeStart(this.powerInstance.entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.components).getOrThrow();
    }

}
