package net.threetag.palladium.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PalladiumLivingEntityExtension {

    @Shadow
    public abstract AttributeMap getAttributes();

    @Unique
    private RenderLayerStates renderLayerStates = new RenderLayerStates();

    @Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        if (this.getAttributes().hasAttribute(PalladiumAttributes.JUMP_POWER.get())) {
            var instance = this.getAttributes().getInstance(PalladiumAttributes.JUMP_POWER.get());
            cir.setReturnValue((float) Objects.requireNonNull(instance).getValue() * cir.getReturnValue());
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        this.renderLayerStates.tick((LivingEntity) (Object) this);
    }

    @Override
    public RenderLayerStates palladium_getRenderLayerStates() {
        return this.renderLayerStates;
    }

}
