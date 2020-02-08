package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.platform.GlStateManager;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.GrinderContainer;
import net.threetag.threecore.util.energy.EnergyUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrinderScreen extends ContainerScreen<GrinderContainer> {

    private static final ResourceLocation GRINDER_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/grinder.png");

    public final PlayerInventory inventoryPlayer;
    public final GrinderContainer grinderContainer;

    public GrinderScreen(GrinderContainer grinderContainer, PlayerInventory inventory, ITextComponent title) {
        super(grinderContainer, inventory, title);
        this.inventoryPlayer = inventory;
        this.grinderContainer = grinderContainer;
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
        EnergyUtil.drawTooltip(this.grinderContainer.getEnergyStored(), this.grinderContainer.getMaxEnergyStored(), this, 10, 17, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GRINDER_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(left, top, 0, 0, this.xSize, this.ySize);
        int progress = this.grinderContainer.getProgressScaled(24);
        this.blit(left + 67, top + 38, 176, 0, progress + 1, 16);
        int energy = (int) (this.grinderContainer.getEnergyPercentage() * 40);
        this.blit(left + 10, top + 17 + 40 - energy, 176, 17 + 40 - energy, 12, energy);
    }

}
