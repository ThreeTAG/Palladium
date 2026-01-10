package net.threetag.palladium.menu.slot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.Nullable;

public class TailoringToolSlot extends Slot {

    public static final ResourceLocation EMPTY_TOOL_SLOT = Palladium.id("container/slot/shears");
    public ResourceLocation icon = null;

    public TailoringToolSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public @Nullable ResourceLocation getNoItemIcon() {
        return this.icon;
    }
}
