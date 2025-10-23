package net.threetag.palladium.entity;

import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;

import java.util.Map;

public interface PalladiumEntityExtension {

    Map<PalladiumEntityDataType<?>, PalladiumEntityData<?, ?>> palladium$getDataMap();

}
