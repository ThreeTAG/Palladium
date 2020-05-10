package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
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

    public final PlayerInventory playerInventory;
    public final StirlingGeneratorContainer container;

    public StirlingGeneratorScreen(StirlingGeneratorContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.playerInventory = inventory;
        this.container = container;
        this.ySize = 216;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), (float) (this.xSize / 2 - this.font.getStringWidth(this.title.getFormattedText()) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 8, 48, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
        TCFluidUtil.drawTooltip(this.container.stirlingGeneratorTileEntity.fluidTank, this, 152, 38, 16, 60, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(STIRLING_GENERATOR_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(left, top, 0, 0, this.xSize, this.ySize);
        int energy = (int) (this.container.getEnergyPercentage() * 40);
        this.blit(left + 8, top + 48 + 40 - energy, 194, 40 - energy, 12, energy);
        int burn = this.container.getBurnLeftScaled();
        this.blit(left + 80, top + 53 + 12 - burn, 206, 14 - burn, 14, burn + 1);

        RenderUtil.renderGuiTank(this.container.stirlingGeneratorTileEntity.fluidTank, 0, left + 152, top + 38, 0, 16, 60);

        this.minecraft.getTextureManager().bindTexture(STIRLING_GENERATOR_GUI_TEXTURES);
        this.blit(left + 151, top + 37, 176, 0, 18, 62);
    }
}
