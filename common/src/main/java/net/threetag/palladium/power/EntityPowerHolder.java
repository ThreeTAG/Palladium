package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.provider.SuperpowerPowerProvider;

import java.util.HashMap;
import java.util.Map;

public class EntityPowerHolder implements IPowerHolder {

    private final LivingEntity entity;
    private final Power power;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();

    public EntityPowerHolder(LivingEntity entity, Power power) {
        this.entity = entity;
        this.power = power;
        for (AbilityConfiguration ability : this.getPower().getAbilities()) {
            this.entryMap.put(ability.getId(), new AbilityEntry(ability));
        }
    }

    @Override
    public Power getPower() {
        return this.power;
    }

    @Override
    public Map<String, AbilityEntry> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    @Override
    public void tick() {
        this.entryMap.forEach((id, entry) -> entry.tick(entity, this.getPower(), this));
    }

    @Override
    public void firstTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().firstTick(entity, entry, this, entry.isEnabled()));
    }

    @Override
    public void lastTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().lastTick(entity, entry, this, entry.isEnabled()));
    }

    @Override
    public boolean isInvalid() {
        ResourceLocation powerId = SuperpowerPowerProvider.SUPERPOWER_ID.get(this.entity);
        return this.power.isInvalid() || powerId == null || !powerId.equals(this.power.getId());
    }

    //    @Override
//    public CompoundTag toNBT() {
//        CompoundTag nbt = new CompoundTag();
//
//        if (this.getPower() != null) {
//            nbt.putString("Power", this.powerId.toString());
//
//            if (!this.entryMap.isEmpty()) {
//                CompoundTag abilitiesTag = new CompoundTag();
//                this.entryMap.forEach((id, entry) -> abilitiesTag.put(id, entry.toNBT()));
//                nbt.put("Abilities", abilitiesTag);
//            }
//        }
//
//        return nbt;
//    }
//
//    @Override
//    public void fromNBT(CompoundTag nbt) {
//        this.setPower(PowerManager.getInstance(this.entity.getLevel()).getPower(new ResourceLocation(nbt.getString("Power"))));
//    }
}
