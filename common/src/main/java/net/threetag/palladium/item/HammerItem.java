package net.threetag.palladium.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.threetag.palladium.tags.PalladiumBlockTags;

public class HammerItem extends DiggerItem {

    public HammerItem(float attackDamage, float attackSpeed, Tier tier, Properties properties) {
        super(attackDamage, attackSpeed, tier, PalladiumBlockTags.MINEABLE_WITH_HAMNMER, properties);
    }

}
