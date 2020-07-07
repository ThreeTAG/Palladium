package net.threetag.threecore.karma;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilityKarma;
import net.threetag.threecore.network.SyncKarmaMessage;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class KarmaEventHandler {

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone e) {
        if (e.isWasDeath()) {
            e.getOriginal().getCapability(CapabilityKarma.KARMA).ifPresent(oldCap -> e.getPlayer().getCapability(CapabilityKarma.KARMA).ifPresent(newCap -> newCap.setKarma(oldCap.getKarma())));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof ServerPlayerEntity) {
            e.getEntity().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> ThreeCore.NETWORK_CHANNEL.sendTo(new SyncKarmaMessage(e.getEntity().getEntityId(), k.getKarma()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity attacker = (PlayerEntity) e.getSource().getTrueSource();
            LivingEntity killed = e.getEntityLiving();

            if (isMonster(killed))
                CapabilityKarma.addKarma(attacker, 2);
            else if (killed.getCapability(CapabilityKarma.KARMA).isPresent()) {
                killed.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
                    if (KarmaClass.fromKarma(k.getKarma()).isGood())
                        CapabilityKarma.addKarma(attacker, -2);
                    else
                        CapabilityKarma.addKarma(attacker, +2);
                });
            } else if (killed instanceof VillagerEntity)
                CapabilityKarma.addKarma(attacker, -2);
            else if (killed instanceof TameableEntity && ((TameableEntity) killed).isTamed())
                CapabilityKarma.addKarma(attacker, -2);
            else if (killed instanceof WitherEntity)
                CapabilityKarma.addKarma(attacker, 15);
            else if (killed instanceof EnderDragonEntity)
                CapabilityKarma.addKarma(attacker, 30);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        if (e.getAmount() > 0 && e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity attacker = (PlayerEntity) e.getSource().getTrueSource();
            LivingEntity attacked = e.getEntityLiving();

            if (attacked instanceof ZombiePigmanEntity && !((ZombiePigmanEntity) attacked).isAngry())
                CapabilityKarma.addKarma(attacker, -1);
            else if (attacked instanceof WolfEntity && !((WolfEntity) attacked).isAngry())
                CapabilityKarma.addKarma(attacker, -1);
            else if (attacked instanceof SpiderEntity && attacked.getBrightness() >= 0.5F)
                CapabilityKarma.addKarma(attacker, -1);
        }
    }

    @SubscribeEvent
    public static void onAnimalTame(AnimalTameEvent e) {
        e.getTamer().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> CapabilityKarma.addKarma(e.getTamer(), 1));
    }

    @SubscribeEvent
    public static void onCropTrample(BlockEvent.FarmlandTrampleEvent e) {
        if(!(e.getEntity() instanceof PlayerEntity)) return;
        e.getEntity().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> CapabilityKarma.addKarma((PlayerEntity) e.getEntity(), -1));
    }

    public static boolean isMonster(LivingEntity entity) {
        for (Biome.SpawnListEntry entry : entity.world.getBiome(entity.getPosition()).getSpawns(EntityClassification.MONSTER)) {
            if (entry.entityType == entity.getType()) {
                return true;
            }
        }

        return false;
    }

}
