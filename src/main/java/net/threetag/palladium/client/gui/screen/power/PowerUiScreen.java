package net.threetag.palladium.client.gui.screen.power;

import net.threetag.palladium.client.gui.ui.screen.TabUiScreen;
import net.threetag.palladium.power.PowerInstance;

import java.util.List;
import java.util.Map;

public class PowerUiScreen extends TabUiScreen {

    private PowerInstance powerInstance;
    private final Map<Tab, PowerInstance> mapping;

    public PowerUiScreen(List<Tab> tabs, Map<Tab, PowerInstance> mapping) {
        super(tabs);
        this.mapping = mapping;
        this.powerInstance = this.mapping.get(this.getSelectedTab());
    }

    @Override
    public void setTab(Tab tab) {
        this.powerInstance = this.mapping.get(tab);
        super.setTab(tab);
    }

    public PowerInstance getPowerInstance() {
        return this.powerInstance;
    }
}
