package net.threetag.palladium.power.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.CooldownType;
import net.threetag.palladium.network.SyncAbilityEntryPropertyMessage;
import net.threetag.palladium.network.SyncAbilityStateMessage;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;
import org.jetbrains.annotations.Nullable;

public class AbilityEntry {

    private final AbilityConfiguration abilityConfiguration;
    private final IPowerHolder holder;
    private boolean unlocked = false;
    private boolean enabled = false;
    public boolean keyPressed = false;
    public int maxCooldown = 0, cooldown = 0;
    public int maxActivationTimer = 0, activationTimer = 0;
    private int lifetime = 0;
    private int enabledTicks = 0;
    public String id;
    private final PropertyManager propertyManager = new PropertyManager().setListener(new PropertyManager.Listener() {
        @Override
        public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
            syncProperty(property, holder.getEntity(), null);
        }
    });

    public AbilityEntry(AbilityConfiguration abilityConfiguration, IPowerHolder holder) {
        this.abilityConfiguration = abilityConfiguration;
        this.holder = holder;
        this.abilityConfiguration.getAbility().registerUniqueProperties(this.propertyManager);
        this.abilityConfiguration.getUnlockingConditions().forEach(condition -> condition.registerAbilityProperties(this, this.propertyManager));
        this.abilityConfiguration.getEnablingConditions().forEach(condition -> condition.registerAbilityProperties(this, this.propertyManager));
    }

    public AbilityConfiguration getConfiguration() {
        return abilityConfiguration;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public IPowerHolder getHolder() {
        return holder;
    }

    public AbilityReference getReference() {
        return new AbilityReference(this.holder.getPower().getId(), this.id);
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
    public void syncProperty(PalladiumProperty<?> property, LivingEntity entity, @Nullable SyncType syncType) {
        if (!entity.level.isClientSide && this.propertyManager.isRegistered(property)) {
            if (syncType == null) {
                syncType = property.getSyncType();
            }

            CompoundTag tag = new CompoundTag();
            PalladiumProperty property1 = property;
            tag.put(property.getKey(), property1.toNBT(this.propertyManager.get(property)));
            if (syncType == SyncType.EVERYONE) {
                new SyncAbilityEntryPropertyMessage(entity.getId(), new AbilityReference(holder.getPower().getId(), abilityConfiguration.getId()), property.getKey(), tag).sendToDimension(entity.level);
            } else if (syncType == SyncType.SELF && entity instanceof ServerPlayer serverPlayer) {
                new SyncAbilityEntryPropertyMessage(entity.getId(), new AbilityReference(holder.getPower().getId(), abilityConfiguration.getId()), property.getKey(), tag).send(serverPlayer);
            }
        }
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getEnabledTicks() {
        return enabledTicks;
    }

    public void setClientState(LivingEntity entity, IPowerHolder powerHolder, boolean unlocked, boolean enabled, int maxCooldown, int cooldown, int maxActivationTimer, int activationTimer) {
        this.unlocked = unlocked;
        this.maxCooldown = maxCooldown;
        this.cooldown = cooldown;
        this.maxActivationTimer = maxActivationTimer;
        this.activationTimer = activationTimer;

        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (this.enabled) {
                this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
            } else {
                this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, !this.isEnabled());
            }
        }
    }

    public void tick(LivingEntity entity, Power power, IPowerHolder powerHolder) {
        if (!entity.level.isClientSide) {
            if (this.lifetime == 0) {
                this.abilityConfiguration.getUnlockingConditions().forEach(condition -> condition.init(entity, this, this.propertyManager));
                this.abilityConfiguration.getEnablingConditions().forEach(condition -> condition.init(entity, this, this.propertyManager));
            }

            boolean unlocked = true;
            boolean sync = false;

            for (Condition unlockingCondition : this.abilityConfiguration.getUnlockingConditions()) {
                if (!unlockingCondition.active(entity, this, power, powerHolder)) {
                    unlocked = false;
                    break;
                }
            }

            if (entity.isSpectator()) {
                unlocked = false;
            }

            if (this.unlocked != unlocked) {
                this.unlocked = unlocked;
                sync = true;
            }

            boolean enabled = this.unlocked;

            if (this.unlocked) {
                for (Condition enablingCondition : this.abilityConfiguration.getEnablingConditions()) {
                    if (!enablingCondition.active(entity, this, power, powerHolder)) {
                        enabled = false;
                        break;
                    }
                }
            }

            if (entity.isSpectator()) {
                enabled = false;
            }

            if (this.enabled != enabled) {
                if (!this.enabled) {
                    this.enabled = true;
                    sync = true;
                    this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
                } else {
                    this.keyPressed = false;
                    this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
                    this.enabled = false;
                    sync = true;

                }
            }

            if (sync || lifetime == 0) {
                this.syncState(entity);
            }
        }

        if (this.isEnabled()) {
            this.enabledTicks++;
        } else if (this.enabledTicks > 0) {
            this.enabledTicks--;
        }

        if (this.abilityConfiguration.getCooldownType() == CooldownType.STATIC) {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
        } else if (this.abilityConfiguration.getCooldownType() == CooldownType.DYNAMIC) {
            if (this.isEnabled() && this.cooldown > 0) {
                this.cooldown--;
            } else if (!this.isEnabled() && this.cooldown < this.maxCooldown) {
                this.cooldown++;
            }
        }

        if (this.activationTimer > 0) {
            this.activationTimer--;
        }

        this.lifetime++;
        this.abilityConfiguration.getAbility().tick(entity, this, powerHolder, this.isEnabled());
    }

    public void syncState(LivingEntity entity) {
        getSyncStateMessage(entity).sendToDimension(entity.getLevel());
    }

    public SyncAbilityStateMessage getSyncStateMessage(LivingEntity entity) {
        return new SyncAbilityStateMessage(entity.getId(), this.getReference(), this.unlocked, this.enabled, this.maxCooldown, this.cooldown, this.maxActivationTimer, this.activationTimer);
    }

    public void startCooldown(LivingEntity entity, int cooldown) {
        this.maxCooldown = this.cooldown = cooldown;
        this.syncState(entity);
    }

    public void startActivationTimer(LivingEntity entity, int activationTimer) {
        this.maxActivationTimer = this.activationTimer = activationTimer;
        this.syncState(entity);
    }

    public void keyPressed(LivingEntity entity, boolean pressed) {
        for (Condition condition : this.getConfiguration().getUnlockingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder.getPower(), holder);
                } else {
                    condition.onKeyReleased(entity, this, holder.getPower(), holder);
                }
                return;
            }
        }

        for (Condition condition : this.getConfiguration().getEnablingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder.getPower(), holder);
                } else {
                    condition.onKeyReleased(entity, this, holder.getPower(), holder);
                }
                return;
            }
        }
    }

    public PalladiumProperty<?> getEitherPropertyByKey(String key) {
        PalladiumProperty<?> property = this.propertyManager.getPropertyByName(key);
        return property != null ? property : this.abilityConfiguration.getPropertyManager().getPropertyByName(key);
    }

    public <T> T getProperty(PalladiumProperty<T> property) {
        return this.propertyManager.isRegistered(property) ? this.propertyManager.get(property) : this.abilityConfiguration.get(property);
    }

    public Object getPropertyByName(String key) {
        var property = getEitherPropertyByKey(key);
        return property != null ? this.getProperty(property) : null;
    }

    public <T> AbilityEntry setUniqueProperty(PalladiumProperty<T> property, T value) {
        this.propertyManager.set(property, value);
        return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean setUniquePropertyByName(String key, Object value) {
        PalladiumProperty property = this.getPropertyManager().getPropertyByName(key);

        if (property != null) {
            this.setUniqueProperty(property, PalladiumProperty.fixValues(property, value));
            return true;
        }

        return false;
    }

    public void fromNBT(CompoundTag tag) {
        this.propertyManager.fromNBT(tag);
    }

    public CompoundTag toNBT() {
        return this.propertyManager.toNBT(false);
    }

}
