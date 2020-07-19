package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.StirlingGeneratorContainer;
import net.threetag.threecore.util.RenderUtil;
import net.threetag.threecore.util.TCFluidUtil;
import net.threetag.threecore.util.energy.EnergyUtil;

public class StirlingGeneratorScreen extends ContainerScreen<StirlingGeneratorContainer> {

    private static final ResourceLocation STIRLING_GENERATOR_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/stirling_generator.png");

    public StirlingGeneratorScreen(StirlingGeneratorContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.ySize = 216;
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(stack);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        this.field_230712_o_.func_238422_b_(stack, this.func_231171_q_(), (float) (this.xSize / 2 - this.field_230712_o_.func_238414_a_(this.func_231171_q_()) / 2), 6.0F, 4210752);
        this.field_230712_o_.func_238422_b_(stack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(stack, this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 8, 48, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
        TCFluidUtil.drawTooltip(this.container.stirlingGeneratorTileEntity.fluidTank, stack, this, 152, 38, 16, 60, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(STIRLING_GENERATOR_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.func_238474_b_(stack, left, top, 0, 0, this.xSize, this.ySize);
        int energy = (int) (this.container.getEnergyPercentage() * 40);
        this.func_238474_b_(stack, left + 8, top + 48 + 40 - energy, 194, 40 - energy, 12, energy);
        int burn = this.container.getBurnLeftScaled();
        this.func_238474_b_(stack, left + 80, top + 53 + 12 - burn, 206, 14 - burn, 14, burn + 1);

        RenderUtil.renderGuiTank(this.container.stirlingGeneratorTileEntity.fluidTank, 0, left + 152, top + 38, 0, 16, 60);

        this.field_230706_i_.getTextureManager().bindTexture(STIRLING_GENERATOR_GUI_TEXTURES);
        this.func_238474_b_(stack, left + 151, top + 37, 176, 0, 18, 62);
    }
}
