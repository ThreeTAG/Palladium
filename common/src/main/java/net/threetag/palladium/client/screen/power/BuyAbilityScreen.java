package net.threetag.palladium.client.screen.power;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.threetag.palladium.client.screen.components.BackgroundlessButton;
import net.threetag.palladium.client.screen.components.TextWithIconButton;
import net.threetag.palladium.network.BuyAbilityUnlockMessage;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityReference;

import java.util.List;
import java.util.Objects;

public class BuyAbilityScreen extends Screen {

    public final AbilityReference reference;
    public final AbilityConfiguration.UnlockData unlockData;
    public final boolean available;
    public final PowersScreen parentScreen;
    private final Component text;
    private static final int GUI_WIDTH = 202;
    private static final int GUI_HEIGHT = 60;

    public BuyAbilityScreen(AbilityReference reference, AbilityConfiguration.UnlockData unlockData, boolean available, PowersScreen parentScreen) {
        super(Component.empty());
        this.reference = reference;
        this.unlockData = unlockData;
        this.available = available;
        this.parentScreen = parentScreen;
        this.text = Component.translatable("gui.palladium.powers.buy_ability");
    }

    @Override
    protected void init() {
        super.init();

        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        this.addRenderableWidget(new BackgroundlessButton(guiLeft + 193, guiTop + 3, 5, 5, Component.literal("x"), s -> parentScreen.closeOverlayScreen()));
        Button button = new TextWithIconButton(guiLeft + 23, guiTop + 33, 54, 20, Component.literal(this.unlockData.amount + "x "), null, this.unlockData.icon, s -> {
            new BuyAbilityUnlockMessage(this.reference).send();
            this.parentScreen.closeOverlayScreen();
            Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).playSound(SoundEvents.PLAYER_LEVELUP, 1F, 1F);
        }, (button1, poseStack, i, j) -> this.renderTooltip(poseStack, Component.literal(this.unlockData.amount + "x ").append(this.unlockData.description), i, j));
        button.active = this.available;
        this.addRenderableWidget(button);
        this.addRenderableWidget(new Button(guiLeft + 125, guiTop + 33, 54, 20, Component.translatable("gui.no"), s -> parentScreen.overlayScreen = null));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, PowersScreen.WINDOW);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(poseStack, guiLeft, guiTop, 0, 196, GUI_WIDTH, GUI_HEIGHT);

        List<FormattedCharSequence> lines = this.font.split(this.text, GUI_WIDTH - 40);
        for (int k = 0; k < lines.size(); k++) {
            FormattedCharSequence text = lines.get(k);
            int width = this.font.width(text);
            this.font.draw(poseStack, text, guiLeft + GUI_WIDTH / 2F - width / 2F, guiTop + 9 + k * 10, 4210752);
        }

        super.render(poseStack, mouseX, mouseY, partialTick);
    }
}
