package net.threetag.palladium.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.screen.components.EditButton;
import net.threetag.palladium.client.screen.components.FlatIconButton;
import net.threetag.palladium.network.ToggleAccessoryMessage;
import net.threetag.palladium.util.SupporterHandler;

import java.util.Collection;

public class AccessoryScreen extends OptionsSubScreen {

    public AccessoryScreen(Screen screen) {
        super(screen, null, new TranslatableComponent("gui.palladium.accessories"));
    }

    public AccessorySlot currentSlot;
    public AccessorySlotList slotList;
    public AccessoryList accessoryList;
    public RotationSlider rotationSlider;
    public float rotation = 180F;

    public static void addButton() {
        ClientGuiEvent.INIT_POST.register((screen, access) -> {
            Button button = null;
            Component text = new TranslatableComponent("gui.palladium.accessories");

            if (screen instanceof SkinCustomizationScreen) {
                button = new Button(screen.width / 2 - 100, screen.height / 6 + 24 * (12 >> 1), 200, 20, text,
                        b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen)));
            }

            if (screen instanceof InventoryScreen inv) {
                button = new EditButton(inv.leftPos + 63, inv.topPos + 66, b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen)), (b, poseStack, i, j) -> screen.renderTooltip(poseStack, text, i, j));
            }

            if (screen instanceof CreativeModeInventoryScreen inv) {
                button = new EditButton(inv.leftPos + 93, inv.topPos + 37, b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen)), (b, poseStack, i, j) -> screen.renderTooltip(poseStack, text, i, j)) {
                    @Override
                    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                        this.visible = inv.getSelectedTab() == 11;
                        super.render(poseStack, mouseX, mouseY, partialTick);
                    }
                };
            }

            if (button != null) {
                button.active = Minecraft.getInstance().player != null && !Accessory.getAvailableAccessories(SupporterHandler.getPlayerData(Minecraft.getInstance().player.getGameProfile().getId())).isEmpty();
                access.addRenderableWidget(button);
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new Button(30, this.height - 30, 100, 20, new TranslatableComponent("gui.done"), (button) -> this.onClose()));
        this.addRenderableWidget(this.rotationSlider = new RotationSlider(100 + (this.width - 150) / 2, this.height / 2 + this.height / 3 + 10, 100, 20, 0.5F));

        this.slotList = new AccessorySlotList(this.minecraft, this, 42, this.height, 20, this.height - 40, 36);
        this.slotList.setLeftPos(6);
        this.addWidget(slotList);

        this.accessoryList = new AccessoryList(this.minecraft, this, 150, this.height, 20, this.height - 40, this.font.lineHeight + 8);
        this.accessoryList.setLeftPos(48);
        this.addWidget(accessoryList);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        this.renderDirtBackground(0, 160);

        if (this.accessoryList != null)
            this.accessoryList.render(poseStack, mouseX, mouseY, partialTicks);

        if (this.slotList != null)
            this.slotList.render(poseStack, mouseX, mouseY, partialTicks);

        drawCenteredString(poseStack, this.font, this.title, 80, 7, 16777215);

        renderEntityInInventory(150 + (this.width - 150) / 2, this.height / 2 + this.height / 3, this.height / 3, (float) (this.rotation - 180F), 0F, 0F, this.minecraft.player);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void renderDirtBackground(int vOffset, int width) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, this.height, 0.0).uv(0.0F, (float) this.height / 32.0F + (float) vOffset).color(64, 64, 64, 255).endVertex();
        bufferBuilder.vertex(width, this.height, 0.0)
                .uv((float) width / 32.0F, (float) this.height / 32.0F + (float) vOffset)
                .color(64, 64, 64, 255)
                .endVertex();
        bufferBuilder.vertex(width, 0.0, 0.0).uv((float) width / 32.0F, (float) vOffset).color(64, 64, 64, 255).endVertex();
        bufferBuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, (float) vOffset).color(64, 64, 64, 255).endVertex();
        tesselator.end();
    }

    public static void renderEntityInInventory(int posX, int posY, int scale, float rotation, float mouseX, float mouseY, LivingEntity livingEntity) {
        float f = (float) Math.atan((double) (mouseX / 40.0F));
        float g = (float) Math.atan((double) (mouseY / 40.0F));
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate((double) posX, (double) posY, 1050.0);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0, 0.0, 1000.0);
        poseStack2.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(g * 20.0F);
        quaternion.mul(quaternion2);
        poseStack2.mulPose(quaternion);
        poseStack2.mulPose(Vector3f.XN.rotationDegrees(10));
        poseStack2.mulPose(Vector3f.YP.rotationDegrees(rotation));
        float h = livingEntity.yBodyRot;
        float i = livingEntity.getYRot();
        float j = livingEntity.getXRot();
        float k = livingEntity.yHeadRotO;
        float l = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + f * 20.0F;
        livingEntity.setYRot(180.0F + f * 40.0F);
        livingEntity.setXRot(-g * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack2, bufferSource, 15728880));
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = h;
        livingEntity.setYRot(i);
        livingEntity.setXRot(j);
        livingEntity.yHeadRotO = k;
        livingEntity.yHeadRot = l;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public static class AccessorySlotList extends AbstractSelectionList<SlotListEntry> {

        private final int listWidth;

        public AccessorySlotList(Minecraft minecraft, AccessoryScreen parent, int width, int height, int top, int bottom, int slotHeight) {
            super(minecraft, width, height, top, bottom, slotHeight);

            this.listWidth = width;
            for (AccessorySlot slot : AccessorySlot.getSlots()) {
                if (!Accessory.getAvailableAccessories(SupporterHandler.getPlayerData(minecraft.player.getUUID()), slot).isEmpty()) {
                    Accessory.getPlayerData(minecraft.player).ifPresent(data -> {
                        this.addEntry(new SlotListEntry(slot, parent, data.getSlots().containsKey(slot) && !data.getSlots().get(slot).isEmpty()));
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

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {

        }
    }

    public static class SlotListEntry extends AbstractSelectionList.Entry<SlotListEntry> {

        private final AccessorySlot slot;
        private final AccessoryScreen parent;
        private final boolean active;

        public SlotListEntry(AccessorySlot slot, AccessoryScreen parent, boolean active) {
            this.slot = slot;
            this.parent = parent;
            this.active = active;
        }

        @Override
        public void render(PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, FlatIconButton.WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            blit(poseStack, left, top + 2, 20, (this.parent.currentSlot == this.slot ? 64 : (isMouseOver ? 32 : 0)), 32, 32, 256, 256);
            if (this.slot.getIcon() != null) {
                RenderSystem.setShaderTexture(0, slot.getIcon());
                blit(poseStack, left, top + 2, 0, 0, 32, 32, 32, 32);
            } else {
                Font fontRenderer = this.parent.font;
                String s = this.slot.getDisplayName().getString().substring(0, 1);
                fontRenderer.drawShadow(poseStack, new TextComponent(s).getVisualOrderText(), left + 16 - fontRenderer.width(s) / 2F, top + 14, isMouseOver ? 16777120 : 0xfefefe);
            }

            if (isMouseOver) {
                this.parent.renderTooltip(poseStack, this.slot.getDisplayName(), mouseX, mouseY);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            this.parent.currentSlot = this.slot;
            this.parent.accessoryList.refreshList();
            this.parent.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }

    public static class AccessoryList extends AbstractSelectionList<AccessoryListEntry> {

        private final AccessoryScreen parent;
        private final int listWidth;

        public AccessoryList(Minecraft minecraft, AccessoryScreen parent, int width, int height, int top, int bottom, int slotHeight) {
            super(minecraft, width, height, top, bottom, slotHeight);
            this.listWidth = width;
            this.parent = parent;
            this.refreshList();
        }

        public void refreshList() {
            this.clearEntries();
            Collection<Accessory> accessories = Lists.newArrayList();

            if (this.parent.currentSlot != null) {
                Accessory.getPlayerData(minecraft.player).ifPresent(a -> {
                    if (a.getSlots().containsKey(parent.currentSlot)) {
                        accessories.addAll(a.getSlots().get(parent.currentSlot));
                    }
                });
                for (Accessory accessory : Accessory.REGISTRY) {
                    if (accessory.getPossibleSlots().contains(parent.currentSlot) && accessory.isAvailable(Minecraft.getInstance().player)) {
                        this.addEntry(new AccessoryListEntry(accessory, this.parent, accessories.contains(accessory)));
                    }
                }
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {

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

    public static class AccessoryListEntry extends AbstractSelectionList.Entry<AccessoryListEntry> {

        private final Accessory accessory;
        private final AccessoryScreen parent;
        private final boolean active;

        public AccessoryListEntry(Accessory accessory, AccessoryScreen parent, boolean active) {
            this.accessory = accessory;
            this.parent = parent;
            this.active = active;
        }

        @Override
        public void render(PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Font fontRenderer = this.parent.font;
            Component name = this.accessory.getDisplayName();

            if (Platform.isDevelopmentEnvironment() && !SupporterHandler.getPlayerData(Minecraft.getInstance().player.getUUID()).getAccessories().contains(this.accessory)) {
                name = name.copy().withStyle(ChatFormatting.STRIKETHROUGH);
            }

            fontRenderer.drawShadow(poseStack, fontRenderer.split(name, width - 25).get(0), left, top + 4, isMouseOver ? 16777120 : 0xfefefe);
            if (this.active) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, FlatIconButton.WIDGETS_LOCATION);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                blit(poseStack, left + width - 25, top + 2, 0, 120, 16, 16, 256, 256);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            new ToggleAccessoryMessage(this.parent.currentSlot, this.accessory).sendToServer();
            this.parent.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }

    public class RotationSlider extends AbstractSliderButton {

        public RotationSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, TextComponent.EMPTY, value);
        }

        @Override
        protected void updateMessage() {
            this.setMessage(TextComponent.EMPTY);
        }

        @Override
        protected void applyValue() {
            AccessoryScreen.this.rotation = (float) (this.value * 360F);
        }
    }
}
