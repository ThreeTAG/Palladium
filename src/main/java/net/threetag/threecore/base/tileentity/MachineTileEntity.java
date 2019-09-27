package net.threetag.threecore.base.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.tileentity.LockableItemCapTileEntity;

import java.util.Map;
import java.util.Objects;

public abstract class MachineTileEntity extends LockableItemCapTileEntity implements IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {

    private final Map<ResourceLocation, Integer> recipeUseCounts = Maps.newHashMap();

    public MachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (Objects.requireNonNull(this.world).getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        int i = nbt.getShort("RecipesUsedSize");
        for (int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("RecipeLocation" + j));
            int k = nbt.getInt("RecipeAmount" + j);
            this.recipeUseCounts.put(resourcelocation, k);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putShort("RecipesUsedSize", (short) this.recipeUseCounts.size());
        int i = 0;

        for (Map.Entry<ResourceLocation, Integer> entry : this.recipeUseCounts.entrySet()) {
            nbt.putString("RecipeLocation" + i, entry.getKey().toString());
            nbt.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        return super.write(nbt);
    }
}
