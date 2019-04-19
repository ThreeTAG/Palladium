package com.threetag.threecore.util.render;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ItemIcon implements IIcon {

    public final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    public ItemIcon(IItemProvider itemProvider) {
        this.stack = new ItemStack(itemProvider);
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        mc.getItemRenderer().renderItemIntoGUI(this.stack, x, y);
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public IIconSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIconSerializer<ItemIcon> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "item");

        @Override
        public ItemIcon read(JsonObject json) {
            return new ItemIcon(CraftingHelper.getItemStack(json, true));
        }

        @Override
        public ItemIcon read(NBTTagCompound nbt) {
            return new ItemIcon(ItemStack.read(nbt));
        }

        @Override
        public NBTTagCompound serialize(ItemIcon icon) {
            return icon.stack.serializeNBT();
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }

}
