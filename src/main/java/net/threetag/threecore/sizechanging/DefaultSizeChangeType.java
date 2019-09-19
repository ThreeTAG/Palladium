package net.threetag.threecore.sizechanging;

import net.minecraft.entity.monster.CreeperEntity;
import net.threetag.threecore.sizechanging.capability.ISizeChanging;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class DefaultSizeChangeType extends SizeChangeType {

    @Override
    public int getSizeChangingTime(Entity entity, ISizeChanging data, float estimatedSize) {
        return 60;
    }

    @Override
    public void onSizeChanged(Entity entity, ISizeChanging data, float size) {
        if (entity instanceof LivingEntity) {
            AbstractAttributeMap map = ((LivingEntity) entity).getAttributes();
            setAttribute(map, SharedMonsterAttributes.MOVEMENT_SPEED, (size - 1F) * 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL, SizeChangeType.ATTRIBUTE_UUID);
            // TODO reimplement those attributes
            //setAttribute(map, LCAttributes.JUMP_HEIGHT, (size - 1F) * 1D, 0, SizeChangeType.ATTRIBUTE_UUID);
            //setAttribute(map, LCAttributes.FALL_RESISTANCE, size > 1F ? 1F / size : size, 1, SizeChangeType.ATTRIBUTE_UUID);
            //setAttribute(map, LCAttributes.STEP_HEIGHT, size, 1, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute(map, SharedMonsterAttributes.ATTACK_DAMAGE, (size - 1F) * 1D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute(map, PlayerEntity.REACH_DISTANCE, (size - 1F) * 1D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            setAttribute(map, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, (size - 1F) * 0.5D, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);

            if(entity instanceof CreeperEntity) {
                ((CreeperEntity)entity).explosionRadius = (int) (3F * size);
            }
        }
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

    public void setAttribute(AbstractAttributeMap map, IAttribute attribute, double value, AttributeModifier.Operation operation, UUID uuid) {
        if (map.getAttributeInstance(attribute) != null) {
            IAttributeInstance instance = map.getAttributeInstance(attribute);
            if (instance.getModifier(uuid) != null)
                instance.removeModifier(uuid);
            instance.applyModifier(new AttributeModifier(uuid, "default_size_changer", value, operation));
        }
    }

}
