package com.threetag.threecore.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

public class GuiHandler {

    @Nullable
    public static GuiScreen getClientGuiElement(FMLPlayMessages.OpenContainer container) {
        PacketBuffer buf = container.getAdditionalData();
        BlockPos pos = buf.readBlockPos();
        World world = Minecraft.getInstance().world;
        EntityPlayer player = Minecraft.getInstance().player;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IGuiTile) {
            return ((IGuiTile) te).createGui(player);
        }
        return null;
    }

}
