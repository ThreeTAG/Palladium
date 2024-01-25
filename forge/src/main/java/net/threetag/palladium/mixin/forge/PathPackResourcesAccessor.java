package net.threetag.palladium.mixin.forge;

import net.minecraftforge.resource.PathPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;

@Mixin(PathPackResources.class)
public interface PathPackResourcesAccessor {

    @Invoker(value = "resolve", remap = false)
    Path invokeResolve(String... paths);

}
