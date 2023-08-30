package net.threetag.palladium.util.property;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class BlockTagProperty extends TagKeyProperty<Block> {

    public BlockTagProperty(String key) {
        super(key, Registries.BLOCK);
    }

}
