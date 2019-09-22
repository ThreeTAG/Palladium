package net.threetag.threecore.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.util.icon.ItemIcon;

public class DummyAbility extends Ability {

    public DummyAbility() {
        super(AbilityType.DUMMY);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new ItemIcon(new ItemStack(Blocks.BARRIER)));
    }
}
