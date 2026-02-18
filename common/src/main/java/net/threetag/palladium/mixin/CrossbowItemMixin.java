package net.threetag.palladium.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.item.AddonProjectileItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(at = @At("HEAD"), method = "shootProjectile")
    private static void shootProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack, ItemStack ammoStack, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle, CallbackInfo ci) {
        if (!level.isClientSide) {
            if (ammoStack.getItem() instanceof AddonProjectileItem projectileItem) {
                var projectile = projectileItem.createProjectile(level, ammoStack, shooter);

                if (shooter instanceof CrossbowAttackMob crossbowAttackMob) {
                    crossbowAttackMob.shootCrossbowProjectile(crossbowAttackMob.getTarget(), crossbowStack, projectile, projectileAngle);
                } else {
                    Vec3 vec3 = shooter.getUpVector(1.0F);
                    Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(projectileAngle * ((float) Math.PI / 180F), vec3.x, vec3.y, vec3.z);
                    Vec3 vec32 = shooter.getViewVector(1.0F);
                    Vector3f vector3f = vec32.toVector3f().rotate(quaternionf);
                    projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
                }

                crossbowStack.hurtAndBreak(1, shooter, (livingEntity) -> livingEntity.broadcastBreakEvent(hand));
                level.addFreshEntity(projectile);
                level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
            }
        }
    }

}
