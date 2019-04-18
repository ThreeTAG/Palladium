package com.threetag.threecore.abilities;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.capability.AbilityEventHandler;
import com.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import com.threetag.threecore.abilities.client.AbilityBarRenderer;
import com.threetag.threecore.abilities.client.AbilityKeyHandler;
import com.threetag.threecore.abilities.command.SuperpowerCommand;
import com.threetag.threecore.abilities.network.*;
import com.threetag.threecore.abilities.superpower.SuperpowerManager;
import com.threetag.threecore.karma.ThreeCoreKarma;
import com.threetag.threecore.karma.command.KarmaCommand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.INBTBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

public class ThreeCoreAbilities {

    public ThreeCoreAbilities() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new AbilityEventHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityKeyHandler());
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new AbilityBarRenderer()));

        AbilityHelper.registerAbilityContainer(CapabilityAbilityContainer.ID, (p) -> p.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));
        for (EntityEquipmentSlot slots : EntityEquipmentSlot.values())
            AbilityHelper.registerAbilityContainer(new ResourceLocation(ThreeCore.MODID, "item_" + slots.getName()), (p) -> p.getItemStackFromSlot(slots).getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));
    }

    public void setup(FMLCommonSetupEvent e) {
        // Network
        ThreeCore.registerMessage(MessageSendPlayerAbilityContainer.class, MessageSendPlayerAbilityContainer::toBytes, MessageSendPlayerAbilityContainer::new, MessageSendPlayerAbilityContainer::handle);
        ThreeCore.registerMessage(MessageUpdateAbility.class, MessageUpdateAbility::toBytes, MessageUpdateAbility::new, MessageUpdateAbility::handle);
        ThreeCore.registerMessage(MessageAddAbility.class, MessageAddAbility::toBytes, MessageAddAbility::new, MessageAddAbility::handle);
        ThreeCore.registerMessage(MessageRemoveAbility.class, MessageRemoveAbility::toBytes, MessageRemoveAbility::new, MessageRemoveAbility::handle);
        ThreeCore.registerMessage(MessageAbilityKey.class, MessageAbilityKey::toBytes, MessageAbilityKey::new, MessageAbilityKey::handle);

        // Capability
        CapabilityManager.INSTANCE.register(IAbilityContainer.class, new Capability.IStorage<IAbilityContainer>() {
                    @Nullable
                    @Override
                    public INBTBase writeNBT(Capability<IAbilityContainer> capability, IAbilityContainer instance, EnumFacing side) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IAbilityContainer> capability, IAbilityContainer instance, EnumFacing side, INBTBase nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new CapabilityAbilityContainer());
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        SuperpowerCommand.register(e.getCommandDispatcher());
        e.getServer().getResourceManager().addReloadListener(new SuperpowerManager());
    }
}
