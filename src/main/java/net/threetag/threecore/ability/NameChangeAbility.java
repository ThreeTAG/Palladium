package net.threetag.threecore.ability;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.threetag.threecore.util.threedata.TextComponentThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class NameChangeAbility extends Ability {

    public static final ThreeData<ITextComponent> NAME = new TextComponentThreeData("name").enableSetting("The name which will render on the entity");

    public NameChangeAbility() {
        super(AbilityType.NAME_CHANGE);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(NAME, new StringTextComponent("???"));
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
