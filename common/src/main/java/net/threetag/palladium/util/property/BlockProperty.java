package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class BlockProperty extends RegistryObjectProperty<Block> {

    public BlockProperty(String key) {
        super(key, BuiltInRegistries.BLOCK);
    }
}
