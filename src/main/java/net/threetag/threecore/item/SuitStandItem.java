package net.threetag.threecore.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.threetag.threecore.entity.SuitStandEntity;
import net.threetag.threecore.util.ItemGroupFiller;

import java.util.List;

public class SuitStandItem extends Item {

    public static final ItemGroupFiller FILLER = new ItemGroupFiller(() -> Items.ARMOR_STAND);

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

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            FILLER.fill(items, new ItemStack(this));
        }
    }

}
