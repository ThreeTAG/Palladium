package net.threetag.palladium;

import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

public class PalladiumDebug {

    public static void init() {
        OverlayRegistry.registerOverlay("palladium/debug", (minecraft, gui, mStack, partialTicks, width, height) -> {
            if (minecraft.player instanceof PalladiumPlayerExtension extension) {
                float flightAnimation = extension.palladium_getFlightAnimation(partialTicks);
//                float leaning = Mth.clamp(flightAnimation, 0, 20) / 20F;
//
                minecraft.font.draw(mStack, "Flight: " + flightAnimation, 10, 40, 0xfefefe);
//                minecraft.font.draw(mStack, "Leaning: " + leaning, 10, 50, 0xfefefe);
//                minecraft.font.draw(mStack, "Speed: " + extension.palladium_getSpeed(partialTicks), 10, 60, 0xfefefe);
//                minecraft.font.draw(mStack, "xxa: " + minecraft.player.xxa, 10, 40, 0xfefefe);
//                minecraft.font.draw(mStack, "yya: " + minecraft.player.yya, 10, 50, 0xfefefe);
//                minecraft.font.draw(mStack, "zza: " + minecraft.player.zza, 10, 60, 0xfefefe);
            }
        });
    }

}
