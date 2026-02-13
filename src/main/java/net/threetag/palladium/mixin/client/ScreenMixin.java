package net.threetag.palladium.mixin.client;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.threetag.palladium.client.gui.widget.TickableWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    @Final
    private List<GuiEventListener> children;

    @Shadow
    public abstract void init(int p_96608_, int p_96609_);

    @Inject(method = "tick", at = @At("RETURN"))
    private void widgetTick(CallbackInfo ci) {
        for (GuiEventListener child : this.children) {
            if (child instanceof TickableWidget tickableWidget) {
                tickableWidget.tick();
            }
        }
    }

}
