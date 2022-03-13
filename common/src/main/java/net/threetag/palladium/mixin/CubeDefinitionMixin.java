package net.threetag.palladium.mixin;

import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.UVPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CubeDefinition.class)
public interface CubeDefinitionMixin {

    @Accessor
    String getComment();

    @Accessor
    Vector3f getOrigin();

    @Accessor
    Vector3f getDimensions();

    @Accessor
    CubeDeformation getGrow();

    @Accessor
    boolean getMirror();

    @Accessor
    UVPair getTexCoord();

    @Accessor
    UVPair getTexScale();

}
