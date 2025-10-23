package net.threetag.palladium.customization;

import com.mojang.serialization.MapCodec;

public abstract class CustomizationSerializer<T extends Customization> {

    public abstract MapCodec<T> codec();

}
