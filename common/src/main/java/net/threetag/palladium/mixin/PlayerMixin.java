package net.threetag.palladium.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public abstract class PlayerMixin implements PalladiumPlayerExtension {

    @Unique
    private FlightHandler palladium$flightHandler;

    @Unique
    private AccessoryPlayerData palladium$accessories;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(Level level, BlockPos blockPos, float f, GameProfile gameProfile, ProfilePublicKey profilePublicKey, CallbackInfo ci) {
        this.palladium$getFlightHandler();
        this.palladium$accessories = new AccessoryPlayerData();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        this.palladium$flightHandler.tick();
    }

    @ModifyVariable(method = "getDimensions", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getDimensions(Pose pose) {
        var hover = this.palladium$flightHandler.getHoveringAnimation(0);
        var levitation = this.palladium$flightHandler.getLevitationAnimation(0);
        var flight = this.palladium$flightHandler.getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.palladium$flightHandler.flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
    }

    @ModifyVariable(method = "getStandingEyeHeight", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getStandingEyeHeight(Pose pose) {
        var hover = this.palladium$getFlightHandler().getHoveringAnimation(0);
        var levitation = this.palladium$getFlightHandler().getLevitationAnimation(0);
        var flight = this.palladium$getFlightHandler().getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.palladium$getFlightHandler().flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        CompoundTag palladiumTag = compound.contains("Palladium") ? compound.getCompound("Palladium") : new CompoundTag();
        if (palladiumTag.contains("Accessories", Tag.TAG_COMPOUND)) {
            this.palladium$accessories.fromNBT(palladiumTag.getCompound("Accessories"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        CompoundTag palladiumTag = compound.contains("Palladium") ? compound.getCompound("Palladium") : new CompoundTag();
        palladiumTag.put("Accessories", this.palladium$accessories.toNBT());
        compound.put("Palladium", palladiumTag);
    }

    @Override
    public FlightHandler palladium$getFlightHandler() {
        if (this.palladium$flightHandler == null) {
            this.palladium$flightHandler = new FlightHandler((Player) (Object) this);
        }

        return this.palladium$flightHandler;
    }

    @Override
    public AccessoryPlayerData palladium$getAccessories() {
        return this.palladium$accessories;
    }
}
