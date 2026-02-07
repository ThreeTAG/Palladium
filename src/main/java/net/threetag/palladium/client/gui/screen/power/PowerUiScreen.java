package net.threetag.palladium.client.gui.screen.power;

import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.gui.ui.layout.SimpleUiLayout;
import net.threetag.palladium.power.PowerInstance;

public class PowerUiScreen extends UiScreen {

    private final PowerInstance powerInstance;

    public PowerUiScreen(SimpleUiLayout configuration, PowerInstance powerInstance) {
        super(configuration);
        this.powerInstance = powerInstance;
    }

    public PowerInstance getPowerInstance() {
        return this.powerInstance;
    }
}
