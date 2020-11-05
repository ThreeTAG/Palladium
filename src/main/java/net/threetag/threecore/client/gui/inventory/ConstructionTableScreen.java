package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.ConstructionTableBlock;
import net.threetag.threecore.client.gui.widget.FlatIconButton;
import net.threetag.threecore.container.*;
import net.threetag.threecore.network.OpenConstructionTableTabMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConstructionTableScreen<T extends AbstractConstructionTableContainer> extends ContainerScreen<T> {

    private static double mouseX;
    private static double mouseY;
    public static final ResourceLocation HELMET_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/construction_table/helmet_crafting.png");
    public static final ResourceLocation CHESTPLATE_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/construction_table/chestplate_crafting.png");
    public static final ResourceLocation LEGGINGS_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/construction_table/leggings_crafting.png");
    public static final ResourceLocation BOOTS_GUI_TEXTURES = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/construction_table/boots_crafting.png");
    private final ResourceLocation texture;

    public ConstructionTableScreen(T container, PlayerInventory playerInventory, ITextComponent title, ResourceLocation texture) {
        this(container, playerInventory, title, texture, 216);
    }

    public ConstructionTableScreen(T container, PlayerInventory playerInventory, ITextComponent title, ResourceLocation texture, int ySize) {
        super(container, playerInventory, title);
        this.texture = texture;
        this.ySize = ySize;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    protected void init() {
        super.init();

        // Ugly fix to prevent that the mouse cursor goes back to the center of the screen once you change the tab
        if (mouseX > -1 && mouseY > -1) {
            InputMappings.setCursorPosAndMode(this.getMinecraft().getMainWindow().getHandle(), 212993, mouseX, mouseY);
            mouseX = mouseY = -1;
        }

        int i = (this.width - xSize) / 2;
        int j = (this.height - ySize) / 2;
        List<Map.Entry<ResourceLocation, ConstructionTableBlock.Tab>> entries = new ArrayList<>(ConstructionTableBlock.getTabs().entrySet());

        for (int k = 0; k < entries.size(); k++) {
            Map.Entry<ResourceLocation, ConstructionTableBlock.Tab> entry = entries.get(k);
            int x = (int) Math.floor(k / 5D);
            int y = k % 5;
            Button button = new FlatIconButton(i - 22 - x * 22, j + y * 22, entry.getValue().icon, (b) -> {
                mouseX = this.getMinecraft().mouseHelper.getMouseX();
                mouseY = this.getMinecraft().mouseHelper.getMouseY();
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new OpenConstructionTableTabMessage(entry.getKey()));
            });
            button.active = this.container.getType() != entry.getValue().containerType.get();
            this.addButton(button);
        }

        this.titleX = 28;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(this.texture);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);
    }

    public static class Helmet extends ConstructionTableScreen<HelmetCraftingContainer> {

        public Helmet(HelmetCraftingContainer container, PlayerInventory playerInventory, ITextComponent title) {
            super(container, playerInventory, title, HELMET_GUI_TEXTURES);
        }
    }

    public static class Chestplate extends ConstructionTableScreen<ChestplateCraftingContainer> {

        public Chestplate(ChestplateCraftingContainer container, PlayerInventory playerInventory, ITextComponent title) {
            super(container, playerInventory, title, CHESTPLATE_GUI_TEXTURES);
        }
    }

    public static class Leggings extends ConstructionTableScreen<LeggingsCraftingContainer> {

        public Leggings(LeggingsCraftingContainer container, PlayerInventory playerInventory, ITextComponent title) {
            super(container, playerInventory, title, LEGGINGS_GUI_TEXTURES);
        }
    }

    public static class Boots extends ConstructionTableScreen<BootsCraftingContainer> {

        public Boots(BootsCraftingContainer container, PlayerInventory playerInventory, ITextComponent title) {
            super(container, playerInventory, title, BOOTS_GUI_TEXTURES);
        }
    }

}
