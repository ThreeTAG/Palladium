package net.threetag.palladium.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladiumcore.item.PalladiumSpawnEggItem;

import java.util.function.Supplier;

public class SortedSpawnEggItem extends PalladiumSpawnEggItem {

    public static CreativeModeTabFiller DEFAULT_FILLER = new CreativeModeTabFiller(() -> Items.BEE_SPAWN_EGG);
    private CreativeModeTabFiller filler = DEFAULT_FILLER;

    public SortedSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    public SortedSpawnEggItem setFiller(CreativeModeTabFiller filler) {
        this.filler = filler;
        return this;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.filler != null) {
            if (this.allowedIn(category)) {
                this.filler.fill(this, category, items);
            }
        } else {
            super.fillItemCategory(category, items);
        }
    }

}
