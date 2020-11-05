package net.threetag.threecore.ability.container;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.ability.superpower.Superpower;

public class SuperpowerAbilityContainer extends DefaultAbilityContainer {

    public SuperpowerAbilityContainer(Superpower superpower, int lifetime) {
        super(superpower.getId(), superpower.getName(), superpower.getIcon(), lifetime);
        this.addAbilities(null, superpower);
    }

    public SuperpowerAbilityContainer(CompoundNBT nbt, boolean network) {
        super(nbt, network);
    }
}
