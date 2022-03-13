package net.threetag.palladium.mixin;

import net.minecraft.client.model.geom.builders.MaterialDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MaterialDefinition.class)
public interface MaterialDefinitionMixin {

    @Accessor
    int getXTexSize();

    @Accessor
    int getYTexSize();

}
