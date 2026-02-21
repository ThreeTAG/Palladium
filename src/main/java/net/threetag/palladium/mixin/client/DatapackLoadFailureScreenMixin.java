package net.threetag.palladium.mixin.client;

import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.threetag.palladium.addonpack.AddonPackErrorScreenUtil;
import net.threetag.palladium.config.PalladiumClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Original: ThePotatoArchivist/DatapackErrorViewer, 2025, MIT-License <br>
 * <a href="https://github.com/ThePotatoArchivist/DatapackErrorViewer/blob/main/src/main/java/archives/tater/datapackerrors/mixin/DatapackLoadFailureScreenMixin.java">Repository</a>
 */
@Mixin(DatapackLoadFailureScreen.class)
public class DatapackLoadFailureScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!PalladiumClientConfig.DEV_MODE.getAsBoolean()) {
            return;
        }

        var screen = (DatapackLoadFailureScreen) (Object) this;
        var widget = AddonPackErrorScreenUtil.getErrorsWidget(screen, screen.getFont());

        if (widget != null) {
            screen.addRenderableWidget(widget);
        }
    }

    @ModifyArg(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"),
            index = 1
    )
    private int moveDownButtons(int y) {
        if (!PalladiumClientConfig.DEV_MODE.getAsBoolean()) {
            return y;
        }

        return ((DatapackLoadFailureScreen) (Object) this).height / 2 + 90;
    }

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultiLineLabel;visitLines(Lnet/minecraft/client/gui/TextAlignment;IIILnet/minecraft/client/gui/ActiveTextCollector;)I"),
            index = 2
    )
    private int moveUpText(int y) {
        if (!PalladiumClientConfig.DEV_MODE.getAsBoolean()) {
            return y;
        }

        return ((DatapackLoadFailureScreen) (Object) this).height / 2 - 100;
    }

}
