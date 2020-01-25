package net.threetag.threecore.abilities.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.network.SetAbilityKeybindMessage;
import net.threetag.threecore.util.client.gui.BackgroundlessButton;

public class AbilityScreen extends Screen {

    public final Ability ability;
    public final AbilitiesScreen parentScreen;
    private final int guiWidth = 202;
    private final int guiHeight = 60;
    private boolean listenToKey = false;
    private Button keyButton;

    public AbilityScreen(Ability ability, AbilitiesScreen parentScreen) {
        super(ability.getDataManager().get(Ability.TITLE));
        this.ability = ability;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();

        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;
        this.addButton(new BackgroundlessButton(i + 193, j + 3, 5, 5, "x", s -> parentScreen.overlayScreen = null));
        if (this.ability.getConditionManager().needsKey()) {
            keyButton = this.addButton(new GuiButtonExt(i + 143, j + 30, 50, 20, "/", (b) -> {
                this.listenToKey = !this.listenToKey;
                this.updateButton();
            }));
            this.updateButton();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(AbilitiesScreen.WINDOW);
        this.blit(i, j, 0, 196, this.guiWidth, this.guiHeight);

        this.font.drawString(this.title.getFormattedText(), i + 8, j + 6, 4210752);
        if (this.keyButton != null)
            this.font.drawString(I18n.format("gui.threecore.abilities.keybind"), i + 143, j + 20, 4210752);

        GlStateManager.pushMatrix();
        GlStateManager.translatef(i + 14, j + 18, 0);
        GlStateManager.scalef(2F, 2F, 1);
        this.ability.getDataManager().get(Ability.ICON).draw(this.minecraft, 0, 0);
        GlStateManager.popMatrix();

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int type, int scanCode, int p_keyPressed_3_) {
        if (this.keyButton != null && this.listenToKey) {
            this.ability.getDataManager().set(Ability.KEYBIND, InputMappings.getInputByCode(type, scanCode).getKeyCode());
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new SetAbilityKeybindMessage(this.ability.container.getId(), this.ability.getId(), InputMappings.getInputByCode(type, scanCode).getKeyCode()));
            this.listenToKey = false;
            this.updateButton();
        }

        return super.keyPressed(type, scanCode, p_keyPressed_3_);
    }

    public void updateButton() {
        String button = InputMappings.getKeynameFromKeycode(this.ability.getDataManager().get(Ability.KEYBIND));
        if (button == null || button.isEmpty())
            button = "-";
        this.keyButton.setMessage(this.listenToKey ? "> " + TextFormatting.YELLOW + button + TextFormatting.RESET + " <" : button);
    }
}
