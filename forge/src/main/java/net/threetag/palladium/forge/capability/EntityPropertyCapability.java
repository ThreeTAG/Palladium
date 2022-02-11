package net.threetag.palladium.forge.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;
import net.threetag.palladium.util.property.EntityPropertyHandler;

public class EntityPropertyCapability extends EntityPropertyHandler implements INBTSerializable<CompoundTag> {

    public EntityPropertyCapability(Entity entity) {
        super(entity);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.toNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.fromNBT(nbt);
    }
}
