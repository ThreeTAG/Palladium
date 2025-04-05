package net.threetag.palladium.power.superpower;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntitySuperpowerHandler extends PalladiumEntityData<LivingEntity> {

    private final List<Holder<Power>> superpowers = new ArrayList<>();

    public EntitySuperpowerHandler(LivingEntity entity) {
        super(entity);
    }

    public boolean canBeAdded(Holder<Power> powerHolder) {
        var power = powerHolder.value();

        if (!this.superpowers.contains(powerHolder)) {
            if (power.getParentId() == null) {
                return this.superpowers.stream().filter(h -> h.value().getParentId() == null).count() < PalladiumConfig.MAX_SUPERPOWER_SETS;
            } else {
                return this.superpowers.stream().anyMatch(h -> h.unwrapKey().orElseThrow().location().equals(power.getParentId()));
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

    @Override
    public void load(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        this.superpowers.clear();
        if (nbt.contains("powers")) {
            for (Tag tag : nbt.getList("powers", Tag.TAG_STRING)) {
                this.superpowers.add(registryLookup.lookupOrThrow(PalladiumRegistryKeys.POWER).get(ResourceKey.create(PalladiumRegistryKeys.POWER, ResourceLocation.parse(tag.getAsString()))).orElse(null));
            }
        }
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registryLookup) {
        var nbt = new CompoundTag();
        var list = new ListTag();

        for (Holder<Power> superpower : this.superpowers) {
            list.add(StringTag.valueOf(superpower.unwrapKey().orElseThrow().location().toString()));
        }

        nbt.put("powers", list);
        return nbt;
    }

    public static class Slot {

    }

}
