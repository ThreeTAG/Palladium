package net.threetag.threecore.util.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtil {

    public static final String ENERGY_UNIT = "FE";

    @OnlyIn(Dist.CLIENT)
    public static ITextComponent getFormattedEnergy(int energy, int maxEnergy) {
        return new StringTextComponent(I18n.format("threecore.util.energy_storage_display", energy, maxEnergy, ENERGY_UNIT));
    }

    @OnlyIn(Dist.CLIENT)
    public static ITextComponent getFormattedEnergy(IEnergyStorage energyStorage) {
        return getFormattedEnergy(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored());
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawTooltip(MatrixStack stack, EnergyStorage energyStorage, ContainerScreen gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        drawTooltip(stack, energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored(), gui, x, y, width, height, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawTooltip(MatrixStack stack, int energy, int maxEnergy, ContainerScreen gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            gui.renderTooltip(stack, getFormattedEnergy(energy, maxEnergy), mouseX + 10, mouseY);
        }
    }

}
