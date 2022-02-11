package net.threetag.palladium.util.property;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityPropertyHandler {

    final Entity entity;
    final Map<PalladiumProperty<?>, Object> defaultProperties = new HashMap<>();
    final Map<PalladiumProperty<?>, Object> values = new HashMap<>();

    public EntityPropertyHandler(Entity entity) {
        this.entity = entity;
    }

    public <T> EntityPropertyHandler register(PalladiumProperty<T> property, T value) {
        this.defaultProperties.put(property, value);
        this.values.put(property, value);
        return this;
    }

    public <T> EntityPropertyHandler set(PalladiumProperty<T> property, T value) {
        this.values.put(property, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(PalladiumProperty<T> property) {
        return (T) this.values.get(property);
    }

    public void fromNBT(CompoundTag nbt) {

    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();

        return nbt;
    }

    @ExpectPlatform
    public static EntityPropertyHandler getHandler(Entity entity) {
        throw new AssertionError();
    }

}
