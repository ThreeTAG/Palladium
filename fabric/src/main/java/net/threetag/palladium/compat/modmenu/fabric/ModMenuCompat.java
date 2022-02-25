package net.threetag.palladium.compat.modmenu.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.threetag.palladium.fabric.PalladiumConfigImpl;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(PalladiumConfigImpl.class, parent).get();
    }
}
