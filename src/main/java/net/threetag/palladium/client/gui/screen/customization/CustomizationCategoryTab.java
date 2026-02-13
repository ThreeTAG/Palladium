package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.client.gui.widget.tab.IconTab;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.icon.TexturedIcon;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

public class CustomizationCategoryTab extends GridLayoutTab implements IconTab {

    private final PlayerCustomizationScreen parent;
    private final CustomizationPreview transformation;
    private final Icon icon;
    private final CustomizationsGrid grid;

    public CustomizationCategoryTab(PlayerCustomizationScreen parent, Holder<CustomizationCategory> customizationCategory) {
        super(Component.translatable(CustomizationCategory.makeDescriptionId(customizationCategory.value(), Minecraft.getInstance().level.registryAccess())));
        this.parent = parent;
        this.transformation = customizationCategory.value().preview();
        var id = Objects.requireNonNull(Minecraft.getInstance().level).registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).getKey(customizationCategory.value());
        this.icon = new TexturedIcon(Identifier.fromNamespaceAndPath(Objects.requireNonNull(id).getNamespace(), "textures/gui/customization_categories/" + id.getPath() + ".png"));
        this.layout.addChild(this.grid = new CustomizationsGrid(ScreenRectangle.empty(), customizationCategory, Minecraft.getInstance()), 0, 0, 1, 2);
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
