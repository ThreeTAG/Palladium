package net.threetag.palladium.components.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.accessory.AccessoryPlayerData;

import java.util.Objects;

public class AccessoriesComponent extends AccessoryPlayerData implements ComponentV3 {

    public AccessoriesComponent(Entity entity) {

    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.fromNBT(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag nbt = this.toNBT();
        for (String key : nbt.getAllKeys()) {
            tag.put(key, Objects.requireNonNull(nbt.get(key)));
        }
    }
}
