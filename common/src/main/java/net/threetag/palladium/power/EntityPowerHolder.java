package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;

public class EntityPowerHolder implements IPowerHolder {

    private Power power;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();

    @Override
    public Power getPower() {
        return this.power;
    }

    @Override
    public void setPower(Power power) {
        this.power = power;
        this.entryMap.clear();
        if (this.power != null) {
            for (AbilityConfiguration ability : this.power.getAbilities()) {
                this.entryMap.put(ability.getId(), new AbilityEntry(ability));
            }
        }
    }

    @Override
    public Map<String, AbilityEntry> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    @Override
    public void tick(LivingEntity entity) {
        this.entryMap.forEach((id, entry) -> entry.tick(entity, this.getPower(), this));
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();

        if (this.power != null) {
            nbt.putString("Power", this.power.getId().toString());

            if (!this.entryMap.isEmpty()) {
                CompoundTag abilitiesTag = new CompoundTag();
                this.entryMap.forEach((id, entry) -> abilitiesTag.put(id, entry.toNBT()));
                nbt.put("Abilities", abilitiesTag);
            }
        }

        return nbt;
    }

    @Override
    public void fromNBT(CompoundTag nbt) {
        this.setPower(PowerManager.getInstance().getPower(new ResourceLocation(nbt.getString("Power"))));

        CompoundTag abilitiesTag = nbt.getCompound("Abilities");
        this.entryMap.forEach((id, entry) -> entry.fromNBT(abilitiesTag.getCompound(id)));
    }
}
