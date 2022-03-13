package net.threetag.palladium.mixin;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LayerDefinition.class)
public interface LayerDefinitionMixin {

    @Accessor
    MeshDefinition getMesh();

    @Accessor
    MaterialDefinition getMaterial();

}
