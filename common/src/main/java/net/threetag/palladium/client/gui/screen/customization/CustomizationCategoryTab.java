package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.tab.IconTab;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.client.icon.TexturedIcon;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

public class CustomizationCategoryTab extends GridLayoutTab implements IconTab {

    private final PlayerCustomizationScreen parent;
    private final PoseStackTransformation transformation;
    private final Icon icon;
    private final CustomizationsGrid grid;

    public CustomizationCategoryTab(PlayerCustomizationScreen parent, CustomizationCategory slot) {
        super(Component.translatable(CustomizationCategory.makeDescriptionId(slot, Minecraft.getInstance().level.registryAccess())));
        this.parent = parent;
        this.transformation = slot.preview();
        var id = Objects.requireNonNull(Minecraft.getInstance().level).registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).getKey(slot);
        this.icon = new TexturedIcon(ResourceLocation.fromNamespaceAndPath(Objects.requireNonNull(id).getNamespace(), "textures/gui/customization_categories/" + id.getPath() + ".png"));
        this.layout.addChild(this.grid = new CustomizationsGrid(ScreenRectangle.empty(), slot, Minecraft.getInstance()), 0, 0, 1, 2);
    }

    public void tick(boolean gamePaused) {
        this.grid.tick(gamePaused);
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
