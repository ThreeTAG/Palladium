package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class ThreeDataBlock extends ThreeData<Block>
{

    public ThreeDataBlock(String key) {
        super(key);
    }

    @Override
    public Block parseValue(JsonObject jsonObject, Block defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JsonUtils.getString(jsonObject, this.jsonKey)));
        if (block == null)
            throw new JsonSyntaxException("Block " + JsonUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return block;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Block value) {
        nbt.putString(this.key, Objects.requireNonNull(value.getRegistryName()).toString());
    }

    @Override
    public Block readFromNBT(NBTTagCompound nbt, Block defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString(this.key)));
    }

    @Override
    public String getDisplay(Block value) {
        return ForgeRegistries.BLOCKS.getKey(value).toString();
    }

    @Override
    public boolean displayAsString(Block value) {
        return true;
    }
}
