package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.menu.MultiversalIteratorSuitStandMenu;
import net.threetag.palladium.multiverse.Universe;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import org.joml.Quaternionf;

public class MultiversalIteratorSuitStandScreen extends AbstractContainerScreen<MultiversalIteratorSuitStandMenu> implements ContainerListener {

    public static final ResourceLocation TEXTURE = Palladium.id("textures/gui/container/multiversal_iterator_suit_stand.png");
    public static final Quaternionf SUIT_STAND_ANGLE = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);

    private int tickCount = 0;

    public MultiversalIteratorSuitStandScreen(MultiversalIteratorSuitStandMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 214;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new CycleButton(this.leftPos + 51, this.topPos + 71, false, button -> this.menu.switchPage(false)));
        this.addRenderableWidget(new CycleButton(this.leftPos + 114, this.topPos + 71, true, button -> this.menu.switchPage(true)));
        this.addRenderableWidget(new ConfirmButton(this.leftPos + 136, this.topPos + 69, button -> this.menu.convert(this.menu.getSelectedPage().id())));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickCount++;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        var universe = MultiversalExtrapolatorItem.getUniverse(this.menu.getSlot(0).getItem(), this.minecraft.level);

        if (universe == null) {
            RandomSource random = RandomSource.create(this.tickCount / 2);
            String number = random.nextInt(10) + "" + random.nextInt(10) + random.nextInt(10);
            guiGraphics.drawString(this.minecraft.font, Universe.getGenericUniverseComponent(number), this.leftPos + 61, this.topPos + 24, 0xffffffff);
        } else {
            guiGraphics.drawString(this.minecraft.font, universe.getTitle(), this.leftPos + 61, this.topPos + 24, 0xffffffff);
        }

        if (this.menu.suitStand != null) {
            var cachedYRot = this.menu.suitStand.yBodyRot;
            var cachedXRot = this.menu.suitStand.getXRot();
            this.menu.suitStand.yBodyRot = 210.0F;
            this.menu.suitStand.setXRot(25F);
            this.menu.getSelectedPage().pretend(this.menu.suitStand,
                    armorStand -> InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + (this.imageWidth / 2), this.topPos + 105, 30, SUIT_STAND_ANGLE, null, armorStand));
            this.menu.suitStand.yBodyRot = cachedYRot;
            this.menu.suitStand.setXRot(cachedXRot);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
        if (dataSlotIndex == 0 && !stack.isEmpty() && MultiversalExtrapolatorItem.getUniverse(stack, this.minecraft.level) != null) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(PalladiumSoundEvents.MULTIVERSE_SEARCH.get(), 1.0F));
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {

    }

    private class CycleButton extends Button {

        private final boolean next;

        protected CycleButton(int x, int y, boolean next, OnPress onPress) {
            super(x, y, 11, 17, Component.empty(), onPress, DEFAULT_NARRATION);
            this.next = next;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.active = MultiversalIteratorSuitStandScreen.this.menu.getPages().size() > 1;

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.next ? 176 : 190, !this.active ? 36 : this.isHovered ? 18 : 0, this.getWidth(), this.getHeight());
        }
    }

    public class ConfirmButton extends Button {

        protected ConfirmButton(int x, int y, OnPress onPress) {
            super(x, y, 22, 22, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.active = MultiversalIteratorSuitStandScreen.this.menu.getSelectedPage().id() != 0;

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.active ? (this.isHovered ? 44 : 0) : 22, 214, this.getWidth(), this.getHeight());
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.active ? 66 : 88, 214, this.getWidth() - 1, this.getHeight());
        }
    }
}
