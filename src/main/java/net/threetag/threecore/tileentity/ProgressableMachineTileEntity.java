package net.threetag.threecore.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.threetag.threecore.block.MachineBlock;
import net.threetag.threecore.util.EntityUtil;
import net.threetag.threecore.item.recipe.IEnergyRecipe;

import java.util.List;
import java.util.Map;

public abstract class ProgressableMachineTileEntity<T extends IEnergyRecipe<IInventory>> extends MachineTileEntity {

    public T recipe;
    public int progress;
    public int maxProgress;

    private final Map<ResourceLocation, Integer> xpMap = Maps.newHashMap();

    public ProgressableMachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        super.tick();

        boolean working = this.isWorking();
        boolean dirty = false;

        if (this.world != null && !this.world.isRemote) {
            if (recipe != null && this.energyStorage.extractEnergy(1, true) == 1 && canWork(recipe)) {
                this.maxProgress = recipe.getRequiredEnergy();
                if (progress >= maxProgress) {
                    this.addXP();
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
        }

        if (working != this.isWorking()) {
            dirty = true;
            if (this.world.getBlockState(this.pos).get(MachineBlock.LIT))
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MachineBlock.LIT, this.isWorking()), 3);
        }

        if (dirty)
            this.markDirty();
    }

    public abstract IRecipeType<T> getRecipeType();

    public float getXpFromRecipe(T recipe) {
        return 0F;
    }

    public boolean isWorking() {
        return this.progress > 0;
    }

    public abstract boolean canWork(T recipe);

    public abstract void produceOutput(T recipe);

    public void addXP() {
        if (this.xpMap != null)
            this.xpMap.compute(recipe.getId(), (resourceLocation, integer) -> 1 + (integer == null ? 0 : integer));
    }

    public void updateRecipe(T recipe) {
        this.recipe = recipe;
    }

    public void updateRecipe(IInventory inventory) {
        if (this.getWorld() != null)
            this.recipe = this.getWorld().getRecipeManager().getRecipes(this.getRecipeType()).values().stream().
                    flatMap(recipe -> Util.streamOptional(this.getRecipeType().matches(recipe, this.getWorld(), inventory))).
                    findFirst().orElse(null);
    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for (Map.Entry<ResourceLocation, Integer> entry : this.xpMap.entrySet()) {
            player.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                EntityUtil.spawnXP(player.world, player.getPosX(), player.getPosY() + 0.5D, player.getPosZ() + 0.5D, entry.getValue(), getXpFromRecipe((T) recipe));
            });
        }

        player.unlockRecipes(list);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        this.recipe = getRecipe(new ResourceLocation(nbt.getString("Recipe")));
        this.progress = nbt.getInt("Progress");
        this.maxProgress = nbt.getInt("MaxProgress");

        int i = nbt.getShort("RecipesUsedSize");
        for (int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("RecipeLocation" + j));
            int k = nbt.getInt("RecipeAmount" + j);
            this.xpMap.put(resourcelocation, k);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {

        if (this.recipe != null)
            nbt.putString("Recipe", this.recipe.getId().toString());
        nbt.putInt("Progress", this.progress);
        nbt.putInt("MaxProgress", this.maxProgress);

        nbt.putShort("RecipesUsedSize", (short) this.xpMap.size());
        int i = 0;
        for (Map.Entry<ResourceLocation, Integer> entry : this.xpMap.entrySet()) {
            nbt.putString("RecipeLocation" + i, entry.getKey().toString());
            nbt.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        return super.write(nbt);
    }

    private T getRecipe(ResourceLocation id) {
        if (this.getWorld() == null)
            return null;

        for (IRecipe recipe : this.getWorld().getRecipeManager().getRecipes()) {
            if (recipe.getType() == this.getRecipeType() && recipe.getId().equals(id)) {
                return (T) recipe;
            }
        }

        return null;
    }

}
