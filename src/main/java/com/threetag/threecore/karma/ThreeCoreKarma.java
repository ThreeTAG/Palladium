package com.threetag.threecore.karma;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.client.AbilityBarRenderer;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import com.threetag.threecore.karma.capability.IKarma;
import com.threetag.threecore.karma.capability.KarmaCapProvider;
import com.threetag.threecore.karma.client.KarmaBarRenderer;
import com.threetag.threecore.karma.command.KarmaCommand;
import com.threetag.threecore.karma.network.MessageKarmaInfo;
import com.threetag.threecore.karma.network.MessageSyncKarma;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
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
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;

public class ThreeCoreKarma {

    public ThreeCoreKarma() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new KarmaBarRenderer()));
    }

    public void setup(FMLCommonSetupEvent e) {

        // Network
        ThreeCore.registerMessage(MessageSyncKarma.class, MessageSyncKarma::toBytes, MessageSyncKarma::new, MessageSyncKarma::handle);
        ThreeCore.registerMessage(MessageKarmaInfo.class, MessageKarmaInfo::toBytes, MessageKarmaInfo::new, MessageKarmaInfo::handle);

        // Capability
        CapabilityManager.INSTANCE.register(IKarma.class, new Capability.IStorage<IKarma>() {
                    @Nullable
                    @Override
                    public INBTBase writeNBT(Capability<IKarma> capability, IKarma instance, EnumFacing side) {
                        return new NBTTagInt(instance.getKarma());
                    }

                    @Override
                    public void readNBT(Capability<IKarma> capability, IKarma instance, EnumFacing side, INBTBase nbt) {
                        instance.setKarma(((NBTTagInt) nbt).getInt());
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
        if (e.getObject() instanceof EntityPlayer) {
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
        if (e.getEntity() instanceof EntityPlayerMP) {
            e.getEntity().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSyncKarma(e.getEntity().getEntityId(), k.getKarma()), ((EntityPlayerMP) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) e.getSource().getTrueSource();
            EntityLivingBase killed = e.getEntityLiving();

            if (isMonster(killed))
                CapabilityKarma.addKarma(attacker, 2);
            else if (killed.getCapability(CapabilityKarma.KARMA).isPresent()) {
                killed.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
                    if (KarmaClass.fromKarma(k.getKarma()).isGood())
                        CapabilityKarma.addKarma(attacker, -2);
                    else
                        CapabilityKarma.addKarma(attacker, +2);
                });
            } else if (killed instanceof EntityVillager)
                CapabilityKarma.addKarma(attacker, -2);
            else if (killed instanceof EntityTameable && ((EntityTameable) killed).isTamed())
                CapabilityKarma.addKarma(attacker, -2);
            else if (killed instanceof EntityWither)
                CapabilityKarma.addKarma(attacker, 15);
            else if (killed instanceof EntityDragon)
                CapabilityKarma.addKarma(attacker, 30);
        }
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent e) {
        if (e.getAmount() > 0 && e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) e.getSource().getTrueSource();
            EntityLivingBase attacked = e.getEntityLiving();

            // TODO only passive pig zombies
            if (attacked instanceof EntityPigZombie)
                CapabilityKarma.addKarma(attacker, -1);
            else if (attacked instanceof EntitySpider && attacked.getBrightness() >= 0.5F)
                CapabilityKarma.addKarma(attacker, -1);
        }
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent e) {
        e.getTamer().getCapability(CapabilityKarma.KARMA).ifPresent((k) -> CapabilityKarma.addKarma(e.getTamer(), 1));
    }

    public static boolean isMonster(EntityLivingBase entity) {
        for (Biome.SpawnListEntry entry : entity.world.getBiome(entity.getPosition()).getSpawns(EnumCreatureType.MONSTER)) {
            if (entry.entityType.getEntityClass() == entity.getClass()) {
                return true;
            }
        }

        return false;
    }

}
