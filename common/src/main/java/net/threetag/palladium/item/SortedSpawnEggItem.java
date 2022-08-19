package net.threetag.palladium.item;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class SortedSpawnEggItem extends ArchitecturySpawnEggItem {

    public static CreativeModeTabFiller DEFAULT_FILLER = new CreativeModeTabFiller(() -> Items.BEE_SPAWN_EGG);
    private CreativeModeTabFiller filler = DEFAULT_FILLER;

    public SortedSpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties) {
        super(entityType, backgroundColor, highlightColor, properties);
    }

    public SortedSpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties, @Nullable DispenseItemBehavior dispenseItemBehavior) {
        super(entityType, backgroundColor, highlightColor, properties, dispenseItemBehavior);
    }

    public SortedSpawnEggItem setFiller(CreativeModeTabFiller filler) {
        this.filler = filler;
        return this;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.allowdedIn(category)) {
            this.filler.fill(this, category, items);
        }
    }

}
