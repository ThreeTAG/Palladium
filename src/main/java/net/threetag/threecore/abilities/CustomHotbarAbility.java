package net.threetag.threecore.abilities;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.HotbarElementThreeData;
import net.threetag.threecore.util.threedata.ResourceLocationThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class CustomHotbarAbility extends Ability {

    public static final ThreeData<ResourceLocation> TEXTURE = new ResourceLocationThreeData("texture").setSyncType(EnumSync.SELF).enableSetting("texture", "Determines texture for overriding the hotbars one.");
    public static final ThreeData<RenderGameOverlayEvent.ElementType> HOTBAR_ELEMENT = new HotbarElementThreeData("hotbar_element").setSyncType(EnumSync.SELF).enableSetting("hotbar_element", "Specified the hotbar element which shall be changed. Possible values: " + HotbarElementThreeData.getElementList());

    public CustomHotbarAbility() {
        super(AbilityType.CUSTOM_HOTBAR);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new ItemIcon(Items.STICK));
        this.dataManager.register(HOTBAR_ELEMENT, RenderGameOverlayEvent.ElementType.HEALTH);
        this.dataManager.register(TEXTURE, AbstractGui.GUI_ICONS_LOCATION);
    }
}
