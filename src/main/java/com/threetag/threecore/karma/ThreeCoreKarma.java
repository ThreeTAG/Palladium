package com.threetag.threecore.karma;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import com.threetag.threecore.karma.capability.IKarma;
import com.threetag.threecore.karma.capability.KarmaCapProvider;
import com.threetag.threecore.karma.client.KarmaBarRenderer;
import com.threetag.threecore.karma.command.KarmaCommand;
import com.threetag.threecore.karma.network.KarmaInfoMessage;
import com.threetag.threecore.karma.network.SyncKarmaMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;

public class ThreeCoreKarma {

    public ThreeCoreKarma() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new KarmaBarRenderer());
    }

    public void setup(FMLCommonSetupEvent e) {

        // Network
        ThreeCore.registerMessage(SyncKarmaMessage.class, SyncKarmaMessage::toBytes, SyncKarmaMessage::new, SyncKarmaMessage::handle);
        ThreeCore.registerMessage(KarmaInfoMessage.class, KarmaInfoMessage::toBytes, KarmaInfoMessage::new, KarmaInfoMessage::handle);

        // Capability
        CapabilityManager.INSTANCE.register(IKarma.class, new Capability.IStorage<IKarma>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IKarma> capability, IKarma instance, Direction side) {
                        return new IntNBT(instance.getKarma());
                    }

                    @Override
                    public void readNBT(Capability<IKarma> capability, IKarma instance, Direction side, INBT nbt) {
                        instance.setKarma(((IntNBT) nbt).getInt());
                    }
                },
                () -> new CapabilityKarma());
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        KarmaCommand.register(e.getCommandDispatcher());
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof PlayerEntity) {
            if (!e.getObject().getCapability(CapabilityKarma.KARMA).isPresent()) {
                e.addCapability(new ResourceLocation(ThreeCore.MODID, "karma"), new KarmaCapProvider());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone e) {
        if (e.isWasDeath()) {
            e.getOriginal().getCapability(CapabilityKarma.KARMA).ifPresent(oldCap -> e.getEntityPlayer().getCapability(CapabilityKarma.KARMA).ifPresent(newCap -> newCap.setKarma(oldCap.getKarma())));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof ServerPlayerEntity) {
            e.getEntity().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> ThreeCore.NETWORK_CHANNEL.sendTo(new SyncKarmaMessage(e.getEntity().getEntityId(), k.getKarma()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
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
    public void onDamage(LivingHurtEvent e) {
        if (e.getAmount() > 0 && e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity attacker = (PlayerEntity) e.getSource().getTrueSource();
            LivingEntity attacked = e.getEntityLiving();

            // TODO only passive pig zombies
            if (attacked instanceof ZombiePigmanEntity)
                CapabilityKarma.addKarma(attacker, -1);
            else if (attacked instanceof SpiderEntity && attacked.getBrightness() >= 0.5F)
                CapabilityKarma.addKarma(attacker, -1);
        }
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent e) {
        e.getTamer().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> CapabilityKarma.addKarma(e.getTamer(), 1));
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
