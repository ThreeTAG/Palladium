package net.threetag.threecore.client.gui.ability;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.client.gui.widget.BackgroundlessButton;
import net.threetag.threecore.network.SetAbilityKeybindMessage;

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

        this.addButton(new BackgroundlessButton(i + 193, j + 3, 5, 5, new StringTextComponent(TextFormatting.DARK_GRAY + "x"), s -> parentScreen.overlayScreen = null));
        if (this.ability.getConditionManager().needsKey()) {
            keyButton = this.addButton(new ExtendedButton(i + 143, j + 30, 50, 20, new StringTextComponent("/"), (b) -> {
                this.listenToKey = !this.listenToKey;
                this.updateButton();
            }));
            this.updateButton();
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(AbilitiesScreen.WINDOW);
        this.blit(stack, i, j, 0, 196, this.guiWidth, this.guiHeight);

        this.font.drawString(stack, this.title.getString(), i + 8, j + 6, 4210752);
        if (this.keyButton != null)
            this.font.drawString(stack, I18n.format("gui.threecore.abilities.keybind"), i + 143, j + 20, 4210752);

        RenderSystem.pushMatrix();
        RenderSystem.translatef(i + 14, j + 18, -70);
        RenderSystem.scalef(2, 2, 1);
        this.ability.drawIcon(this.getMinecraft(), stack, this, 0, 0);
        RenderSystem.popMatrix();

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int type, int scanCode, int modifiers) {
        if (this.keyButton != null && this.listenToKey) {
            this.ability.getDataManager().set(Ability.KEYBIND, InputMappings.getInputByCode(type, scanCode).getKeyCode());
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new SetAbilityKeybindMessage(this.ability.container.getId(), this.ability.getId(), InputMappings.getInputByCode(type, scanCode).getKeyCode()));
            this.listenToKey = false;
            this.updateButton();
        }

        return super.keyPressed(type, scanCode, modifiers);
    }

    public void updateButton() {
        String button = this.ability.getDataManager().get(Ability.KEYBIND) == -1 ? "-" : InputMappings.getInputByCode(this.ability.getDataManager().get(Ability.KEYBIND), 0).func_237520_d_().getString();
        if (button.isEmpty())
            button = "-";
        this.keyButton.setMessage(new StringTextComponent(this.listenToKey ? "> " + TextFormatting.YELLOW + button + TextFormatting.RESET + " <" : button));
    }
}
