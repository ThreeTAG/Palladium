package net.threetag.palladium.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.screen.components.EditButton;
import net.threetag.palladium.client.screen.components.FlatIconButton;
import net.threetag.palladium.condition.InAccessorySlotMenuCondition;
import net.threetag.palladium.network.ToggleAccessoryMessage;
import net.threetag.palladium.util.SupporterHandler;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladiumcore.event.ScreenEvents;
import net.threetag.palladiumcore.util.Platform;
import org.joml.Quaternionf;

import java.util.Collection;
import java.util.Objects;

public class AccessoryScreen extends OptionsSubScreen {

    public AccessoryScreen(Screen screen) {
        super(screen, null, Component.translatable("gui.palladium.accessories"));
    }

    public AccessorySlot currentSlot;
    public AccessorySlotList slotList;
    public AccessoryList accessoryList;
    public RotationSlider rotationSlider;
    public float rotation = 180F;

    public static void addButton() {
        ScreenEvents.INIT_POST.register((screen) -> {
            Button button = null;
            Component text = Component.translatable("gui.palladium.accessories");

            if (screen instanceof SkinCustomizationScreen) {
                button = Button.builder(text, b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen))).bounds(screen.width / 2 - 100, screen.height / 6 + 24 * (12 >> 1), 200, 20).build();
            }

            if (screen instanceof InventoryScreen inv) {
                button = new EditButton(inv.leftPos + 63, inv.topPos + 66, b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen)));
                button.setTooltip(Tooltip.create(text));
            }

            if (screen instanceof CreativeModeInventoryScreen inv) {
                button = new EditButton(inv.leftPos + 93, inv.topPos + 37, b -> Minecraft.getInstance().setScreen(new AccessoryScreen(screen))) {
                    @Override
                    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        this.visible = CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INVENTORY);
                        super.render(guiGraphics, mouseX, mouseY, partialTick);
                    }
                };
                button.setTooltip(Tooltip.create(text));
            }

            if (button != null) {
                button.active = Minecraft.getInstance().player != null && !Accessory.getAvailableAccessories(SupporterHandler.getPlayerData(Minecraft.getInstance().player.getGameProfile().getId())).isEmpty();
                screen.addRenderableWidget(button);
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

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), (button) -> this.onClose()).bounds(30, this.height - 30, 100, 20).build());
        this.addRenderableWidget(this.rotationSlider = new RotationSlider(100 + (this.width - 150) / 2, this.height / 2 + this.height / 3 + 10, 100, 20, 0.5F));

        this.slotList = new AccessorySlotList(this.minecraft, this, 42, this.height, 20, this.height - 40, 36);
        this.slotList.setLeftPos(6);
        this.addWidget(slotList);

        this.accessoryList = new AccessoryList(this.minecraft, this, 150, this.height, 20, this.height - 40, this.font.lineHeight + 8);
        this.accessoryList.setLeftPos(48);
        this.addWidget(accessoryList);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        this.renderDirtBackground(0, 160);

        if (this.accessoryList != null)
            this.accessoryList.render(guiGraphics, mouseX, mouseY, partialTicks);

        if (this.slotList != null)
            this.slotList.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(this.font, this.title, 80, 7, 16777215);

        InAccessorySlotMenuCondition.CURRENT_SLOT = this.currentSlot;
        Quaternionf quaternionf = (new Quaternionf()).rotateX((float) Math.toRadians(180F)).rotateY((float) Math.toRadians(this.rotation));
        var player = Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player);
        float h = player.yBodyRot;
        float i = player.getYRot();
        float j = player.getXRot();
        float k = player.yHeadRotO;
        float l = player.yHeadRot;
        player.yBodyRot = 180.0F;
        player.setYRot(180.0F);
        player.setXRot(0);
        player.yHeadRot = player.getYRot();
        player.yHeadRotO = player.getYRot();
        InventoryScreen.renderEntityInInventory(guiGraphics, 150 + (this.width - 150) / 2, this.height / 2 + this.height / 3, this.height / 3, quaternionf, null, player);
        player.yBodyRot = h;
        player.setYRot(i);
        player.setXRot(j);
        player.yHeadRotO = k;
        player.yHeadRot = l;
        InAccessorySlotMenuCondition.CURRENT_SLOT = null;

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
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

    public static class AccessorySlotList extends AbstractSelectionList<SlotListEntry> {

        private final int listWidth;

        public AccessorySlotList(Minecraft minecraft, AccessoryScreen parent, int width, int height, int top, int bottom, int slotHeight) {
            super(minecraft, width, height, top, bottom, slotHeight);

            this.listWidth = width;
            var context = DataContext.forEntity(minecraft.player);
            for (AccessorySlot slot : AccessorySlot.getSlots()) {
                if (!Accessory.getAvailableAccessories(SupporterHandler.getPlayerData(minecraft.player.getUUID()), slot).isEmpty() && slot.isVisible(context)) {
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
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.blit(FlatIconButton.WIDGETS_LOCATION, left, top + 2, 20, (this.parent.currentSlot == this.slot ? 64 : (isMouseOver ? 32 : 0)), 32, 32, 256, 256);
            Font fontRenderer = this.parent.font;
            if (this.slot.getIcon() != null) {
                guiGraphics.blit(this.slot.getIcon(), left, top + 2, 0, 0, 32, 32, 32, 32);
            } else {
                String s = this.slot.getDisplayName().getString().substring(0, 1);
                guiGraphics.drawString(fontRenderer, Component.literal(s).getVisualOrderText(), (int) (left + 16 - fontRenderer.width(s) / 2F), top + 14, isMouseOver ? 16777120 : 0xfefefe);
            }

            if (isMouseOver) {
                guiGraphics.renderTooltip(fontRenderer, this.slot.getDisplayName(), mouseX, mouseY);
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
                for (Accessory accessory : Accessory.REGISTRY.getValues()) {
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
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Font fontRenderer = this.parent.font;
            Component name = this.accessory.getDisplayName();

            if (!Platform.isProduction() && !SupporterHandler.getPlayerData(Minecraft.getInstance().player.getUUID()).getAccessories().contains(this.accessory)) {
                name = name.copy().withStyle(ChatFormatting.STRIKETHROUGH);
            }

            guiGraphics.drawString(fontRenderer, fontRenderer.split(name, width - 25).get(0), left, top + 4, isMouseOver ? 16777120 : 0xfefefe);
            if (this.active) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(FlatIconButton.WIDGETS_LOCATION, left + width - 25, top + 2, 0, 120, 16, 16, 256, 256);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) {
            new ToggleAccessoryMessage(this.parent.currentSlot, this.accessory).send();
            this.parent.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
    }

    public class RotationSlider extends AbstractSliderButton {

        public RotationSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.empty(), value);
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.empty());
        }

        @Override
        protected void applyValue() {
            AccessoryScreen.this.rotation = (float) (this.value * 360F);
        }
    }
}
