package net.threetag.palladium.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladiumcore.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public interface PalladiumClientEvents {

    Event<RegisterAnimations> REGISTER_ANIMATIONS = new Event<>(RegisterAnimations.class, listeners -> (r) -> {
        for (RegisterAnimations listener : listeners) {
            listener.register(r);
        }
    });

    Event<RenderPowerScreen> RENDER_POWER_SCREEN = new Event<>(RenderPowerScreen.class, listeners -> (s, g, x, y, p, t) -> {
        for (RenderPowerScreen listener : listeners) {
            listener.renderPowerScreen(s, g, x, y, p, t);
        }
    });

    interface RegisterAnimations {

        void register(BiConsumer<ResourceLocation, PalladiumAnimation> registry);

    }

    interface RenderPowerScreen {

        void renderPowerScreen(PowersScreen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, @Nullable ResourceLocation tab);

    }

}
