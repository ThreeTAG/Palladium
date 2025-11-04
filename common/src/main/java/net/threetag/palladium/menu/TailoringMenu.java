package net.threetag.palladium.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.recipe.PalladiumRecipeSerializers;
import net.threetag.palladium.item.recipe.SizedIngredient;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncAvailableTailoringRecipes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TailoringMenu extends AbstractContainerMenu {

    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final ResourceLocation EMPTY_TOOL_SLOT = Palladium.id("item/empty_tailoring_tool_slot");

    private final SimpleContainer toolSlot = new SimpleContainer(1);
    private final TailoringResultContainer resultSlots = new TailoringResultContainer();
    private final ContainerLevelAccess access;
    public final Inventory playerInventory;
    private List<TailoringRecipe> availableRecipes = new ArrayList<>();

    public TailoringMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public TailoringMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(PalladiumMenuTypes.TAILORING.get(), containerId);
        this.access = access;
        this.playerInventory = playerInventory;

        this.addSlot(new Slot(this.toolSlot, 0, 85, 75) {
            @Override
            public @NotNull Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_TOOL_SLOT);
            }
        });

        for (int i = 0; i < 4; i++) {
            this.addSlot(new TailoringResultSlot(this.resultSlots, playerInventory.player, SLOT_IDS[i], i, 208, 19 + (i * 18)));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 36 + j * 18, 107 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 36 + i * 18, 165));
        }

        access.execute((level, blockPos) -> {
            if (playerInventory.player instanceof ServerPlayer serverPlayer) {
                this.availableRecipes = level.getRecipeManager().getRecipesFor(PalladiumRecipeSerializers.TAILORING.get(), this.playerInventory, level);
                PalladiumNetwork.NETWORK.sendToPlayer(serverPlayer, new SyncAvailableTailoringRecipes(this.availableRecipes.stream().map(TailoringRecipe::getId).toList()));
            }
        });
    }

    public boolean canCraft(Player player, TailoringRecipe recipe) {
        if (player instanceof ServerPlayer serverPlayer && !serverPlayer.getRecipeBook().contains(recipe)) {
            return false;
        }

        for (SizedIngredient sizedIngredient : recipe.getSizedIngredients()) {
            if (!sizedIngredient.test(player.getInventory())) {
                return false;
            }
        }

        return recipe.getToolIngredient().test(this.toolSlot.getItem(0)) && this.resultSlots.isEmpty();
    }

    public void craft(Player player, TailoringRecipe recipe) {
        if (canCraft(player, recipe)) {
            for (SizedIngredient sizedIngredient : recipe.getSizedIngredients()) {
                sizedIngredient.take(player.getInventory());
            }

            if (player instanceof ServerPlayer serverPlayer) {
                var tool = this.toolSlot.getItem(0);
                tool.hurtAndBreak((int) recipe.getResults().values().stream().filter(s -> !s.isEmpty()).count(), serverPlayer, pl -> this.toolSlot.clearContent());
            }

            for (int i = 0; i < 4; i++) {
                var slot = SLOT_IDS[i];
                this.resultSlots.setItem(i, recipe.getResults().get(slot).copy());
            }
        }
    }

    public boolean isValidTool(ItemStack stack) {
        return this.playerInventory.player.level().getRecipeManager().getAllRecipesFor(PalladiumRecipeSerializers.TAILORING.get()).stream().anyMatch(r -> r.getToolIngredient().test(stack));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index >= 1 && index <= 4) {
                if (!this.moveItemStackTo(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index != 0) {
                if (this.isValidTool(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 5 && index < 32) {
                    if (!this.moveItemStackTo(itemStack2, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 32 && index < 41 && !this.moveItemStackTo(itemStack2, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 5, 41, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, PalladiumBlocks.TAILORING_BENCH.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.toolSlot);
            this.clearContainer(player, this.resultSlots);
        });
    }
}
