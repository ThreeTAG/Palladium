package net.threetag.palladium.power.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SyncAbilityEntryPropertyMessage;
import net.threetag.palladium.network.SyncAbilityStateMessage;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;

public class AbilityEntry {

    private final AbilityConfiguration abilityConfiguration;
    private final IPowerHolder holder;
    private boolean unlocked = true;
    private boolean enabled = true;
    public boolean keyPressed = false;
    private int lifetime = 0;
    private int enabledTicks = 0;
    public String id;
    private final PropertyManager propertyManager = new PropertyManager().setListener(new PropertyManager.Listener() {
        @Override
        public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
            if (!holder.getEntity().level.isClientSide) {
                CompoundTag tag = new CompoundTag();
                tag.put(property.getKey(), property.toNBT(newValue));
                if (property.getSyncType() == SyncType.EVERYONE) {
                    new SyncAbilityEntryPropertyMessage(holder.getEntity().getId(), holder.getPower().getId(), abilityConfiguration.getId(), property.getKey(), tag).sendToLevel((ServerLevel) holder.getEntity().level);
                } else if (property.getSyncType() == SyncType.SELF && holder.getEntity() instanceof ServerPlayer serverPlayer) {
                    new SyncAbilityEntryPropertyMessage(holder.getEntity().getId(), holder.getPower().getId(), abilityConfiguration.getId(), property.getKey(), tag).sendTo(serverPlayer);
                }
            }
        }
    });

    public AbilityEntry(AbilityConfiguration abilityConfiguration, IPowerHolder holder) {
        this.abilityConfiguration = abilityConfiguration;
        this.holder = holder;
        this.abilityConfiguration.getAbility().registerUniqueProperties(this.propertyManager);
    }

    public AbilityConfiguration getConfiguration() {
        return abilityConfiguration;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
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

    public void setClientState(LivingEntity entity, IPowerHolder powerHolder, boolean unlocked, boolean enabled) {
        this.unlocked = unlocked;

        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (this.enabled) {
                this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
            } else {
                this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
            }
        }
    }

    public void tick(LivingEntity entity, Power power, IPowerHolder powerHolder) {
        if (!entity.level.isClientSide) {
            boolean unlocked = true;
            boolean sync = false;

            for (Condition unlockingCondition : this.abilityConfiguration.getUnlockingConditions()) {
                if (!unlockingCondition.active(entity, this, power, powerHolder)) {
                    unlocked = false;
                    break;
                }
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

            if (this.unlocked != unlocked) {
                this.unlocked = unlocked;
                sync = true;
            }

            if (this.enabled != enabled) {
                this.enabled = enabled;
                sync = true;

                if (this.enabled) {
                    this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
                } else {
                    this.keyPressed = false;
                    this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
                }
            }

            if (sync || lifetime == 0) {
                new SyncAbilityStateMessage(entity.getId(), this.holder.getPower().getId(), this.abilityConfiguration.getId(), this.unlocked, this.enabled).sendToLevel((ServerLevel) entity.getLevel());
            }
        }

        if (this.isEnabled()) {
            this.enabledTicks++;
        } else if (this.enabledTicks > 0) {
            this.enabledTicks--;
        }

        this.lifetime++;
        this.abilityConfiguration.getAbility().tick(entity, this, powerHolder, this.isEnabled());
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

    public <T> AbilityEntry setOwnProperty(PalladiumProperty<T> property, T value) {
        this.propertyManager.set(property, value);
        return this;
    }

}
