package net.threetag.palladium.compat.geckolib.armor;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.List;

public interface PackGeckoArmorItem extends IAnimatable {

    PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, List<ParsedAnimationController<IAnimatable>> animationControllers);

    ResourceLocation getGeckoModelLocation();

    ResourceLocation getGeckoTextureLocation();

    ResourceLocation getGeckoAnimationLocation();

}
