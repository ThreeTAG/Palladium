package net.threetag.palladium.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

@Config(name = Palladium.MOD_ID)
public class PalladiumConfigImpl implements ConfigData {

    @ConfigEntry.Category("client")
    AbilityBarRenderer.Position abilityBarPosition = AbilityBarRenderer.Position.TOP_LEFT;

    public static AbilityBarRenderer.Position getAbilityBarPosition() {
        return AutoConfig.getConfigHolder(PalladiumConfigImpl.class).getConfig().abilityBarPosition;
    }
}
