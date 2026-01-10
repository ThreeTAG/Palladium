package net.threetag.palladium.client.gui.component;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.threetag.palladium.Palladium;

public class EditButton extends ImageButton {

    public static final WidgetSprites SPRITES = new WidgetSprites(
            Palladium.id("widget/pencil"), Palladium.id("widget/pencil_disabled"), Palladium.id("widget/pencil_highlighted")
    );

    public EditButton(int x, int y, OnPress onPress) {
        super(x, y, 12, 12, SPRITES, onPress);
    }
}
