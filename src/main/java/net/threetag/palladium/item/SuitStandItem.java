package net.threetag.palladium.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.entity.SuitStand;

import java.util.function.Consumer;

public class SuitStandItem extends Item {

    public SuitStandItem(Properties properties) {
        super(properties);
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
                if (level instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)level;
                    Consumer<SuitStand> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
                    SuitStand suitStand = PalladiumEntityTypes.SUIT_STAND.get().create(serverLevel, consumer, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
                    if (suitStand == null) {
                        return InteractionResult.FAIL;
                    }

                    float f = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    suitStand.snapTo(suitStand.getX(), suitStand.getY(), suitStand.getZ(), f, 0.0F);
                    serverLevel.addFreshEntityWithPassengers(suitStand);
                    level.playSound(null, suitStand.getX(), suitStand.getY(), suitStand.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    suitStand.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
                }

                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
