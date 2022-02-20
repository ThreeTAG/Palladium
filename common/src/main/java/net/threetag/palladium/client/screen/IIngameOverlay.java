package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;

public interface IIngameOverlay {

    void render(Gui gui, PoseStack mStack, float partialTicks, int width, int height);

}
