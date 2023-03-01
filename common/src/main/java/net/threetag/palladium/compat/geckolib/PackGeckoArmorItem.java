package net.threetag.palladium.compat.geckolib;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;

public interface PackGeckoArmorItem extends IAnimatable {

    PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, String animationName);

    ResourceLocation getGeckoModelLocation();

    ResourceLocation getGeckoTextureLocation();

    ResourceLocation getGeckoAnimationLocation();

}
