package net.threetag.palladium.entity;

import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;

import java.util.Map;

public interface PalladiumEntityExtension {

    Vec3 palladium$getPreviousPosition();

    Map<PalladiumEntityDataType<?>, PalladiumEntityData<?, ?>> palladium$getDataMap();

}
