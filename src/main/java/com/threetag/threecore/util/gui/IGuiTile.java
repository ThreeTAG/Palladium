package com.threetag.threecore.util.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

public interface IGuiTile {

    GuiContainer createGui(EntityPlayer player);

}
