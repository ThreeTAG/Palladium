package net.threetag.palladium.power.ability;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.TagKeyListProperty;

import java.util.Arrays;
import java.util.List;

public class ToolHandsAbility extends Ability {

    public static final PalladiumProperty<List<TagKey<Block>>> DROP_BLOCK_TAGS = new TagKeyListProperty<>("drop_block_tags", Registries.BLOCK).configurable("Blocks in these tags will be dropped when mined");

    public ToolHandsAbility() {
        this.withProperty(ICON, new ItemIcon(Items.WOODEN_PICKAXE));
        this.withProperty(DROP_BLOCK_TAGS, Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL));
    }

    public static boolean blockDrops(AbilityInstance instance, BlockState blockState) {
        if (!instance.isEnabled()) {
            return false;
        }

        for (TagKey<Block> blockTag : instance.getProperty(DROP_BLOCK_TAGS)) {
            if (blockState.is(blockTag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows the player to mine a block and receive its drops as if a tool were used";
    }
}
