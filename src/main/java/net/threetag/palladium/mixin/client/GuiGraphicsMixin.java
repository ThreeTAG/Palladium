package net.threetag.palladium.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.client.util.RenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiGraphics.RenderingTextCollector.class)
public class GuiGraphicsMixin {

    @ModifyArg(
            method = "accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/client/gui/ActiveTextCollector$Parameters;Lnet/minecraft/util/FormattedCharSequence;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiTextRenderState;<init>(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;Lorg/joml/Matrix3x2fc;IIIIZZLnet/minecraft/client/gui/navigation/ScreenRectangle;)V"),
            index = 5
    )
    private int changedColor(int original) {
        return RenderUtil.getStringColorOverride(original);
    }

    @ModifyArg(
            method = "accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/client/gui/ActiveTextCollector$Parameters;Lnet/minecraft/util/FormattedCharSequence;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiTextRenderState;<init>(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;Lorg/joml/Matrix3x2fc;IIIIZZLnet/minecraft/client/gui/navigation/ScreenRectangle;)V"),
            index = 7
    )
    private boolean changedShadow(boolean original) {
        return RenderUtil.getStringShadowOverride(original);
    }

}
