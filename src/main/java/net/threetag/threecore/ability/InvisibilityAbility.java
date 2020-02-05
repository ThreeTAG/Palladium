package net.threetag.threecore.ability;

import net.threetag.threecore.util.icon.TexturedIcon;

public class InvisibilityAbility extends Ability {

    public InvisibilityAbility() {
        super(AbilityType.INVISIBILITY);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 96, 16, 16, 16));
    }
}
