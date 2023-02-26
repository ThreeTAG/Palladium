package net.threetag.palladium.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.entity.SuitStand;

public class SuitStandItem extends SortedItem {

    public SuitStandItem(Properties properties) {
        super(properties, new CreativeModeTabFiller(() -> Items.ARMOR_STAND));

        DispenserBlock.registerBehavior(this, new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockpos = source.getPos().relative(direction);
                Level level = source.getLevel();
                SuitStand suitStand = new SuitStand(level, (double) blockpos.getX() + 0.5, blockpos.getY(), (double) blockpos.getZ() + 0.5);
                EntityType.updateCustomEntityTag(level, null, suitStand, stack.getTag());
                suitStand.setYRot(direction.toYRot());
                level.addFreshEntity(suitStand);
                stack.shrink(1);
                return stack;
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            Level level = context.getLevel();
            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
            BlockPos blockPos = blockPlaceContext.getClickedPos();
            ItemStack itemStack = context.getItemInHand();
            Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
            AABB aABB = PalladiumEntityTypes.SUIT_STAND.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
            if (level.noCollision(null, aABB) && level.getEntities(null, aABB).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    SuitStand suitStand = PalladiumEntityTypes.SUIT_STAND.get()
                            .create(serverLevel, itemStack.getTag(), null, context.getPlayer(), blockPos, MobSpawnType.SPAWN_EGG, true, true);
                    if (suitStand == null) {
                        return InteractionResult.FAIL;
                    }

                    float f = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    suitStand.moveTo(suitStand.getX(), suitStand.getY(), suitStand.getZ(), f, 0.0F);
                    this.randomizePose(suitStand, level.random);
                    serverLevel.addFreshEntityWithPassengers(suitStand);
                    level.playSound(null, suitStand.getX(), suitStand.getY(), suitStand.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    suitStand.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
                }

                itemStack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    private void randomizePose(SuitStand suitStand, RandomSource random) {
        Rotations rotations = suitStand.getHeadPose();
        float f = random.nextFloat() * 5.0F;
        float g = random.nextFloat() * 20.0F - 10.0F;
        Rotations rotations2 = new Rotations(rotations.getX() + f, rotations.getY() + g, rotations.getZ());
        suitStand.setHeadPose(rotations2);
        rotations = suitStand.getBodyPose();
        f = random.nextFloat() * 10.0F - 5.0F;
        rotations2 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        suitStand.setBodyPose(rotations2);
    }

}
