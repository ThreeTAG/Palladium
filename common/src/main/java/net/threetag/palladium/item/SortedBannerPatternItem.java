package net.threetag.palladium.item;

import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPattern;

public class SortedBannerPatternItem extends BannerPatternItem {

    public static final CreativeModeTabFiller DEFAULT_FILLER = new CreativeModeTabFiller(() -> Items.PIGLIN_BANNER_PATTERN);
    public CreativeModeTabFiller filler = DEFAULT_FILLER;

    public SortedBannerPatternItem(TagKey<BannerPattern> tagKey, Properties properties) {
        super(tagKey, properties);
    }

    public SortedBannerPatternItem setFiller(CreativeModeTabFiller filler) {
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
