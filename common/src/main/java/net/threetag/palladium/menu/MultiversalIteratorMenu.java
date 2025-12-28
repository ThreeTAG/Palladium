package net.threetag.palladium.menu;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.multiverse.MultiversalItemVariantsManager;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import net.threetag.palladium.util.PlayerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiversalIteratorMenu extends ItemCombinerMenu {

    private final DataSlot resultCount = DataSlot.standalone();
    private List<Item> results;
    private int resultIndex = 0;

    public MultiversalIteratorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public MultiversalIteratorMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(PalladiumMenuTypes.MULTIVERSAL_ITERATOR.get(), containerId, playerInventory, access);
        this.addDataSlot(this.resultCount);
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 38, 20, arg -> arg.getItem() instanceof MultiversalExtrapolatorItem).withSlot(1, 48, 49, arg -> true).withResultSlot(2, 108, 49).build();
    }

    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return true;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());
        this.damageStackInSlot(0);
        this.shrinkStackInSlot(1);
        PlayerUtil.playSoundToAll(player.level(), player.getX(), player.getEyeY(), player.getZ(), 50, PalladiumSoundEvents.MULTIVERSE_SEARCH.get(), SoundSource.PLAYERS);
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    private void shrinkStackInSlot(int index) {
        ItemStack itemStack = this.inputSlots.getItem(index);
        if (!itemStack.isEmpty()) {
            itemStack.shrink(1);
            this.inputSlots.setItem(index, itemStack);
        }
    }

    private void damageStackInSlot(int index) {
        ItemStack itemStack = this.inputSlots.getItem(index);
        if (!itemStack.isEmpty()) {
            itemStack.hurtAndBreak(1, this.player, player1 -> this.inputSlots.setItem(index, ItemStack.EMPTY));
            this.inputSlots.setItem(index, itemStack);
        }
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(PalladiumBlocks.MULTIVERSAL_ITERATOR.get());
    }

    @Override
    public void createResult() {
        if (!this.inputSlots.getItem(0).isEmpty() && !this.inputSlots.getItem(1).isEmpty()) {
            var universe = MultiversalExtrapolatorItem.getUniverse(this.inputSlots.getItem(0), this.player.level());

            if (universe != null) {
                this.results = MultiversalItemVariantsManager.getInstance(this.player.level()).getVariantsOf(this.inputSlots.getItem(1).getItem(), universe);
                this.resultIndex = 0;

                if (!this.results.isEmpty()) {
                    var inputTag = this.inputSlots.getItem(1).getTag();
                    var resultStack = this.results.get(this.resultIndex).getDefaultInstance();
                    if (inputTag != null) {
                        resultStack.setTag(inputTag.copy());
                    }
                    this.resultSlots.setItem(0, resultStack);
                    this.resultCount.set(this.results.size());
                    return;
                }
            }
        }

        this.results = null;
        this.resultIndex = 0;
        this.resultCount.set(0);
        this.resultSlots.setItem(0, ItemStack.EMPTY);
    }

    public void cycleResult() {
        if (this.results != null && this.results.size() > 1) {
            this.resultIndex = this.resultIndex >= this.results.size() - 1 ? 0 : this.resultIndex + 1;
            this.resultSlots.setItem(0, this.results.get(this.resultIndex).getDefaultInstance());
        }
    }

    public int getResultCount() {
        return this.resultCount.get();
    }
}
