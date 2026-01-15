package net.threetag.palladium.menu.slot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class TailoringResultSlot extends Slot {

    private final Player player;
    private final ArmorType armorType;
    private int removeCount;

    public TailoringResultSlot(Container container, Player player, ArmorType armorType, int slot, int x, int y) {
        super(container, slot, x, y);
        this.player = player;
        this.armorType = armorType;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return switch (this.armorType) {
            case HELMET -> InventoryMenu.EMPTY_ARMOR_SLOT_HELMET;
            case CHESTPLATE, BODY -> InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE;
            case LEGGINGS -> InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS;
            case BOOTS -> InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS;
        };
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount = this.removeCount + Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        this.removeCount += numItemsCrafted;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.removeCount > 0) {
            stack.onCraftedBy(this.player, this.removeCount);
            EventHooks.firePlayerCraftingEvent(this.player, stack, this.container);
        }

        if (this.container instanceof RecipeCraftingHolder recipecraftingholder) {
            recipecraftingholder.awardUsedRecipes(this.player, Collections.emptyList());
        }

        this.removeCount = 0;
    }
}
