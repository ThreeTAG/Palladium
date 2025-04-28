package net.threetag.palladium.client.gui.screen.accessory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.grid.AbstractSelectionGrid;

import java.util.Objects;

public class AccessoryGrid extends AbstractSelectionGrid<AccessoryGrid.Entry> {

    private final AccessorySlot slot;
    private final PoseStackTransformation preview;

    public AccessoryGrid(ScreenRectangle rectangle, AccessorySlot slot, Minecraft minecraft) {
        super(minecraft, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), 50, 50, 4);
        this.slot = slot;
        this.preview = slot.preview().invertYRot();

        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.LEATHER_HELMET));
        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.IRON_HELMET));
        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.GOLDEN_HELMET));
        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.DIAMOND_HELMET));
        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.NETHERITE_HELMET));
        this.addEntry(new Entry(EquipmentSlot.HEAD, Items.TURTLE_HELMET));
        this.addEntry(new Entry(EquipmentSlot.CHEST, Items.LEATHER_CHESTPLATE));
        this.addEntry(new Entry(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE));
        this.addEntry(new Entry(EquipmentSlot.CHEST, Items.GOLDEN_CHESTPLATE));
        this.addEntry(new Entry(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE));
        this.addEntry(new Entry(EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE));
        this.addEntry(new Entry(EquipmentSlot.LEGS, Items.LEATHER_LEGGINGS));
        this.addEntry(new Entry(EquipmentSlot.LEGS, Items.IRON_LEGGINGS));
        this.addEntry(new Entry(EquipmentSlot.LEGS, Items.GOLDEN_LEGGINGS));
        this.addEntry(new Entry(EquipmentSlot.LEGS, Items.DIAMOND_LEGGINGS));
        this.addEntry(new Entry(EquipmentSlot.LEGS, Items.NETHERITE_LEGGINGS));
        this.addEntry(new Entry(EquipmentSlot.FEET, Items.LEATHER_BOOTS));
        this.addEntry(new Entry(EquipmentSlot.FEET, Items.IRON_BOOTS));
        this.addEntry(new Entry(EquipmentSlot.FEET, Items.GOLDEN_BOOTS));
        this.addEntry(new Entry(EquipmentSlot.FEET, Items.DIAMOND_BOOTS));
        this.addEntry(new Entry(EquipmentSlot.FEET, Items.NETHERITE_BOOTS));
    }

    @Override
    public void updateSizeAndPosition(int width, int height, int x, int y) {
        super.updateSizeAndPosition(width / 3 * 2, height, x, y);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public class Entry extends AbstractSelectionGrid.Entry<Entry> {

        private static final WidgetSprites SPRITES = new WidgetSprites(
                Palladium.id("widget/accessory"),
                Palladium.id("widget/accessory_disabled"),
                Palladium.id("widget/accessory_highlighted")
        );

        private final EquipmentSlot slot;
        private final Item item;

        public Entry(EquipmentSlot slot, Item item) {
            this.slot = slot;
            this.item = item;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.blitSprite(
                    RenderType::guiTextured,
                    SPRITES.get(true, hovering),
                    left,
                    top,
                    width,
                    height,
                    0xFFFFFFFF
            );

            var player = Minecraft.getInstance().player;
            var originalItem = Objects.requireNonNull(player).getItemBySlot(this.slot);
            player.setItemSlot(this.slot, this.item.getDefaultInstance());
            guiGraphics.enableScissor(left, top, left + width, top + height);
            AccessoryScreen.renderEntityInInventory(guiGraphics, left + width / 2F, top + height  / 2F, 20, AccessoryGrid.this.preview, Minecraft.getInstance().player);
            guiGraphics.disableScissor();
            player.setItemSlot(this.slot, originalItem);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (CommonInputs.selected(keyCode)) {
                this.select();
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.select();
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void select() {
            AccessoryGrid.this.setSelected(this);
            AccessoryGrid.this.minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
        }
    }

}
