package net.threetag.palladium.components.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;

public class EntityPropertyHandlerComponent extends EntityPropertyHandler implements ComponentV3 {

    public EntityPropertyHandlerComponent(Entity entity) {
        super(entity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.fromNBT(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag nbt = this.toNBT(true);
        for (String key : nbt.getAllKeys()) {
            tag.put(key, nbt.get(key));
        }
    }
}
