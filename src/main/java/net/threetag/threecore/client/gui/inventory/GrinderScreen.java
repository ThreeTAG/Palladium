package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.GrinderContainer;
import net.threetag.threecore.util.energy.EnergyUtil;

public class GrinderScreen extends ContainerScreen<GrinderContainer> {

    private static final ResourceLocation GRINDER_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/grinder.png");

    public GrinderScreen(GrinderContainer grinderContainer, PlayerInventory inventory, ITextComponent title) {
        super(grinderContainer, inventory, title);
        this.ySize = 174;
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(stack);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        this.field_230712_o_.func_238407_a_(stack, this.func_231171_q_(), (float) (this.xSize / 2 - this.field_230712_o_.getStringWidth(this.func_231171_q_().toString()) / 2), 6.0F, 4210752);
        this.field_230712_o_.func_238407_a_(stack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(stack, this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 10, 17, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(GRINDER_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.func_238474_b_(stack, left, top, 0, 0, this.xSize, this.ySize);
        int progress = this.container.getProgressScaled(24);
        this.func_238474_b_(stack, left + 67, top + 38, 176, 0, progress + 1, 16);
        int energy = (int) (this.container.getEnergyPercentage() * 40);
        this.func_238474_b_(stack, left + 10, top + 17 + 40 - energy, 176, 17 + 40 - energy, 12, energy);
    }

}
