package net.threetag.threecore.client.gui.ability;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.condition.BuyableAbilityCondition;
import net.threetag.threecore.client.gui.widget.BackgroundlessButton;
import net.threetag.threecore.network.BuyConditionMessage;
import net.threetag.threecore.util.icon.IIcon;

import java.util.List;

public class BuyAbilityScreen extends Screen {

    public final Ability ability;
    public final BuyableAbilityCondition condition;
    public final AbilitiesScreen parentScreen;
    public final IIcon icon;
    public final ITextComponent hoverText;
    private final int guiWidth = 202;
    private final int guiHeight = 60;

    public BuyAbilityScreen(Ability ability, BuyableAbilityCondition condition, IIcon icon, ITextComponent hoverText, AbilitiesScreen parentScreen) {
        super(condition.getDisplayName());
        this.ability = ability;
        this.condition = condition;
        this.icon = icon;
        this.hoverText = hoverText;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();

        int i = (this.field_230708_k_ - guiWidth) / 2;
        int j = (this.field_230709_l_ - guiHeight) / 2;
        this.func_230480_a_(new BackgroundlessButton(i + 193, j + 3, 5, 5, new StringTextComponent("x"), s -> parentScreen.overlayScreen = null));
        Button button = new Button(i + 60, j + 33, 54, 20, new StringTextComponent(I18n.format("gui.yes")), s -> {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new BuyConditionMessage(this.ability.container.getId(), this.ability.getId(), this.condition.getUniqueId()));
            this.field_230706_i_.player.closeScreen();
            this.field_230706_i_.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        });
        //button.active
        button.field_230693_o_ = this.condition.isAvailable(this.field_230706_i_.player);
        this.func_230480_a_(button);
        this.func_230480_a_(new Button(i + 132, j + 33, 54, 20, new StringTextComponent(I18n.format("gui.no")), s -> parentScreen.overlayScreen = null));
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int i = (this.field_230708_k_ - guiWidth) / 2;
        int j = (this.field_230709_l_ - guiHeight) / 2;

//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(AbilitiesScreen.WINDOW);
        this.func_238474_b_(stack, i, j, 0, 196, this.guiWidth, this.guiHeight);

        List<ITextProperties> lines = this.field_230712_o_.func_238425_b_(new StringTextComponent(I18n.format("gui.threecore.abilities.fulfill_condition")), 132);
        for (int k = 0; k < lines.size(); k++) {
            ITextProperties text = lines.get(k);
            int width = this.field_230712_o_.func_238414_a_(text);
            this.field_230712_o_.func_238422_b_(stack, text, i + 120 - width / 2f, j + 9 + k * 10, 4210752);
        }

        stack.push();
        stack.translate(i + 14, j + 14, 0);
        stack.scale(2, 2, 1);
        this.icon.draw(this.field_230706_i_, stack, 0, 0);
        stack.pop();

        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);

        if (mouseX >= i + 14 && mouseX <= i + 14 + 32 && mouseY >= j + 14 && mouseY <= j + 14 + 32) {
            this.func_238652_a_(stack, this.hoverText, mouseX, mouseY);
        }
    }
}
