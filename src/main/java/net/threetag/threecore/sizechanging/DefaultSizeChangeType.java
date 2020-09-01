package net.threetag.threecore.sizechanging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeMod;
import net.threetag.threecore.capability.ISizeChanging;
import net.threetag.threecore.entity.attributes.TCAttributes;

import java.util.Random;
import java.util.UUID;

public class DefaultSizeChangeType extends SizeChangeType {

    @Override
    public int getSizeChangingTime(Entity entity, ISizeChanging data, float estimatedSize) {
        return 60;
    }

    @Override
    public void onSizeChanged(Entity entity, ISizeChanging data, float size) {
        if (entity instanceof LivingEntity) {

            setAttribute((LivingEntity) entity, Attributes.MOVEMENT_SPEED, (size - 1F) * 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute((LivingEntity) entity, TCAttributes.JUMP_HEIGHT.get(), (size - 1F) * 1D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute((LivingEntity) entity, TCAttributes.FALL_RESISTANCE.get(), size > 1F ? 1F / size : size, AttributeModifier.Operation.MULTIPLY_BASE, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute((LivingEntity) entity, Attributes.ATTACK_DAMAGE, (size - 1F) * 1D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute((LivingEntity) entity, ForgeMod.REACH_DISTANCE.get(), (size - 1F) * 1D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute((LivingEntity) entity, Attributes.KNOCKBACK_RESISTANCE, (size - 1F) * 0.5D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);

            changeCreeperExplosionRadius(entity, size);
            spawnWaterParticles(entity);
        }
    }

    public void changeCreeperExplosionRadius(Entity entity, float size) {
        if (entity instanceof CreeperEntity) {
            ((CreeperEntity) entity).explosionRadius = (int) (3F * size);
        }
    }

    public void spawnWaterParticles(Entity entity) {
        AxisAlignedBB box = entity.getBoundingBox();
        Random random = new Random();
        double x = box.minX + (box.maxX - box.minX) * random.nextDouble();
        double y = box.minY + (box.maxY - box.minY) * random.nextDouble();
        double z = box.minZ + (box.maxZ - box.minZ) * random.nextDouble();
        if (entity.world.hasWater(new BlockPos(x, y, z)))
            entity.world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
    }

    @Override
    public void onUpdate(Entity entity, ISizeChanging data, float size) {

    }

    @Override
    public boolean start(Entity entity, ISizeChanging data, float size, float estimatedSize) {
        return true;
    }

    @Override
    public void end(Entity entity, ISizeChanging data, float size) {

    }

    public void setAttribute(LivingEntity entity, Attribute attribute, double value, AttributeModifier.Operation operation, UUID uuid) {
        if (entity.getAttribute(attribute) != null) {
            ModifiableAttributeInstance instance = entity.getAttribute(attribute);
            if (instance.getModifier(uuid) != null)
                instance.removeModifier(uuid);
            instance.applyNonPersistentModifier(new AttributeModifier(uuid, "default_size_changer", value, operation));
        }
    }

}
