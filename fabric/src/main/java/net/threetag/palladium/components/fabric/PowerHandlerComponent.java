package net.threetag.palladium.components.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerHandler;

public class PowerHandlerComponent extends PowerHandler implements ComponentV3 {

    public PowerHandlerComponent(LivingEntity entity) {
        super(entity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.fromNBT(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag nbt = this.toNBT();
        for (String key : nbt.getAllKeys()) {
            tag.put(key, nbt.get(key));
        }
    }

}
