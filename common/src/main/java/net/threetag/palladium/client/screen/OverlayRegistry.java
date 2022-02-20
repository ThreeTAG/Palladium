package net.threetag.palladium.client.screen;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class OverlayRegistry {

    @ExpectPlatform
    public static void registerOverlay(String displayName, IIngameOverlay overlay) {
        throw new AssertionError();
    }

}
