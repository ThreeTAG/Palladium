package net.threetag.palladium.client.gui.screen.power;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.threetag.palladium.client.gui.component.CloseButton;
import net.threetag.palladium.client.gui.component.TextWithIconButton;
import net.threetag.palladium.network.BuyAbilityPacket;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.unlocking.BuyableUnlockingHandler;

import java.util.List;
import java.util.Objects;

public class BuyAbilityScreen extends Screen {

    public final AbilityReference reference;
    public final BuyableUnlockingHandler.Display display;
    public final boolean available;
    public final PowersScreen parentScreen;
    private final Component text;
    private static final int GUI_WIDTH = 202;
    private static final int GUI_HEIGHT = 60;

    public BuyAbilityScreen(AbilityReference reference, BuyableUnlockingHandler.Display display, boolean available, PowersScreen parentScreen) {
        super(Component.empty());
        this.reference = reference;
        this.display = display;
        this.available = available;
        this.parentScreen = parentScreen;
        this.text = Component.translatable("gui.palladium.powers.buy_ability");
    }

    @Override
    protected void init() {
        super.init();
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        this.addRenderableWidget(new CloseButton(guiLeft + 191, guiTop + 4, button -> this.parentScreen.closeOverlayScreen()));
        
        Button button = TextWithIconButton.textWithIconBuilder(Component.literal(this.display.amount() + "x "), this.display.icon(), s -> {
            NetworkManager.sendToServer(new BuyAbilityPacket(this.reference));
            this.parentScreen.closeOverlayScreen();
            Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).playSound(SoundEvents.PLAYER_LEVELUP, 1F, 1F);
        }).bounds(guiLeft + 23, guiTop + 33, 54, 20).build();
        button.setTooltip(Tooltip.create(this.display.description()));
        button.active = this.available;
        this.addRenderableWidget(button);

        this.addRenderableWidget(Button.builder(Component.translatable("gui.no"), s -> parentScreen.overlayScreen = null).bounds(guiLeft + 125, guiTop + 33, 54, 20).build());
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,PowersScreen.WINDOW, guiLeft, guiTop, 0, 196, GUI_WIDTH, GUI_HEIGHT, 256, 256);

        List<FormattedCharSequence> lines = this.font.split(this.text, GUI_WIDTH - 40);
        for (int k = 0; k < lines.size(); k++) {
            FormattedCharSequence text = lines.get(k);
            int width = this.font.width(text);
            guiGraphics.drawString(font, text, (int) (guiLeft + GUI_WIDTH / 2F - width / 2F), guiTop + 9 + k * 10, 4210752, false);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

}
