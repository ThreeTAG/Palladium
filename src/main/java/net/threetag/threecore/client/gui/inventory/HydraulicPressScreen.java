package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.HydraulicPressContainer;
import net.threetag.threecore.util.energy.EnergyUtil;

public class HydraulicPressScreen extends ContainerScreen<HydraulicPressContainer> {

    private static final ResourceLocation HYDRAULIC_PRESS_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/hydraulic_press.png");

    public final PlayerInventory inventoryPlayer;
    public final HydraulicPressContainer hydraulicPressContainer;

    public HydraulicPressScreen(HydraulicPressContainer hydraulicPressContainer, PlayerInventory inventory, ITextComponent title) {
        super(hydraulicPressContainer, inventory, title);
        this.inventoryPlayer = inventory;
        this.hydraulicPressContainer = hydraulicPressContainer;
        this.ySize = 174;
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
        this.font.drawString(this.inventoryPlayer.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(this.hydraulicPressContainer.getEnergyStored(), this.hydraulicPressContainer.getMaxEnergyStored(), this, 10, 17, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(HYDRAULIC_PRESS_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(left, top, 0, 0, this.xSize, this.ySize);
        int progress = this.hydraulicPressContainer.getProgressScaled(24);
        this.blit(left + 97, top + 38, 176, 0, progress + 1, 16);
        int energy = (int) (this.hydraulicPressContainer.getEnergyPercentage() * 40);
        this.blit(left + 10, top + 17 + 40 - energy, 176, 17 + 40 - energy, 12, energy);
        Slot slot = this.container.getSlot(1);
        if (slot.getStack().isEmpty())
            this.blit(left + slot.xPos, top + slot.yPos, 200, 0, 16, 16);
    }

}
