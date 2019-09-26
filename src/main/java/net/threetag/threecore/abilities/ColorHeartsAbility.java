package net.threetag.threecore.abilities;

import net.threetag.threecore.util.icon.TexturedIcon;
import net.threetag.threecore.util.threedata.ColorThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;

import java.awt.*;

public class ColorHeartsAbility extends Ability {

    public static ThreeData<Color> COLOR = new ColorThreeData("color").setSyncType(EnumSync.SELF).enableSetting("color", "Determines color for the tint for the hearts in the healthbar");

    public ColorHeartsAbility() {
        super(AbilityType.COLOR_HEARTS);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 0, 16, 16, 16, 256, 256, Color.BLUE));
        this.dataManager.register(COLOR, Color.BLUE);
    }
}
