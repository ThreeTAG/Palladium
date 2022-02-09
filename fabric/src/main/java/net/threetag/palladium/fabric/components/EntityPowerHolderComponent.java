package net.threetag.palladium.fabric.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHolder;

public class EntityPowerHolderComponent extends EntityPowerHolder implements ComponentV3 {

    public EntityPowerHolderComponent(LivingEntity entity) {
        super(entity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.fromNBT(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag nbt = this.toNBT();
        nbt.getAllKeys().forEach(key -> tag.put(key, nbt.get(key)));
    }

}
