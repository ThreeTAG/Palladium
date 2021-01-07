package net.threetag.threecore.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.client.gui.widget.FlatIconButton;
import net.threetag.threecore.network.ToggleAccessoireMessage;
import net.threetag.threecore.util.SupporterHandler;
import net.threetag.threecore.util.icon.TexturedIcon;

import java.util.Collection;

public class AccessoireScreen extends SettingsScreen {

    public static final TexturedIcon TICK_ICON = new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 160, 16, 16, 16);

    public AccessoireSlot currentSlot;
    public AccessoireSlotList slotList;
    public AccessoireList accessoireList;
    public RotationSlider rotationSlider;

    public AccessoireScreen(Screen parentScreen) {
        super(parentScreen, null, new TranslationTextComponent("gui.threecore.accessoires"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new ExtendedButton(30, this.height - 30, 100, 18, new TranslationTextComponent("gui.done"), (button) -> this.closeScreen()));
        this.addButton(this.rotationSlider = new RotationSlider(100 + (this.width - 150) / 2, this.height / 2 + this.height / 3 + 10, 100, 10, 0, 360, 180));

        this.slotList = new AccessoireSlotList(this.minecraft, this, 42, this.height, 20, this.height - 40, 36);
        this.slotList.setLeftPos(6);
        this.children.add(slotList);

        this.accessoireList = new AccessoireList(this.minecraft, this, 150, this.height, 20, this.height - 40, this.font.FONT_HEIGHT + 8);
        this.accessoireList.setLeftPos(48);
        this.children.add(accessoireList);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderDirtBackground(160, 0);

        if (this.accessoireList != null)
            this.accessoireList.render(matrixStack, mouseX, mouseY, partialTicks);

        if (this.slotList != null)
            this.slotList.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, this.title, 80, 7, 16777215);

        drawEntityOnScreen(150 + (this.width - 150) / 2, this.height / 2 + this.height / 3, this.height / 3, (float) (this.rotationSlider.getValue() - 180F), 0F, 0F, this.minecraft.player);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderDirtBackground(int width, int offset) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, this.height, 0.0D).tex(0.0F, (float) this.height / 32.0F + (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, this.height, 0.0D).tex((float) width / 32.0F, (float) this.height / 32.0F + (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, 0.0D, 0.0D).tex((float) width / 32.0F, (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0F, (float) offset).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public class AccessoireSlotList extends ExtendedList<SlotListEntry> {

        private final int listWidth;

        public AccessoireSlotList(Minecraft mcIn, AccessoireScreen parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
            super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
            this.listWidth = widthIn;
            for (AccessoireSlot slot : AccessoireSlot.getSlots()) {
                if(!Accessoire.getAvailableAccessoires(SupporterHandler.getPlayerData(mcIn.player.getGameProfile().getId()), slot).isEmpty()) {
                    mcIn.player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
                        this.addEntry(new SlotListEntry(slot, parent, !accessoireHolder.getSlots().get(slot).isEmpty()));
                    });
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

    public static class SlotListEntry extends ExtendedList.AbstractListEntry<SlotListEntry> {

        private final AccessoireSlot slot;
        private final AccessoireScreen parent;
        private final boolean active;

        public SlotListEntry(AccessoireSlot slot, AccessoireScreen parent, boolean active) {
            this.slot = slot;
            this.parent = parent;
            this.active = active;
        }

        @Override
        public void render(MatrixStack matrixStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            RenderSystem.pushMatrix();
            this.parent.minecraft.getTextureManager().bindTexture(FlatIconButton.WIDGETS_LOCATION);
            blit(matrixStack, left, top + 2, 20, (this.parent.currentSlot == this.slot ? 64 : (isMouseOver ? 32 : 0)), 32, 32, 256, 256);
            if (this.slot.getIcon() != null) {
                this.parent.minecraft.getTextureManager().bindTexture(slot.getIcon());
                blit(matrixStack, left, top + 2, 0,0, 32, 32, 32, 32);
            } else {
                FontRenderer fontRenderer = this.parent.font;
                String s = this.slot.getDisplayName().getString().substring(0, 1);
                fontRenderer.func_238407_a_(matrixStack, new StringTextComponent(s).func_241878_f(), left + 16 - fontRenderer.getStringWidth(s) / 2F, top + 14, isMouseOver ? 16777120 : 0xfefefe);
            }

            if(isMouseOver) {
                this.parent.renderTooltip(matrixStack, this.slot.getDisplayName(), mouseX, mouseY);
            }

            RenderSystem.popMatrix();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            this.parent.currentSlot = this.slot;
            this.parent.accessoireList.refreshList();
            this.parent.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
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

            if (this.parent.currentSlot != null) {
                this.minecraft.player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(a -> {
                    accessoires.addAll(a.getSlots().get(parent.currentSlot));
                });
                for (Accessoire accessoire : Accessoire.REGISTRY.getValues()) {
                    if (accessoire.getPossibleSlots().contains(parent.currentSlot) && accessoire.isAvailable(Minecraft.getInstance().player)) {
                        this.addEntry(new AccessoireListEntry(accessoire, this.parent, accessoires.contains(accessoire)));
                    }
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
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new ToggleAccessoireMessage(this.parent.currentSlot, this.accessoire));
            this.parent.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }

    public static class RotationSlider extends Slider {

        public RotationSlider(int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal) {
            super(xPos, yPos, width, height, StringTextComponent.EMPTY, StringTextComponent.EMPTY, minVal, maxVal, currentVal, false, false, null, slider -> {
            });
        }
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float rotation, float mouseX, float mouseY, LivingEntity p_228187_5_) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.XN.rotationDegrees(10));
        matrixstack.rotate(Vector3f.YP.rotationDegrees(rotation));
        float f2 = p_228187_5_.renderYawOffset;
        float f3 = p_228187_5_.rotationYaw;
        float f4 = p_228187_5_.rotationPitch;
        float f5 = p_228187_5_.prevRotationYawHead;
        float f6 = p_228187_5_.rotationYawHead;
        p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
        p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
        p_228187_5_.rotationPitch = -f1 * 20.0F;
        p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
        p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.renderYawOffset = f2;
        p_228187_5_.rotationYaw = f3;
        p_228187_5_.rotationPitch = f4;
        p_228187_5_.prevRotationYawHead = f5;
        p_228187_5_.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }

}
