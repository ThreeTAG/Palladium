package net.threetag.palladium.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;

public class PalladiumBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Palladium.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

}
