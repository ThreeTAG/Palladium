package net.threetag.palladium.compat.jei.tailoring;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.item.recipe.PalladiumRecipeSerializers;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.menu.PalladiumMenuTypes;
import net.threetag.palladium.menu.TailoringMenu;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.TailoringJeiTransferMessage;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TailoringTransferHandler implements IRecipeTransferHandler<TailoringMenu, TailoringRecipe> {

    private final IRecipeTransferHandlerHelper transferHelper;

    public TailoringTransferHandler(IRecipeTransferHandlerHelper transferHelper) {
        this.transferHelper = transferHelper;
    }

    @Override
    public Class<? extends TailoringMenu> getContainerClass() {
        return TailoringMenu.class;
    }

    @Override
    public Optional<MenuType<TailoringMenu>> getMenuType() {
        return Optional.of(PalladiumMenuTypes.TAILORING.get());
    }

    @Override
    public RecipeType<TailoringRecipe> getRecipeType() {
        return TailoringCategory.RECIPE_TYPE;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(TailoringMenu container, TailoringRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!transferHelper.recipeTransferHasServerSupport()) {
            return transferHelper.createUserErrorWithTooltip(Component.translatable("jei.tooltip.error.recipe.transfer.no.server"));
        }

        var level = player.level();
        var availableRecipes = level.getRecipeManager().getRecipesFor(PalladiumRecipeSerializers.TAILORING.get(), player.getInventory(), level);
        if (!availableRecipes.contains(recipe)) {
            return transferHelper.createUserErrorWithTooltip(Component.translatable("gui.palladium.jei.tailoring.recipe_unavailable"));
        }

        // inputSlotViews will only contain 1 IRecipeSlotView, the tool slot. The other inputs aren't added to the slot view, because they do not exist as a slot in JEI's recipe layout. See TailoringCategory#setRecipe
        var inputSlotViews = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);

        // Validate the slot view size, as a sanity check
        if (inputSlotViews.isEmpty()) {
            Palladium.LOGGER.error("Tailoring recipe transfer: Expected at least one input slot view");
            return transferHelper.createInternalError();
        }
        var toolSlotView = inputSlotViews.get(0);

        // Get item stacks from player and recipe's container
        var availableItemStacks = getAvailableItemStacks(container);

        // Check if there were any tool match in all inventories (player, recipe's container)
        Slot toolFoundSlot = null;
        if (!toolSlotView.isEmpty()) {
            // For each slot, check if the item matches the recipe's tool ingredient
            for (var availableSlotStack : availableItemStacks.entrySet()) {
                ItemStack availableStack = availableSlotStack.getValue();
                if (recipe.getToolIngredient().test(availableStack)) {
                    toolFoundSlot = availableSlotStack.getKey();
                }
            }
        }

        if (toolFoundSlot == null) {
            return transferHelper.createUserErrorWithTooltip(Component.translatable("jei.tooltip.error.recipe.transfer.missing"));
        }

        // If doTransfer is true
        if (doTransfer) {
            // source slot index, destination slot index, amount to transfer (in this case, always 1)
            PalladiumNetwork.NETWORK.sendToServer(new TailoringJeiTransferMessage(toolFoundSlot.index, 0, 1));

            TailoringScreen.setSelectRecipe(recipe);
        }

        return null;
    }

    // Get slots from the recipe's container
    private Map<Slot, ItemStack> getAvailableItemStacks(TailoringMenu container) {
        Map<Slot, ItemStack> availableItemStacks = new HashMap<>();

        // Add the tool slot stack
        {
            Slot toolSlot = container.getSlot(0);
            ItemStack toolStack = toolSlot.getItem();
            if (!toolStack.isEmpty()) {
                availableItemStacks.put(toolSlot, toolStack);
            }
        }

        // Add player inventory stacks
        {
            int playerInvStart = 1 + 4; // 1 Tool slot, 4 Output Slots.
            int playerInvSize = 36;
            for (int i = playerInvStart; i < playerInvStart + playerInvSize; i++) {
                Slot invSlot = container.getSlot(i);
                ItemStack playerStack = invSlot.getItem();
                if (!playerStack.isEmpty()) {
                    availableItemStacks.put(invSlot, playerStack);
                }
            }
        }

        return availableItemStacks;
    }
}
