package net.threetag.threecore.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.threetag.threecore.accessoires.Accessoire;

import java.util.Collection;

public interface IAccessoireHolder {

    void enable(Accessoire accessoire, PlayerEntity player);

    void disable(Accessoire accessoire, PlayerEntity player);

    void validate(PlayerEntity player);

    Collection<Accessoire> getActiveAccessoires();

}
