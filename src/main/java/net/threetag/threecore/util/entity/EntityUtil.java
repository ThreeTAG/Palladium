package net.threetag.threecore.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EntityUtil {

    public static void spawnXP(World world, double x, double y, double z, int amount, float value) {
        int i;
        if (value == 0.0F) {
            amount = 0;
        } else if (value < 1.0F) {
            i = MathHelper.floor((float) amount * value);
            if (i < MathHelper.ceil((float) amount * value) && Math.random() < (double) ((float) amount * value - (float) i)) {
                ++i;
            }

            amount = i;
        }

        while (amount > 0) {
            i = ExperienceOrbEntity.getXPSplit(amount);
            amount -= i;
            world.addEntity(new ExperienceOrbEntity(world, x, y, z, i));
        }
    }

    public static RayTraceResult rayTraceWithEntities(Entity entityIn, double distance, RayTraceContext.BlockMode blockModeIn, RayTraceContext.FluidMode fluidModeIn) {
        Vec3d lookVec = entityIn.getLookVec();
        Vec3d startVec = entityIn.getPositionVec().add(0, entityIn.getEyeHeight(), 0);
        Vec3d endVec = startVec.add(entityIn.getLookVec().scale(distance));
        RayTraceResult blockResult = entityIn.world.rayTraceBlocks(new RayTraceContext(startVec, endVec, blockModeIn, fluidModeIn, entityIn));
        RayTraceResult entityResult = null;

        for (int i = 0; i < distance * 2; i++) {
            if (entityResult != null)
                break;
            float scale = i / 2F;
            Vec3d pos = startVec.add(lookVec.scale(scale));

            Vec3d min = pos.add(0.25F, 0.25F, 0.25F);
            Vec3d max = pos.add(-0.25F, -0.25F, -0.25F);
            for (Entity entity : entityIn.world.getEntitiesWithinAABBExcludingEntity(entityIn, new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z))) {
                entityResult = new EntityRayTraceResult(entity, pos);
                break;
            }
        }

        if (entityResult != null && entityResult.getHitVec().distanceTo(startVec) <= blockResult.getHitVec().distanceTo(startVec)) {
            return entityResult;
        } else {
            return blockResult;
        }
    }

}
