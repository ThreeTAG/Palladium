package net.threetag.palladium.power.ability;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.tags.PalladiumBlockTags;
import net.threetag.palladium.util.property.BlockTagProperty;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class IntangibilityAbility extends Ability {

    public static final PalladiumProperty<Boolean> VERTICAL = new BooleanProperty("vertical").configurable("Makes the player vertically intangible aswell");
    public static final PalladiumProperty<TagKey<Block>> WHITELIST = new BlockTagProperty("whitelist").configurable("Tag which includes the block the player can phase through. Leave null for all blocks.");
    public static final PalladiumProperty<TagKey<Block>> BLACKLIST = new BlockTagProperty("blacklist").configurable("Tag which includes the block the player can phase through. Leave null for all blocks.");

    public IntangibilityAbility() {
        this.withProperty(VERTICAL, false)
                .withProperty(WHITELIST, null)
                .withProperty(BLACKLIST, PalladiumBlockTags.PREVENTS_INTANGIBILITY);
    }

    public static boolean canGoThrough(AbilityEntry entry, BlockState state) {
        var whitelist = entry.getProperty(WHITELIST);
        var blacklist = entry.getProperty(BLACKLIST);

        if (whitelist != null) {
            if (blacklist != null) {
                return state.is(whitelist) && !state.is(blacklist);
            } else {
                return state.is(whitelist);
            }
        } else {
            if (blacklist != null) {
                return !state.is(blacklist);
            } else {
                return true;
            }
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes the player go through blocks.";
    }
}
