package net.threetag.palladium.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluxCapacitorItem extends EnergyItem {

    private static final int BAR_COLOR = Mth.color(0.9F, 0.1F, 0F);

    public FluxCapacitorItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties, capacity, maxInput, maxOutput);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        var stored = EnergyHelper.getEnergyStoredInItem(stack);
        tooltipComponents.add(Component.translatable("item.palladium.flux_capacitor.desc",
                Component.literal(Utils.getFormattedNumber(stored)).withStyle(ChatFormatting.GOLD),
                Component.literal(Utils.getFormattedNumber(this.getEnergyCapacity(stack))).withStyle(ChatFormatting.GOLD)
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var storage = EnergyHelper.getFromItemStack(stack);
        return storage.map(energyStorage -> energyStorage.getEnergyAmount() < energyStorage.getEnergyCapacity()).orElse(false);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var storage = EnergyHelper.getFromItemStack(stack);
        return storage.map(energyStorage -> Math.round(13F * energyStorage.getEnergyAmount() / (float) energyStorage.getEnergyCapacity())).orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.allowedIn(category)) {
            items.add(this.getDefaultInstance());
            var filled = this.getDefaultInstance();
            filled.getOrCreateTag().putInt("energy", this.getEnergyCapacity(filled));
            items.add(filled);
        }
    }
}
