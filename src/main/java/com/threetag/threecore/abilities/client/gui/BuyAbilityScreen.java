package com.threetag.threecore.abilities.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.condition.BuyableAbilityCondition;
import com.threetag.threecore.abilities.condition.Condition;
import com.threetag.threecore.abilities.network.BuyConditionMessage;
import com.threetag.threecore.util.client.gui.BackgroundlessButton;
import com.threetag.threecore.util.render.IIcon;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class BuyAbilityScreen extends Screen {

    public final Ability ability;
    public final BuyableAbilityCondition condition;
    public final AbilityScreen parentScreen;
    public final IIcon icon;
    public final ITextComponent hoverText;
    private final int guiWidth = 202;
    private final int guiHeight = 60;

    public BuyAbilityScreen(Ability ability, BuyableAbilityCondition condition, IIcon icon, ITextComponent hoverText, AbilityScreen parentScreen) {
        super(condition.getDataManager().get(Condition.TITLE));
        this.ability = ability;
        this.condition = condition;
        this.icon = icon;
        this.hoverText = hoverText;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();

        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;
        this.addButton(new BackgroundlessButton(i + 193, j + 3, 5, 5, "x", s -> parentScreen.overlayScreen = null));
        Button button = new Button(i + 60, j + 33, 54, 20, I18n.format("gui.yes"), s -> {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new BuyConditionMessage(this.ability.container.getId(), this.ability.getId(), this.condition.getUniqueId()));
            this.minecraft.player.closeScreen();
            this.minecraft.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        });
        button.active = this.condition.isAvailable(this.minecraft.player);
        this.addButton(button);
        this.addButton(new Button(i + 132, j + 33, 54, 20, I18n.format("gui.no"), s -> parentScreen.overlayScreen = null));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(AbilityScreen.WINDOW);
        this.blit(i, j, 0, 196, this.guiWidth, this.guiHeight);

        List<String> lines = this.font.listFormattedStringToWidth(I18n.format("gui.threecore.abilities.fulfill_condition"), 132);
        for (int k = 0; k < lines.size(); k++) {
            String text = lines.get(k);
            int width = this.font.getStringWidth(text);
            this.font.drawString(text, i + 120 - width / 2, j + 9 + k * 10, 4210752);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(i + 14, j + 14, 0);
        GlStateManager.scalef(2F, 2F, 1);
        this.icon.draw(this.minecraft, 0, 0);
        GlStateManager.popMatrix();

        super.render(mouseX, mouseY, partialTicks);

        if (mouseX >= i + 14 && mouseX <= i + 14 + 32 && mouseY >= j + 14 && mouseY <= j + 14 + 32) {
            this.renderTooltip(this.hoverText.getFormattedText(), mouseX, mouseY);
        }
    }
}
