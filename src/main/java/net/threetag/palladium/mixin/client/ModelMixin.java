package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.ModelUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(Model.class)
public class ModelMixin<S> {

    @Shadow
    @Final
    private List<ModelPart> allParts;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setupAnim(S s, CallbackInfo ci) {
        for (ModelPart part : this.allParts) {
            part.visible = true;
        }
        if (s instanceof EntityRenderState state) {
            for (String partName : state.getRenderDataOrDefault(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS, Collections.emptySet())) {
                var part = ModelUtil.getPartFromModel((Model<?>) (Object) this, partName);

                if (part != null) {
                    part.visible = false;
                }
            }
        }
    }

}
