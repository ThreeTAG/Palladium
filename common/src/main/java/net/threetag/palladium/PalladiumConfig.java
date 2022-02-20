package net.threetag.palladium;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

public class PalladiumConfig {

    @ExpectPlatform
    public static AbilityBarRenderer.Position getAbilityBarPosition() {
        throw new AssertionError();
    }

}
