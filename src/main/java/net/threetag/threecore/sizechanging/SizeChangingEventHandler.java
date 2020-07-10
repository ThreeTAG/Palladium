package net.threetag.threecore.sizechanging;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.entity.attributes.TCAttributes;
import net.threetag.threecore.network.SyncSizeMessage;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class SizeChangingEventHandler {

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        e.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            sizeChanging.updateBoundingBox();
            if (e.getEntity() instanceof ServerPlayerEntity && sizeChanging instanceof INBTSerializable)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getEntity().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });

        if (e.getEntity() instanceof ProjectileEntity) {
            copyScale(((ThrowableEntity) e.getEntity()).func_234616_v_(), e.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent e) {
        e.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(size -> {
            if (size.getScale() <= 0.3F) {
                if (!e.getEntityLiving().func_233570_aj_() && (e.getEntityLiving().getHeldItemMainhand().getItem() == Items.PAPER || e.getEntityLiving().getHeldItemOffhand().getItem() == Items.PAPER || e.getEntityLiving().getHeldItemMainhand().getItem() == Items.FEATHER || e.getEntityLiving().getHeldItemOffhand().getItem() == Items.FEATHER)) {
                    e.getEntityLiving().fallDistance = 0;
                    e.getEntityLiving().setMotion(e.getEntity().getMotion().x, e.getEntity().getMotion().y * 0.6D, e.getEntity().getMotion().z);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            if (sizeChanging instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getTarget().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

    @SubscribeEvent()
    public static void onCommonSetup(FMLCommonSetupEvent e) {
        DeferredWorkQueue.runLater(() -> {
            for (EntityType<?> value : ForgeRegistries.ENTITIES.getValues())
                if(value.getClassification() != EntityClassification.MISC)
                {
                    AttributeModifierMap map = GlobalEntityTypeAttributes.func_233835_a_((EntityType<? extends LivingEntity>) value);
                    Map<Attribute, ModifiableAttributeInstance> oldAttributes = map.field_233802_a_;
                    AttributeModifierMap.MutableAttribute newMap = AttributeModifierMap.func_233803_a_();
                    newMap.field_233811_a_.putAll(oldAttributes);
                    newMap.func_233814_a_(TCAttributes.SIZE_WIDTH);
                    newMap.func_233814_a_(TCAttributes.SIZE_HEIGHT);
                    GlobalEntityTypeAttributes.put((EntityType<? extends LivingEntity>) value, newMap.func_233813_a_());
                }
        });
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent e) {
        copyScale(e.getPlayer(), e.getEntityItem());
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent e) {
        e.getDrops().forEach(entity -> copyScale(e.getEntityLiving(), entity));
    }

    @SubscribeEvent
    public static void visibility(PlayerEvent.Visibility e) {
        e.getPlayer().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> e.modifyVisibility(sizeChanging.getScale()));
    }

    @SubscribeEvent
    public static void oProjectileImpactFireball(ProjectileImpactEvent.Fireball e) {
        e.getFireball().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(e.getFireball().world, e.getFireball().func_234616_v_());
            e.getFireball().world.createExplosion(null, e.getFireball().getPosX(), e.getFireball().getPosY(), e.getFireball().getPosZ(), sizeChanging.getScale(), flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
        });
    }

    @SubscribeEvent
    public static void oProjectileImpactThrowable(ProjectileImpactEvent.Throwable e) {
        if (e.getThrowable() instanceof SnowballEntity)
            e.getThrowable().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                float radius = sizeChanging.getScale();
                if (radius > 2F) {
                    List<BlockPos> positions = Lists.newLinkedList();
                    for (int x = 0; x < radius; x++) {
                        for (int z = 0; z < radius; z++) {
                            BlockPos pos = new BlockPos(e.getThrowable().getPosX() + x - radius / 2F, e.getThrowable().getPosY() + e.getThrowable().size.height / 2F + radius / 2F, e.getThrowable().getPosZ() + z - radius / 2F);
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
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        if (e.getTarget() instanceof CowEntity && e.getItemStack().getItem() instanceof BucketItem) {
            e.getTarget().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                if (sizeChanging.getScale() <= 0.75F) {
                    e.setCanceled(true);
                    e.setCancellationResult(ActionResultType.FAIL);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent e) {
        if (e.getSource().getImmediateSource() instanceof ProjectileEntity) {
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
