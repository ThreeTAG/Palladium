package net.threetag.threecore.base.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.block.MachineBlock;
import net.threetag.threecore.base.inventory.GrinderContainer;
import net.threetag.threecore.base.recipe.GrinderRecipe;
import net.threetag.threecore.util.energy.EnergyStorageExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GrinderTileEntity extends MachineTileEntity {

    private EnergyStorageExt energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.GRINDER);
    public int progress;
    public int progressMax;
    private final Map<ResourceLocation, Integer> field_214022_n = Maps.newHashMap();

    protected final IIntArray intArray = new IIntArray() {
        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return progress;
                case 1:
                    return progressMax;
                case 2:
                    return energyStorage.getEnergyStored();
                case 3:
                    return energyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int i, int value) {
            switch (i) {
                case 0:
                    progress = value;
                case 1:
                    progressMax = value;
                case 2:
                    energyStorage.setEnergyStored(value);
                case 3:
                    energyStorage.setMaxEnergyStored(value);
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    private ItemStackHandler energySlot = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot) {
            GrinderTileEntity.this.markDirty();
        }
    };
    private ItemStackHandler inputSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            GrinderTileEntity.this.markDirty();
        }
    };
    private ItemStackHandler outputSlots = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            GrinderTileEntity.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };
    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, inputSlot, outputSlots);
    private RecipeWrapper recipeWrapper = new RecipeWrapper(this.combinedHandler);

    public GrinderTileEntity() {
        super(ThreeCoreBase.GRINDER_TILE_ENTITY);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.grinder");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new GrinderContainer(id, playerInventory, this, this.intArray);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        this.progress = nbt.getInt("Progress");
        this.progressMax = nbt.getInt("ProgressMax");
        this.energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.GRINDER, nbt.getInt("Energy"));

        if (nbt.contains("EnergySlots"))
            this.energySlot.deserializeNBT(nbt.getCompound("EnergySlots"));
        if (nbt.contains("InputSlots"))
            this.inputSlot.deserializeNBT(nbt.getCompound("InputSlots"));
        if (nbt.contains("OutputSlots"))
            this.outputSlots.deserializeNBT(nbt.getCompound("OutputSlots"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);

        nbt.putInt("Progress", this.progress);
        nbt.putInt("ProgressMax", this.progressMax);
        nbt.putInt("Energy", this.energyStorage.getEnergyStored());
        nbt.put("EnergySlots", this.energySlot.serializeNBT());
        nbt.put("InputSlots", this.inputSlot.serializeNBT());
        nbt.put("OutputSlots", this.outputSlots.serializeNBT());

        return nbt;
    }

    @Override
    public void tick() {
        boolean working = this.isWorking();
        boolean dirty = false;

        if (this.world != null && !this.world.isRemote) {
            this.energySlot.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> {
                int energy = e.extractEnergy(e.getEnergyStored(), true);
                energy = energyStorage.receiveEnergy(energy, true);
                if (energy > 0) {
                    this.energyStorage.receiveEnergy(energy, false);
                    e.extractEnergy(energy, false);
                }
            });

            ItemStack input = this.inputSlot.getStackInSlot(0);
            if (!input.isEmpty()) {
                GrinderRecipe recipe = this.world.getRecipeManager().getRecipe(GrinderRecipe.RECIPE_TYPE, this.recipeWrapper, this.world).orElse(null);
                if (canWork(recipe)) {
                    this.progressMax = recipe.getEnergy();
                    if (progress >= progressMax) {
                        produceOutput(recipe);
                        progress = 0;
                        dirty = true;
                    } else {
                        progress++;
                        this.energyStorage.extractEnergy(1, false);
                    }
                } else {
                    progress = 0;
                }
            } else {
                progress = 0;
            }
        }

        if (working != this.isWorking()) {
            dirty = true;
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MachineBlock.LIT, this.isWorking()), 3);
        }

        if (dirty)
            this.markDirty();
    }

    public boolean isWorking() {
        return this.progress > 0;
    }

    public boolean canWork(GrinderRecipe recipe) {
        if (recipe != null && this.energyStorage.extractEnergy(1, true) == 1) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                boolean output;
                boolean byproduct;

                ItemStack outputSlot = this.outputSlots.getStackInSlot(0);
                if (outputSlot.isEmpty()) {
                    output = true;
                } else if (!outputSlot.isItemEqual(recipeOutput)) {
                    output = false;
                } else if (outputSlot.getCount() + recipeOutput.getCount() <= this.outputSlots.getSlotLimit(0) && outputSlot.getCount() < outputSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    output = true;
                } else {
                    output = outputSlot.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }

                ItemStack byproductSlot = this.outputSlots.getStackInSlot(1);
                ItemStack byproductItem = recipe.getByproduct();
                if (byproductSlot.isEmpty() || byproductItem.isEmpty()) {
                    byproduct = true;
                } else if (!byproductSlot.isItemEqual(byproductItem)) {
                    byproduct = false;
                } else if (byproductSlot.getCount() + byproductItem.getCount() <= this.outputSlots.getSlotLimit(0) && byproductSlot.getCount() < byproductSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    byproduct = true;
                } else {
                    byproduct = byproductSlot.getCount() + byproductItem.getCount() <= byproductItem.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }

                return output && byproduct;
            }
        } else {
            return false;
        }
    }

    public void produceOutput(GrinderRecipe recipe) {
        if (recipe != null && this.canWork(recipe)) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            ItemStack outputSlot = outputSlots.getStackInSlot(0);
            if (outputSlot.isEmpty()) {
                this.outputSlots.setStackInSlot(0, recipeOutput.copy());
            } else if (outputSlot.getItem() == recipeOutput.getItem()) {
                outputSlot.grow(recipeOutput.getCount());
            }

            if (!recipe.getByproduct().isEmpty() && recipe.getByproductChance() > new Random().nextFloat()) {
                ItemStack byproductSlot = outputSlots.getStackInSlot(1);
                if (byproductSlot.isEmpty()) {
                    this.outputSlots.setStackInSlot(1, recipe.getByproduct().copy());
                } else if (byproductSlot.getItem() == recipe.getByproduct().getItem()) {
                    byproductSlot.grow(recipe.getByproduct().getCount());
                }
            }

            if (!this.world.isRemote) {
                this.canUseRecipe(this.world, null, recipe);
            }

            this.inputSlot.getStackInSlot(0).shrink(1);
        }
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for (int i = 0; i < this.combinedHandler.getSlots(); i++) {
            ItemStack stack = this.combinedHandler.getStackInSlot(i);
            recipeItemHelper.accountStack(stack);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe recipe) {
        if (recipe != null) {
            this.field_214022_n.compute(recipe.getId(), (resourceLocation, integer) -> 1 + (integer == null ? 0 : integer));
        }
    }

    @Nullable
    public IRecipe getRecipeUsed() {
        return null;
    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = Lists.newArrayList();
        Iterator var3 = this.field_214022_n.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<ResourceLocation, Integer> entry = (Map.Entry) var3.next();
            player.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                spawnXP(player, entry.getValue(), ((GrinderRecipe) recipe).getExperience());
            });
        }

        player.unlockRecipes(list);
        this.field_214022_n.clear();
    }

    private static void spawnXP(PlayerEntity player, int amount, float value) {
        int i;
        if (value == 0.0F) {
            amount = 0;
        } else if (value < 1.0F) {
            i = MathHelper.floor((float) amount * value);
            if (i < MathHelper.ceil((float) amount * value) && Math.random() < (double) ((float) amount * value - (float) i)) {
                ++i;
            }

            amount = i;
        }

        while (amount > 0) {
            i = ExperienceOrbEntity.getXPSplit(amount);
            amount -= i;
            player.world.addEntity(new ExperienceOrbEntity(player.world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, i));
        }

    }

    public int getEnergy() {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergy() {
        return this.energyStorage.getMaxEnergyStored();
    }

    private LazyOptional<IItemHandlerModifiable> combinedInvHandler = LazyOptional.of(() -> combinedHandler);
    private LazyOptional<IItemHandlerModifiable> inputSlotHandler = LazyOptional.of(() -> inputSlot);
    private LazyOptional<IItemHandlerModifiable> outputSlotHandler = LazyOptional.of(() -> outputSlots);
    private LazyOptional<IItemHandlerModifiable> energySlotHandler = LazyOptional.of(() -> energySlot);
    private LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return combinedInvHandler.cast();
            else if (side == Direction.UP)
                return inputSlotHandler.cast();
            else if (side == Direction.DOWN)
                return outputSlotHandler.cast();
            else
                return energySlotHandler.cast();
        } else if (cap == CapabilityEnergy.ENERGY)
            return energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
