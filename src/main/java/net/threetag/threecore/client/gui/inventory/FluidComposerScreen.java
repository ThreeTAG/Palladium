package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.FluidComposerContainer;
import net.threetag.threecore.util.RenderUtil;
import net.threetag.threecore.util.TCFluidUtil;
import net.threetag.threecore.util.energy.EnergyUtil;

public class FluidComposerScreen extends ContainerScreen<FluidComposerContainer> {

    private static final ResourceLocation FLUID_COMPOSER_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/fluid_composer.png");

    public FluidComposerScreen(FluidComposerContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.ySize = 216;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.func_238414_a_(this.title)) / 2;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
        EnergyUtil.drawTooltip(stack, this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 68, 97, 40, 12, mouseX - this.guiLeft, mouseY - this.guiTop);
        TCFluidUtil.drawTooltip(this.container.fluidComposerTileEntity.inputFluidTank, stack, this, 8, 38, 16, 60, mouseX - this.guiLeft, mouseY - this.guiTop);
        TCFluidUtil.drawTooltip(this.container.fluidComposerTileEntity.outputFluidTank, stack, this, 152, 38, 16, 60, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(FLUID_COMPOSER_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(stack, left, top, 0, 0, this.xSize, this.ySize);
        int energy = (int) (this.container.getEnergyPercentage() * 40);
        this.blit(stack, left + 68, top + 97, 200, 0, energy, 12);
        int progress = this.container.getProgressScaled(24);
        this.blit(stack, left + 108, top + 46, 176, 0, progress, 16);

        RenderUtil.renderGuiTank(this.container.fluidComposerTileEntity.inputFluidTank, 0, left + 8, top + 38, 0, 16, 60);
        RenderUtil.renderGuiTank(this.container.fluidComposerTileEntity.outputFluidTank, 0, left + 152, top + 38, 0, 16, 60);

        this.getMinecraft().getTextureManager().bindTexture(FLUID_COMPOSER_GUI_TEXTURES);
        this.blit(stack, left + 7, top + 37, 176, 17, 18, 62);
        this.blit(stack, left + 151, top + 37, 176, 17, 18, 62);
    }
}
