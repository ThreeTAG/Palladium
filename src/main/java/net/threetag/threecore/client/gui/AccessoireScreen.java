package net.threetag.threecore.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.network.ToggleAccessoireMessage;

import java.util.Collection;

public class AccessoireScreen extends AbstractAccessoireScreen {

    public final AccessoireSlot slot;
    public AccessoireList accessoireList;

    public AccessoireScreen(Screen parentScreen, AccessoireSlot slot) {
        super(parentScreen, slot.getDisplayName());
        this.slot = slot;
    }

    @Override
    protected void init() {
        super.init();

        this.accessoireList = new AccessoireList(this.minecraft, this, 150, this.height, 20, this.height - 40, this.font.FONT_HEIGHT + 8);
        this.accessoireList.setLeftPos(6);
        this.children.add(accessoireList);
    }

    @Override
    public void renderSidebar(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.accessoireList != null)
            this.accessoireList.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public class AccessoireList extends ExtendedList<AccessoireListEntry> {

        private AccessoireScreen parent;
        private int listWidth;

        public AccessoireList(Minecraft mcIn, AccessoireScreen parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
            super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
            this.parent = parent;
            this.listWidth = widthIn;
            this.refreshList();
        }

        public void refreshList() {
            this.clearEntries();
            Collection<Accessoire> accessoires = Lists.newArrayList();
            this.minecraft.player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(a -> {
                accessoires.addAll(a.getSlots().get(parent.slot));
            });
            for (Accessoire accessoire : Accessoire.REGISTRY.getValues()) {
                if (accessoire.getPossibleSlots().contains(parent.slot) && accessoire.isAvailable(Minecraft.getInstance().player)) {
                    this.addEntry(new AccessoireListEntry(accessoire, this.parent, accessoires.contains(accessoire)));
                }
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

    public static class AccessoireListEntry extends ExtendedList.AbstractListEntry<AccessoireListEntry> {

        private final Accessoire accessoire;
        private final AccessoireScreen parent;
        private final boolean active;

        public AccessoireListEntry(Accessoire accessoire, AccessoireScreen parent, boolean active) {
            this.accessoire = accessoire;
            this.parent = parent;
            this.active = active;
        }

        @Override
        public void render(MatrixStack matrixStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            RenderSystem.pushMatrix();
            FontRenderer fontRenderer = this.parent.font;
            fontRenderer.func_238407_a_(matrixStack, fontRenderer.trimStringToWidth(this.accessoire.getDisplayName(), entryWidth - 25).get(0), left, top + 4, isMouseOver ? 16777120 : 0xfefefe);
            if (this.active) {
                TICK_ICON.draw(this.parent.minecraft, matrixStack, left + entryWidth - 25, top + 2);
            }
            RenderSystem.popMatrix();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new ToggleAccessoireMessage(this.parent.slot, this.accessoire));
            this.parent.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }

}
