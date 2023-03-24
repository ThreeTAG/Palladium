package net.threetag.palladium.compat.geckolib;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.List;

public interface PackGeckoArmorItem extends IAnimatable {

    PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, List<ParsedAnimationController<IAnimatable>> animationControllers);

    ResourceLocation getGeckoModelLocation();

    ResourceLocation getGeckoTextureLocation();

    ResourceLocation getGeckoAnimationLocation();

}
