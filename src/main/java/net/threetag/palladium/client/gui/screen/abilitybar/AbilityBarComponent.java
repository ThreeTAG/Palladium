package net.threetag.palladium.client.gui.screen.abilitybar;

import net.threetag.palladium.client.gui.ui.component.RenderableUiComponent;
import net.threetag.palladium.client.gui.ui.component.UiComponentPosition;
import net.threetag.palladium.client.gui.ui.component.UiComponentSerializer;

public interface AbilityBarComponent extends RenderableUiComponent {

    @Override
    default UiComponentPosition getPosition() {
        return UiComponentPosition.TOP_LEFT;
    }

    @Override
    default UiComponentSerializer<?> getSerializer() {
        return null;
    }
}
