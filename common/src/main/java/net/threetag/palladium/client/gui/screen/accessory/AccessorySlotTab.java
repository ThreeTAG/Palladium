package net.threetag.palladium.client.gui.screen.accessory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.tab.IconTab;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.client.icon.TexturedIcon;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

public class AccessorySlotTab extends GridLayoutTab implements IconTab {

    private final AccessoryScreen parent;
    private final PoseStackTransformation transformation;
    private final Icon icon;
    private final AccessoryGrid grid;

    public AccessorySlotTab(AccessoryScreen parent, AccessorySlot slot) {
        super(slot.name());
        this.parent = parent;
        this.transformation = slot.preview();
        var id = Objects.requireNonNull(Minecraft.getInstance().level).registryAccess().lookupOrThrow(PalladiumRegistryKeys.ACCESSORY_SLOT).getKey(slot);
        this.icon = new TexturedIcon(ResourceLocation.fromNamespaceAndPath(Objects.requireNonNull(id).getNamespace(), "textures/gui/accessory_slots/" + id.getPath() + ".png"));
        this.layout.addChild(this.grid = new AccessoryGrid(ScreenRectangle.empty(), slot, Minecraft.getInstance()), 0, 0, 1, 2);
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public void doLayout(ScreenRectangle rectangle) {
        super.doLayout(rectangle);
        this.grid.updateSize(rectangle);
        this.parent.changePreview(this.transformation);
    }
}
