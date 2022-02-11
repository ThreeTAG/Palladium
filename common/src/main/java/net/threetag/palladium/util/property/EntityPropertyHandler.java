package net.threetag.palladium.util.property;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyMessage;

import java.util.HashMap;
import java.util.Map;

public class EntityPropertyHandler {

    final Entity entity;
    final Map<PalladiumProperty<?>, Object> defaultProperties = new HashMap<>();
    final Map<PalladiumProperty<?>, Object> values = new HashMap<>();

    public EntityPropertyHandler(Entity entity) {
        this.entity = entity;
        PalladiumEvents.REGISTER_PROPERTY.invoker().register(this);
    }

    public <T> EntityPropertyHandler register(PalladiumProperty<T> property, T value) {
        this.defaultProperties.put(property, value);
        this.values.put(property, value);
        return this;
    }

    public <T> EntityPropertyHandler set(PalladiumProperty<T> property, T value) {
        this.values.put(property, value);

        if (!this.entity.level.isClientSide) {
            new SyncPropertyMessage(this.entity.getId(), new PalladiumPropertyValue<>(property, value)).sendToLevel((ServerLevel) this.entity.level);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(PalladiumProperty<T> property) {
        return (T) this.values.get(property);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void fromNBT(CompoundTag nbt) {
        this.values.clear();
        for (PalladiumProperty property : this.defaultProperties.keySet()) {
            if (nbt.contains(property.getKey())) {
                Tag tag = nbt.get(property.getKey());

                if (tag instanceof StringTag stringTag && stringTag.getAsString().equals("null")) {
                    this.values.put(property, null);
                } else {
                    this.values.put(property, property.fromNBT(nbt.get(property.getKey()), this.defaultProperties.get(property)));
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        for (PalladiumProperty property : this.values.keySet()) {
            if (this.values.get(property) == null) {
                nbt.put(property.getKey(), StringTag.valueOf("null"));
            } else {
                nbt.put(property.getKey(), property.toNBT(this.values.get(property)));
            }
        }
        return nbt;
    }

    @ExpectPlatform
    public static EntityPropertyHandler getHandler(Entity entity) {
        throw new AssertionError();
    }

}
