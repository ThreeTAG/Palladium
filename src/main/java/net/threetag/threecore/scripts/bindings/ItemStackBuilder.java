package net.threetag.threecore.scripts.bindings;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.scripts.accessors.BlockStateAccessor;
import net.threetag.threecore.scripts.accessors.ItemStackAccessor;

public class ItemStackBuilder {

    public ItemStackAccessor create(@ScriptParameterName("itemId") String itemId) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        return item == null ? ItemStackAccessor.EMPTY : new ItemStackAccessor(new ItemStack(item));
    }

    public ItemStackAccessor createFromBlock(@ScriptParameterName("block") Object block) {
        if (block instanceof BlockStateAccessor) {
            return new ItemStackAccessor(new ItemStack(((BlockStateAccessor) block).value.getBlock()));
        } else {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.toString()));
            return b == null ? ItemStackAccessor.EMPTY : new ItemStackAccessor(new ItemStack(b));
        }
    }

    public ItemStackAccessor createFromJson(@ScriptParameterName("jsonString") String json) {
        try {
            return new ItemStackAccessor(CraftingHelper.getItemStack(JSONUtils.fromJson(json), true));
        } catch (Exception e) {
            return new ItemStackAccessor(ItemStack.EMPTY);
        }
    }

}
