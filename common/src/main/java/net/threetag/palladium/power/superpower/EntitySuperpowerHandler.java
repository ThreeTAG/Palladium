package net.threetag.palladium.power.superpower;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class EntitySuperpowerHandler extends PalladiumEntityData<LivingEntity> {

    private Holder<Power> superpower;

    public EntitySuperpowerHandler(LivingEntity entity) {
        super(entity);
    }

    public void set(Holder<Power> power) {
        this.superpower = power;
    }

    public Holder<Power> getSuperpower() {
        return this.superpower;
    }

    @Override
    public void load(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        this.superpower = registryLookup.lookupOrThrow(PalladiumRegistryKeys.POWER).get(ResourceKey.create(PalladiumRegistryKeys.POWER, ResourceLocation.parse(nbt.getString("power")))).orElse(null);
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registryLookup) {
        var nbt = new CompoundTag();
        if (this.superpower != null) {
            nbt.putString("power", this.superpower.unwrapKey().orElseThrow().location().toString());
        }
        return nbt;
    }

    public static class Slot {

    }

}
