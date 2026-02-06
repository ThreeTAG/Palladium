package net.threetag.palladium.power.superpower;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.config.PalladiumServerConfig;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.power.Power;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class EntitySuperpowerHandler extends PalladiumEntityData<LivingEntity, EntitySuperpowerHandler> {

    // use CompoundTag codec as alternative, as MC opens non-existing tags as objects instead of arrays, causing the primary codec to scream
    public static final Codec<EntitySuperpowerHandler> CODEC = Codec.withAlternative(
            Power.HOLDER_CODEC.listOf().xmap(EntitySuperpowerHandler::new, s -> s.superpowers),
            CompoundTag.CODEC.xmap(compoundTag -> new EntitySuperpowerHandler(Collections.emptyList()), s -> new CompoundTag()));

    private final List<Holder<Power>> superpowers;

    public EntitySuperpowerHandler(List<Holder<Power>> superpowers) {
        this.superpowers = new ArrayList<>(superpowers);
    }

    @Override
    public Codec<EntitySuperpowerHandler> codec() {
        return CODEC;
    }

    @Override
    public void copyFrom(PalladiumEntityData<LivingEntity, EntitySuperpowerHandler> source) {
        if (source instanceof EntitySuperpowerHandler old) {
            this.superpowers.clear();
            this.superpowers.addAll(old.superpowers);
        }
    }

    public boolean canBeAdded(Holder<Power> powerHolder) {
        var power = powerHolder.value();

        if (!this.superpowers.contains(powerHolder)) {
            if (power.getParentId() == null) {
                return this.superpowers.stream().filter(h -> h.value().getParentId() == null).count() < PalladiumServerConfig.MAX_SUPERPOWER_SETS.get();
            } else {
                return this.superpowers.stream().anyMatch(h -> h.unwrapKey().orElseThrow().identifier().equals(power.getParentId()));
            }
        }

        return false;
    }

    public boolean add(Holder<Power> powerHolder) {
        if (this.canBeAdded(powerHolder)) {
            this.superpowers.add(powerHolder);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(Holder<Power> powerHolder) {
        return this.superpowers.remove(powerHolder);
    }

    public boolean remove(Predicate<Holder<Power>> predicate) {
        int i = 0;

        for (Holder<Power> holder : this.getSuperpowers()) {
            if (predicate.test(holder)) {
                this.superpowers.remove(holder);
                i++;
            }
        }

        return i > 0;
    }

    public void removeAll() {
        this.superpowers.clear();
    }

    public boolean has(Holder<Power> powerHolder) {
        return this.superpowers.contains(powerHolder);
    }

    public List<Holder<Power>> getSuperpowers() {
        return ImmutableList.copyOf(this.superpowers);
    }

}
