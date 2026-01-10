package net.threetag.palladium.menu;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.recipe.PalladiumRecipePropertySets;
import net.threetag.palladium.item.recipe.PalladiumRecipeTypes;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.menu.container.TailoringResultContainer;
import net.threetag.palladium.menu.slot.TailoringResultSlot;
import net.threetag.palladium.menu.slot.TailoringToolSlot;
import net.threetag.palladium.network.SyncAvailableTailoringRecipesPacket;
import net.threetag.palladium.network.TailoringCraftPacket;
import net.threetag.palladium.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TailoringMenu extends AbstractContainerMenu {

    private final SimpleContainer toolSlotContainer = new SimpleContainer(1);
    private TailoringToolSlot toolSlot;
    private final TailoringResultContainer resultSlots = new TailoringResultContainer();
    private final ContainerLevelAccess access;
    public final Player player;
    public final Inventory playerInventory;
    private List<ResourceKey<Recipe<?>>> availableRecipes = new ArrayList<>();

    public TailoringMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public TailoringMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(PalladiumMenuTypes.TAILORING.get(), containerId);
        this.player = playerInventory.player;
        this.playerInventory = playerInventory;
        this.access = access;

        this.addSlot(this.toolSlot = new TailoringToolSlot(this.toolSlotContainer, 0, 85, 75));

        for (int i = 0; i < 4; i++) {
            this.addSlot(new TailoringResultSlot(this.resultSlots, playerInventory.player, ArmorType.values()[i], i, 208, 19 + (i * 18)));
        }

        this.addStandardInventorySlots(playerInventory, 36, 107);

        access.execute((level, blockPos) -> {
            if (playerInventory.player instanceof ServerPlayer serverPlayer) {
                this.availableRecipes = serverPlayer.level().recipeAccess().getRecipes().stream()
                        .filter(recipeHolder -> recipeHolder.value().getType() == PalladiumRecipeTypes.TAILORING.get() && ((TailoringRecipe) recipeHolder.value()).isAvailable(serverPlayer))
                        .map(RecipeHolder::id)
                        .collect(Collectors.toList());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncAvailableTailoringRecipesPacket(this.availableRecipes));
            }
        });
    }

    public boolean canCraft(TailoringRecipe recipe) {
        if (this.player instanceof ServerPlayer && !recipe.isAvailable(this.player)) {
            return false;
        }

        for (SizedIngredient sizedIngredient : recipe.getIngredients()) {
            if (!RecipeUtil.checkSizedIngredientInContainer(sizedIngredient, this.playerInventory)) {
                return false;
            }
        }

        return recipe.toolIngredient().test(this.toolSlotContainer.getItem(0)) && this.resultSlots.isEmpty();
    }

    public void craft(ResourceKey<Recipe<?>> recipeKey) {
        if (recipeKey != null) {
            if (this.player instanceof ServerPlayer serverPlayer) {
                var r = serverPlayer.level().recipeAccess().byKey(recipeKey).orElseThrow().value();

                if (r instanceof TailoringRecipe recipe && canCraft(recipe)) {
                    for (SizedIngredient sizedIngredient : recipe.getIngredients()) {
                        RecipeUtil.consumeSizedIngredientFromContainer(sizedIngredient, this.playerInventory);
                    }

                    var tool = this.toolSlotContainer.getItem(0);
                    tool.hurtAndBreak((int) recipe.getResults().values().stream().filter(s -> !s.isEmpty()).count(), serverPlayer.level(), serverPlayer, pl -> this.toolSlotContainer.clearContent());

                    for (int i = 0; i < 4; i++) {
                        var slot = ArmorType.values()[i];
                        this.resultSlots.setItem(i, recipe.getResults().getOrDefault(slot, ItemStack.EMPTY).copy());
                    }
                }
            } else {
                Palladium.PROXY.sendPacketToServer(new TailoringCraftPacket(recipeKey));
            }
        }
    }

    public TailoringToolSlot getToolSlot() {
        return this.toolSlot;
    }

    public boolean isValidTool(ItemStack stack) {
        return this.playerInventory.player.level().recipeAccess().propertySet(PalladiumRecipePropertySets.TAILORING_TOOL).test(stack);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
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
            this.clearContainer(player, this.toolSlotContainer);
            this.clearContainer(player, this.resultSlots);
        });
    }
}
