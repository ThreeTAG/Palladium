package net.threetag.palladium.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.menu.MultiversalIteratorMenu;
import net.threetag.palladium.multiverse.Universe;
import net.threetag.palladium.network.CycleMultiversalIteratorResultMessage;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.sound.PalladiumSoundEvents;

public class MultiversalIteratorScreen extends ItemCombinerScreen<MultiversalIteratorMenu> {

    private static final ResourceLocation TEXTURE = Palladium.id("textures/gui/container/multiversal_iterator.png");
    private int tickCount = 0;
    private int resultAnimationTimer = 0;
    private CycleButton cycleButton;

    public MultiversalIteratorScreen(MultiversalIteratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    protected void subInit() {
        super.subInit();
        this.addRenderableWidget(this.cycleButton = new CycleButton(this.leftPos + 133, this.topPos + 53, button -> this.cycleResult()));
        this.cycleButton.active = false;
    }

    private void cycleResult() {
        PalladiumNetwork.NETWORK.sendToServer(new CycleMultiversalIteratorResultMessage());
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
        var universe = MultiversalExtrapolatorItem.getUniverse(this.menu.getSlot(0).getItem(), this.minecraft.level);

        if (universe == null) {
            RandomSource random = RandomSource.create(this.tickCount / 2);
            String number = random.nextInt(10) + "" + random.nextInt(10) + random.nextInt(10);
            guiGraphics.drawString(this.minecraft.font, Universe.getGenericUniverseComponent(number), this.leftPos + 61, this.topPos + 24, 0xffffffff);
        } else {
            guiGraphics.drawString(this.minecraft.font, universe.getTitle(), this.leftPos + 61, this.topPos + 24, 0xffffffff);
        }

        if (!this.menu.getSlot(2).getItem().isEmpty()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 74, this.topPos + 49, 176, 0, this.resultAnimationTimer, 17);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickCount++;

        if (this.menu.getSlot(2).getItem().isEmpty() || this.resultAnimationTimer >= 20) {
            this.resultAnimationTimer = 0;
        } else {
            this.resultAnimationTimer++;
        }

        if (this.cycleButton != null) {
            this.cycleButton.active = this.menu.getResultCount() > 1;
        }
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
        if (dataSlotIndex == 0 && !stack.isEmpty() && MultiversalExtrapolatorItem.getUniverse(stack, this.minecraft.level) != null) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(PalladiumSoundEvents.MULTIVERSE_SEARCH.get(), 1.0F));
        }
    }

    private static class CycleButton extends Button {

        protected CycleButton(int x, int y, OnPress onPress) {
            super(x, y, 8, 8, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (this.active) {
                guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.isHovered ? 184 : 176, 17, this.getWidth(), this.getHeight() + 1);
            }
        }
    }
}
