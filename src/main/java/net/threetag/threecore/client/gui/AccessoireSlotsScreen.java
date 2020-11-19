package net.threetag.threecore.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.capability.CapabilityAccessoires;

public class AccessoireSlotsScreen extends AbstractAccessoireScreen {

    public AccessoireSlotList slotList;

    public AccessoireSlotsScreen(Screen parentScreen) {
        super(parentScreen, new TranslationTextComponent("gui.threecore.accessoires"));
    }

    @Override
    protected void init() {
        super.init();

        this.slotList = new AccessoireSlotList(this.minecraft, this, 150, this.height, 20, this.height - 40, this.font.FONT_HEIGHT + 8);
        this.slotList.setLeftPos(6);
        this.children.add(slotList);
    }

    @Override
    public void renderSidebar(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.slotList != null)
            this.slotList.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public class AccessoireSlotList extends ExtendedList<SlotListEntry> {

        private final int listWidth;

        public AccessoireSlotList(Minecraft mcIn, AccessoireSlotsScreen parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
            super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
            this.listWidth = widthIn;
            for (AccessoireSlot slot : AccessoireSlot.getSlots()) {
                mcIn.player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
                    this.addEntry(new SlotListEntry(slot, parent, !accessoireHolder.getSlots().get(slot).isEmpty()));
                });
            }
        }

        @Override
        public int getRowWidth() {
            return this.listWidth;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.listWidth;
        }

    }

    public static class SlotListEntry extends ExtendedList.AbstractListEntry<AccessoireSlotsScreen.SlotListEntry> {

        private final AccessoireSlot slot;
        private final AccessoireSlotsScreen parent;
        private final boolean active;

        public SlotListEntry(AccessoireSlot slot, AccessoireSlotsScreen parent, boolean active) {
            this.slot = slot;
            this.parent = parent;
            this.active = active;
        }

        @Override
        public void render(MatrixStack matrixStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            RenderSystem.pushMatrix();
            FontRenderer fontRenderer = this.parent.font;
            fontRenderer.func_238407_a_(matrixStack, fontRenderer.trimStringToWidth(this.slot.getDisplayName(), entryWidth - 25).get(0), left, top + 4, isMouseOver ? 16777120 : 0xfefefe);
            if (this.active) {
                TICK_ICON.draw(this.parent.minecraft, matrixStack, left + entryWidth - 25, top + 2);
            }
            RenderSystem.popMatrix();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            this.parent.minecraft.displayGuiScreen(new AccessoireScreen(this.parent, this.slot));
            this.parent.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }
}
