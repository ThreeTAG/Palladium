package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.ModalScreen;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.gui.ui.component.PowerTreeUiComponent;
import net.threetag.palladium.client.gui.ui.component.TextUiComponent;
import net.threetag.palladium.client.gui.ui.component.UiComponentProperties;
import net.threetag.palladium.client.gui.ui.layout.SimpleUiLayout;
import net.threetag.palladium.client.gui.ui.screen.UiPadding;
import net.threetag.palladium.client.gui.ui.screen.UiScreenBackground;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.datagen.UiLayoutProvider;
import net.threetag.palladium.logic.condition.TrueCondition;
import net.threetag.palladium.power.Power;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PalladiumUiLayoutProvider extends UiLayoutProvider {

    public PalladiumUiLayoutProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.unconditional(Power.DEFAULT_POWER_SCREEN, new SimpleUiLayout(
                256,
                196,
                UiPadding.SEVEN,
                new UiScreenBackground.Sprite(ModalScreen.BACKGROUND_MODAL_HEADER),
                Arrays.asList(
                        new TextUiComponent(Component.translatable("gui.palladium.powers"), RenderUtil.DEFAULT_GRAY_COLOR, false, UiComponentProperties.withDefaultSize(200, 10)),
                        new PowerTreeUiComponent(null, UiScreenBackground.RepeatingTexture.RED_WOOL, new UiComponentProperties(
                                UiAlignment.BOTTOM_CENTER, 0, 0, 256 - 7 - 7, 196 - 20 - 7, Optional.empty(), Optional.empty(), TrueCondition.INSTANCE
                        ))
                )));
    }
}
