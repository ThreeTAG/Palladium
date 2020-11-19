package net.threetag.threecore.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;

import java.util.Collection;
import java.util.Map;

public interface IAccessoireHolder {

    void enable(AccessoireSlot slot, Accessoire accessoire, PlayerEntity player);

    void disable(AccessoireSlot slot, Accessoire accessoire, PlayerEntity player);

    void validate(PlayerEntity player);

    void clear(PlayerEntity player);

    Map<AccessoireSlot, Collection<Accessoire>> getSlots();

}
