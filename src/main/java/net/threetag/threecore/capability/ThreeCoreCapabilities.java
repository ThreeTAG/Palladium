package net.threetag.threecore.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.karma.IKarma;
import net.threetag.threecore.network.SyncAccessoiresMessage;
import net.threetag.threecore.util.threedata.IThreeDataHolder;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class ThreeCoreCapabilities {

    public static void init() {
        // Abilities
        CapabilityManager.INSTANCE.register(IAbilityContainer.class, new Capability.IStorage<IAbilityContainer>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IAbilityContainer> capability, IAbilityContainer instance, Direction direction) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IAbilityContainer> capability, IAbilityContainer instance, Direction direction, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new ItemAbilityContainer(ItemStack.EMPTY));

        // Multi Abilities
        CapabilityManager.INSTANCE.register(IMultiAbilityContainer.class, new Capability.IStorage<IMultiAbilityContainer>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IMultiAbilityContainer> capability, IMultiAbilityContainer instance, Direction direction) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IMultiAbilityContainer> capability, IMultiAbilityContainer instance, Direction direction, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                CapabilityAbilityContainer::new);


        // Karma
        CapabilityManager.INSTANCE.register(IKarma.class, new Capability.IStorage<IKarma>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IKarma> capability, IKarma instance, Direction side) {
                        return IntNBT.valueOf(instance.getKarma());
                    }

                    @Override
                    public void readNBT(Capability<IKarma> capability, IKarma instance, Direction side, INBT nbt) {
                        instance.setKarma(((IntNBT) nbt).getInt());
                    }
                },
                CapabilityKarma::new);


        // Size Changing
        CapabilityManager.INSTANCE.register(ISizeChanging.class, new Capability.IStorage<ISizeChanging>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<ISizeChanging> capability, ISizeChanging instance, Direction side) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");

                    }

                    @Override
                    public void readNBT(Capability<ISizeChanging> capability, ISizeChanging instance, Direction side, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new CapabilitySizeChanging(null));


        // ThreeData
        CapabilityManager.INSTANCE.register(IThreeDataHolder.class, new Capability.IStorage<IThreeDataHolder>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IThreeDataHolder> capability, IThreeDataHolder instance, Direction direction) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IThreeDataHolder> capability, IThreeDataHolder instance, Direction direction, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new CapabilityThreeData(null));

        // Accessoires
        CapabilityManager.INSTANCE.register(IAccessoireHolder.class, new Capability.IStorage<IAccessoireHolder>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IAccessoireHolder> capability, IAccessoireHolder instance, Direction side) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IAccessoireHolder> capability, IAccessoireHolder instance, Direction side, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                CapabilityAccessoires::new);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {

        if (e.getObject() instanceof LivingEntity && !e.getObject().getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "multi_ability_container"), new MultiAbilityContainerProvider(new CapabilityAbilityContainer()));
        }

        if (e.getObject() instanceof PlayerEntity && !e.getObject().getCapability(CapabilityKarma.KARMA).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "karma"), new KarmaCapProvider());
        }

        if (canSizeChange(e.getObject()) && !e.getObject().getCapability(CapabilitySizeChanging.SIZE_CHANGING).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "size_changing"), new SizeChangingProvider(new CapabilitySizeChanging(e.getObject())));
        }

        if (!e.getObject().getCapability(CapabilityThreeData.THREE_DATA).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "three_data"), new ThreeDataProvider(new CapabilityThreeData(e.getObject())));
        }

        if (e.getObject() instanceof PlayerEntity && !e.getObject().getCapability(CapabilityAccessoires.ACCESSOIRES).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "accessoires"), new AccessoireCapProvider());
        }
    }

    public static boolean canSizeChange(Entity entity) {
        return !(entity instanceof HangingEntity) && !(entity instanceof ShulkerEntity) && !(entity instanceof EnderCrystalEntity);
    }

    // Accessoir events ------------------------------------------------------------------------------------------------

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
            if (accessoireHolder instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncAccessoiresMessage(e.getTarget().getEntityId(), accessoireHolder.getActiveAccessoires()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        e.getEntity().getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
            if (e.getEntity() instanceof ServerPlayerEntity)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncAccessoiresMessage(e.getEntity().getEntityId(), accessoireHolder.getActiveAccessoires()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

}
