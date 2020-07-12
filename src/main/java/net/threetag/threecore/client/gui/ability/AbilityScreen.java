package net.threetag.threecore.client.gui.ability;

import com.mojang.blaze3d.matrix.MatrixStack;
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
    protected void func_231160_c_() {
        super.func_231160_c_();

        int i = (this.field_230708_k_ - guiWidth) / 2;
        int j = (this.field_230709_l_ - guiHeight) / 2;
        //addButton
        this.func_230480_a_(new BackgroundlessButton(i + 193, j + 3, 5, 5, new StringTextComponent(TextFormatting.DARK_GRAY + "x"), s -> parentScreen.overlayScreen = null));
        if (this.ability.getConditionManager().needsKey()) {
            keyButton = this.func_230480_a_(new ExtendedButton(i + 143, j + 30, 50, 20, new StringTextComponent("/"), (b) -> {
                this.listenToKey = !this.listenToKey;
                this.updateButton();
            }));
            this.updateButton();
        }
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int i = (this.field_230708_k_ - guiWidth) / 2;
        int j = (this.field_230709_l_ - guiHeight) / 2;

//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(AbilitiesScreen.WINDOW);
        this.func_238474_b_(stack, i, j, 0, 196, this.guiWidth, this.guiHeight);

        this.field_230712_o_.func_238422_b_(stack, this.func_231171_q_(), i + 8, j + 6, 4210752);
        if (this.keyButton != null)
            this.field_230712_o_.func_238421_b_(stack, I18n.format("gui.threecore.abilities.keybind"), i + 143, j + 20, 4210752);

        stack.push();
        stack.translate(i + 14, j + 18, -70);
        stack.scale(2, 2, 1);
        this.ability.drawIcon(this.field_230706_i_, stack, this, 0, 0);
        stack.pop();

        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean func_231046_a_(int type, int scanCode, int p_keyPressed_3_) {
        if (this.keyButton != null && this.listenToKey) {
            this.ability.getDataManager().set(Ability.KEYBIND, InputMappings.getInputByCode(type, scanCode).getKeyCode());
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new SetAbilityKeybindMessage(this.ability.container.getId(), this.ability.getId(), InputMappings.getInputByCode(type, scanCode).getKeyCode()));
            this.listenToKey = false;
            this.updateButton();
        }

        return super.func_231046_a_(type, scanCode, p_keyPressed_3_);
    }

    public void updateButton() {
        String button = this.ability.getDataManager().get(Ability.KEYBIND) == -1 ? "-" : InputMappings.getInputByCode(this.ability.getDataManager().get(Ability.KEYBIND), 0).getTranslationKey();
        if (button.isEmpty())
            button = "-";
        this.keyButton.func_238482_a_(new StringTextComponent(this.listenToKey ? "> " + TextFormatting.YELLOW + button + TextFormatting.RESET + " <" : button));
    }
}
