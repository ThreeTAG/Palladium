package net.threetag.palladium.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.threetag.palladium.Palladium;

public class TailoringToolSlot extends Slot {

    public static final ResourceLocation EMPTY_TOOL_SLOT = Palladium.id("item/empty_tool_slot_shears");
    public ResourceLocation icon = null;

    public TailoringToolSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return this.icon != null ? Pair.of(InventoryMenu.BLOCK_ATLAS, this.icon) : null;
    }
}
