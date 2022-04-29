package net.threetag.palladium.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EntityUtil {

    public static Vec3 getLookVector(Entity entity) {
        return MathUtil.calculateViewVector(entity.getXRot(), entity.getYHeadRot());
    }

    public static Vec3 getLookVector(Entity entity, float partialTicks) {
        float headRot = entity instanceof LivingEntity livingEntity ? Mth.lerp(partialTicks, livingEntity.yHeadRotO, livingEntity.yHeadRot) : entity.getYHeadRot();
        return MathUtil.calculateViewVector(entity.getViewXRot(partialTicks), headRot);
    }

    public static HitResult rayTraceWithEntities(Entity entityIn, double distance, ClipContext.Block blockModeIn, ClipContext.Fluid fluidModeIn) {
        return rayTraceWithEntities(entityIn, null, null, distance, blockModeIn, fluidModeIn, (e) -> true);
    }

    public static HitResult rayTraceWithEntities(Entity entityIn, @Nullable Vec3 startVec, @Nullable Vec3 endVec, double distance, ClipContext.Block blockModeIn, ClipContext.Fluid fluidModeIn, Predicate<Entity> entityPredicate) {
        Vec3 lookVec = getLookVector(entityIn);
        startVec = startVec == null ? entityIn.position().add(0, entityIn.getEyeHeight(), 0) : startVec;
        endVec = endVec == null ? startVec.add(lookVec.scale(distance)) : endVec;
        HitResult blockResult = entityIn.level.clip(new ClipContext(startVec, endVec, blockModeIn, fluidModeIn, entityIn));
        HitResult entityResult = null;

        for (int i = 0; i < distance * 2; i++) {
            if (entityResult != null)
                break;
            float scale = i / 2F;
            Vec3 pos = startVec.add(lookVec.scale(scale));

//            entityIn.level.addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);

            Vec3 min = pos.add(0.25F, 0.25F, 0.25F);
            Vec3 max = pos.add(-0.25F, -0.25F, -0.25F);
            for (Entity entity : entityIn.level.getEntities(entityIn, new AABB(min.x, min.y, min.z, max.x, max.y, max.z))) {
                if (entityPredicate.test(entity)) {
                    entityResult = new EntityHitResult(entity, pos);
                    break;
                }
            }
        }

        if (entityResult != null && entityResult.getLocation().distanceTo(startVec) <= blockResult.getLocation().distanceTo(startVec)) {
            return entityResult;
        } else {
            return blockResult;
        }
    }

}
