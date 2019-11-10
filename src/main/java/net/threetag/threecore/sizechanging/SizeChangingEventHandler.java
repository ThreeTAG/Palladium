package net.threetag.threecore.sizechanging;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.sizechanging.capability.SizeChangingProvider;
import net.threetag.threecore.sizechanging.network.SyncSizeMessage;

import java.util.List;

public class SizeChangingEventHandler {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (canSizeChange(e.getObject()) && !e.getObject().getCapability(CapabilitySizeChanging.SIZE_CHANGING).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "size_changing"), new SizeChangingProvider(new CapabilitySizeChanging(e.getObject())));
        }
    }

    public static boolean canSizeChange(Entity entity) {
        if (entity instanceof HangingEntity || entity instanceof ShulkerEntity || entity instanceof EnderCrystalEntity)
            return false;
        return true;
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent e) {
        e.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            sizeChanging.updateBoundingBox();
            if (e.getEntity() instanceof ServerPlayerEntity && sizeChanging instanceof INBTSerializable)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getEntity().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });

        Entity thrower = null;
        if (e.getEntity() instanceof ThrowableEntity)
            thrower = ((ThrowableEntity) e.getEntity()).getThrower();
        else if (e.getEntity() instanceof AbstractArrowEntity)
            thrower = ((AbstractArrowEntity) e.getEntity()).getShooter();
        else if (e.getEntity() instanceof DamagingProjectileEntity)
            thrower = ((DamagingProjectileEntity) e.getEntity()).shootingEntity;

        if (thrower != null) {
            copyScale(thrower, e.getEntity());
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            if (sizeChanging instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getTarget().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing e) {
        if (e.getEntity() instanceof LivingEntity) {
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(SizeManager.SIZE_WIDTH);
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(SizeManager.SIZE_HEIGHT);
        }
    }

    @SubscribeEvent
    public void onItemToss(ItemTossEvent e) {
        copyScale(e.getPlayer(), e.getEntityItem());
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent e) {
        e.getDrops().forEach(entity -> {
            copyScale(e.getEntityLiving(), entity);
        });
    }

    @SubscribeEvent
    public void visibility(PlayerEvent.Visibility e) {
        e.getPlayer().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> e.modifyVisibility(sizeChanging.getScale()));
    }

    @SubscribeEvent
    public void oProjectileImpactFireball(ProjectileImpactEvent.Fireball e) {
        e.getFireball().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(e.getFireball().world, e.getFireball().shootingEntity);
            e.getFireball().world.createExplosion((Entity) null, e.getFireball().posX, e.getFireball().posY, e.getFireball().posZ, sizeChanging.getScale(), flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
        });
    }

    @SubscribeEvent
    public void oProjectileImpactThrowable(ProjectileImpactEvent.Throwable e) {
        if (e.getThrowable() instanceof SnowballEntity)
            e.getThrowable().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                float radius = sizeChanging.getScale();
                if (radius > 2F) {
                    List<BlockPos> positions = Lists.newLinkedList();
                    for (int x = 0; x < radius; x++) {
                        for (int z = 0; z < radius; z++) {
                            BlockPos pos = new BlockPos(e.getThrowable().posX + x - radius / 2F, e.getThrowable().posY + e.getThrowable().size.height / 2F + radius / 2F, e.getThrowable().posZ + z - radius / 2F);
                            int i = 0;
                            boolean b = false;
                            while (i < radius && !b) {
                                if (!e.getThrowable().world.isAirBlock(pos)) {
                                    b = true;
                                } else {
                                    pos = pos.down();
                                }

                                i++;
                            }
                            if (e.getThrowable().world.isAirBlock(pos.up())) {
                                positions.add(pos.up());
                            }
                        }
                    }

                    for (BlockPos pos : positions) {
                        if (Blocks.SNOW.isValidPosition(Blocks.SNOW.getDefaultState(), e.getThrowable().world, pos))
                            e.getThrowable().world.setBlockState(pos, Blocks.SNOW.getDefaultState());
                    }
                }
            });
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent e) {
        if (e.getSource().getImmediateSource() instanceof IProjectile) {
            e.getSource().getImmediateSource().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                e.setAmount(e.getAmount() * sizeChanging.getScale());
            });
        }
    }

    public static void copyScale(Entity source, Entity entity) {
        source.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging1 -> {
            entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                sizeChanging.setSizeDirectly(sizeChanging1.getSizeChangeType(), sizeChanging1.getScale());
            });
        });
    }

}
