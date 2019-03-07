package com.threetag.threecore.base.inventory;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.tileentity.TileEntityGrinder;
import com.threetag.threecore.util.energy.EnergyUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGrinder extends GuiContainer {

    private static final ResourceLocation GRINDER_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/grinder.png");
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    public final InventoryPlayer inventoryPlayer;
    public final TileEntityGrinder tileEntityGrinder;

    public GuiGrinder(InventoryPlayer inventory, TileEntityGrinder tileEntityGrinder) {
        super(new ContainerGrinder(inventory, tileEntityGrinder));
        this.inventoryPlayer = inventory;
        this.tileEntityGrinder = tileEntityGrinder;
        this.ySize = 174;
    }

    @Override
    protected void initGui() {
        super.initGui();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.tileEntityGrinder.getDisplayName().getFormattedText();
        this.fontRenderer.drawString(name, (float) (this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2), 6.0F, 4210752);
        this.fontRenderer.drawString(this.inventoryPlayer.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        EnergyUtil.drawTooltip(this.tileEntityGrinder.clientEnergy, this.tileEntityGrinder.clientEnergyMax, this, 10, 17, 12, 40, mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GRINDER_GUI_TEXTURES);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
        int progress = this.getProgressScaled(24);
        this.drawTexturedModalRect(left + 67, top + 38, 176, 0, progress + 1, 16);
        int energy = (int) (((float) this.tileEntityGrinder.clientEnergy / (float) this.tileEntityGrinder.clientEnergyMax) * 40);
        drawTexturedModalRect(left + 10, top + 17 + 40 - energy, 176, 17 + 40 - energy, 12, energy);
    }

    private int getProgressScaled(int width) {
        double progress = this.tileEntityGrinder.clientProgress;
        double progressMax = this.tileEntityGrinder.clientProgressMax;
        return progressMax != 0 && progress != 0 ? (int) ((progress / progressMax) * width) : 0;
    }

}
