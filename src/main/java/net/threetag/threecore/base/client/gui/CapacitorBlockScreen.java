package net.threetag.threecore.base.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.inventory.CapacitorBlockContainer;
import net.threetag.threecore.util.energy.EnergyUtil;

public class CapacitorBlockScreen extends ContainerScreen<CapacitorBlockContainer> {

    private static final ResourceLocation CAPACITOR_BLOCK_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/capacitor_block.png");

    public final PlayerInventory inventoryPlayer;
    public final CapacitorBlockContainer screenContainer;

    public CapacitorBlockScreen(CapacitorBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.ySize = 132;
        this.inventoryPlayer = inv;
        this.screenContainer = screenContainer;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.inventoryPlayer.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this, 68, 20, 40, 12, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CAPACITOR_BLOCK_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(left, top, 0, 0, this.xSize, this.ySize);
        int energy = (int) (this.screenContainer.getEnergyPercentage() * 40);
        this.blit(left + 68, top + 20, 176, 0, energy, 12);
    }
}
