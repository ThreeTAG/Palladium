package net.threetag.threecore.util.energy;

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
    public static void drawTooltip(EnergyStorage energyStorage, ContainerScreen gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        drawTooltip(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored(), gui, x, y, width, height, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawTooltip(int energy, int maxEnergy, ContainerScreen gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            String s = getFormattedEnergy(energy, maxEnergy).getFormattedText();
            gui.renderTooltip(s, mouseX + 10, mouseY);
        }
    }

}
