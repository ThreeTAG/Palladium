package net.threetag.threecore.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;
import net.threetag.threecore.entity.SuitStandEntity;

import java.util.List;
import java.util.Random;

public class SuitStandItem extends Item {

    public SuitStandItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext useContext) {
        Direction direction = useContext.getFace();
        if (direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        } else {
            World world = useContext.getWorld();
            BlockItemUseContext blockItemUseContext = new BlockItemUseContext(useContext);
            BlockPos pos = blockItemUseContext.getPos();
            BlockPos up = pos.up();
            if (blockItemUseContext.canPlace() && world.getBlockState(up).isReplaceable(blockItemUseContext)) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));
                if (!entities.isEmpty()) {
                    return ActionResultType.FAIL;
                } else {
                    ItemStack itemStack = useContext.getItem();
                    if (!world.isRemote) {
                        world.removeBlock(pos, false);
                        world.removeBlock(up, false);
                        SuitStandEntity suitStandEntity = new SuitStandEntity(world, x + 0.5D, y, z + 0.5D);
                        suitStandEntity.setShowArms(true);
                        float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(useContext.getPlacementYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                        suitStandEntity.setLocationAndAngles(x + 0.5D, y, z + 0.5D, yaw, 0.0F);
                        EntityType.applyItemNBT(world, useContext.getPlayer(), suitStandEntity, itemStack.getTag());
                        if (itemStack.hasDisplayName()) {
                            suitStandEntity.setCustomName(itemStack.getDisplayName());
                            suitStandEntity.setCustomNameVisible(true);
                        }
                        world.addEntity(suitStandEntity);
                        world.playSound(null, suitStandEntity.getPosX(), suitStandEntity.getPosY(), suitStandEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                    }

                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            } else {
                return ActionResultType.FAIL;
            }
        }
    }

    private void applyRandomRotations(SuitStandEntity suitStandEntity, Random random) {
        Rotations rotation = suitStandEntity.getHeadRotation();
        float f1 = random.nextFloat() * 5.0F;
        float f2 = random.nextFloat() * 20.0F - 10.0F;
        Rotations rotations = new Rotations(rotation.getX() + f1, rotation.getY() + f2, rotation.getZ());
        suitStandEntity.setHeadRotation(rotations);
        rotation = suitStandEntity.getBodyRotation();
        f1 = random.nextFloat() * 10.0F - 5.0F;
        rotations = new Rotations(rotation.getX(), rotation.getY() + f1, rotation.getZ());
        suitStandEntity.setBodyRotation(rotations);
    }

}
