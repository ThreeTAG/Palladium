package net.threetag.palladium.mixin;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(PartDefinition.class)
public interface PartDefinitionMixin {

    @Accessor
    List<CubeDefinition> getCubes();

    @Accessor
    PartPose getPartPose();

    @Accessor
    Map<String, PartDefinition> getChildren();

}
