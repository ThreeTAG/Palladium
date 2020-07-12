package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.CapacitorBlockContainer;
import net.threetag.threecore.util.energy.EnergyUtil;

public class CapacitorBlockScreen extends ContainerScreen<CapacitorBlockContainer> {

    private static final ResourceLocation CAPACITOR_BLOCK_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/capacitor_block.png");

    public CapacitorBlockScreen(CapacitorBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.ySize = 132;
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(stack);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        this.field_230712_o_.func_238407_a_(stack, this.func_231171_q_(), 8.0F, 6.0F, 4210752);
        this.field_230712_o_.func_238407_a_(stack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(stack, this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 68, 20, 40, 12, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(CAPACITOR_BLOCK_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.func_238474_b_(stack, left, top, 0, 0, this.xSize, this.ySize);
        int energy = (int) (this.container.getEnergyPercentage() * 40);
        this.func_238474_b_(stack, left + 68, top + 20, 176, 0, energy, 12);
    }
}
