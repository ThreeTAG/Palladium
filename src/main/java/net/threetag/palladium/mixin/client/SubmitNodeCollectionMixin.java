package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.util.ARGB;
import net.threetag.palladium.client.AbilityClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin {

    @ModifyVariable(method = "submitModel", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    public int modifyOpacityModel(int value) {
        if (AbilityClientEventHandler.OVERRIDDEN_OPACITY < 1F) {
            return ARGB.multiply(ARGB.white(AbilityClientEventHandler.OVERRIDDEN_OPACITY), value);
        }
        return value;
    }

    @ModifyVariable(method = "submitModelPart", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    public int modifyOpacityModelPart(int value) {
        if (AbilityClientEventHandler.OVERRIDDEN_OPACITY < 1F) {
            return ARGB.multiply(ARGB.white(AbilityClientEventHandler.OVERRIDDEN_OPACITY), value);
        }
        return value;
    }

}
