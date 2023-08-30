package net.threetag.palladium.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.power.PowerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@SuppressWarnings({"DataFlowIssue", "rawtypes"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PalladiumLivingEntityExtension {

    @Unique
    private PowerHandler palladium$powerHandler;

    @Unique
    private RenderLayerStates palladium$renderLayerStates;

    @Shadow
    public abstract AttributeMap getAttributes();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(EntityType entityType, Level level, CallbackInfo ci) {
        this.palladium$powerHandler = new PowerHandler((LivingEntity) (Object) this);
        this.palladium$renderLayerStates = new RenderLayerStates();
    }

    @Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        if (this.getAttributes().hasAttribute(PalladiumAttributes.JUMP_POWER.get())) {
            var instance = this.getAttributes().getInstance(PalladiumAttributes.JUMP_POWER.get());
            cir.setReturnValue((float) Objects.requireNonNull(instance).getValue() * cir.getReturnValue());
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide) {
            this.palladium$renderLayerStates.tick(entity);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        CompoundTag palladiumTag = compound.contains("Palladium") ? compound.getCompound("Palladium") : new CompoundTag();
        if (palladiumTag.contains("Powers", Tag.TAG_COMPOUND)) {
            this.palladium$powerHandler.fromNBT(palladiumTag.getCompound("Powers"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        CompoundTag palladiumTag = compound.contains("Palladium") ? compound.getCompound("Palladium") : new CompoundTag();
        palladiumTag.put("Powers", this.palladium$powerHandler.toNBT());
        compound.put("Palladium", palladiumTag);
    }

    @Override
    public PowerHandler palladium$getPowerHandler() {
        return this.palladium$powerHandler;
    }

    @Override
    public RenderLayerStates palladium$getRenderLayerStates() {
        return this.palladium$renderLayerStates;
    }

}
