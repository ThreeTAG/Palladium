package net.threetag.threecore.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.threetag.threecore.entity.SolidItemEntity;

import javax.annotation.Nullable;

public class SolidItem extends Item {

    public SolidItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        SolidItemEntity entity = new SolidItemEntity(world, location.posX, location.posY, location.posZ, itemstack);
        entity.setMotion(location.getMotion());
        if (location instanceof ItemEntity) {
            entity.setOwnerId(((ItemEntity) location).getOwnerId());
            entity.setThrowerId(((ItemEntity) location).getThrowerId());
        }
        return entity;
    }

    @Nullable
    public EntitySize getEntitySize(SolidItemEntity entity, ItemStack stack) {
        return null;
    }

    public boolean onSolidEntityItemUpdate(SolidItemEntity entity) {
        return false;
    }

}
