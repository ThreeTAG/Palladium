package com.threetag.threecore.abilities;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.capability.AbilityEventHandler;
import com.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import com.threetag.threecore.abilities.client.AbilityClientHandler;
import com.threetag.threecore.abilities.client.renderer.AbilityBarRenderer;
import com.threetag.threecore.abilities.command.SuperpowerCommand;
import com.threetag.threecore.abilities.condition.ConditionType;
import com.threetag.threecore.abilities.network.*;
import com.threetag.threecore.abilities.superpower.SuperpowerManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;
import java.io.File;

public class ThreeCoreAbilities {

    public ThreeCoreAbilities() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(new AbilityEventHandler());
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new AbilityBarRenderer());
            MinecraftForge.EVENT_BUS.register(new AbilityClientHandler());
        });

        AbilityHelper.registerAbilityContainer(CapabilityAbilityContainer.ID, (p) -> p.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));
        for (EquipmentSlotType slots : EquipmentSlotType.values())
            AbilityHelper.registerAbilityContainer(new ResourceLocation(ThreeCore.MODID, "item_" + slots.getName()), (p) -> p.getItemStackFromSlot(slots).getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));
    }

    public void setup(FMLCommonSetupEvent e) {
        // Network
        ThreeCore.registerMessage(SendPlayerAbilityContainerMessage.class, SendPlayerAbilityContainerMessage::toBytes, SendPlayerAbilityContainerMessage::new, SendPlayerAbilityContainerMessage::handle);
        ThreeCore.registerMessage(UpdateAbilityMessage.class, UpdateAbilityMessage::toBytes, UpdateAbilityMessage::new, UpdateAbilityMessage::handle);
        ThreeCore.registerMessage(AddAbilityMessage.class, AddAbilityMessage::toBytes, AddAbilityMessage::new, AddAbilityMessage::handle);
        ThreeCore.registerMessage(RemoveAbilityMessage.class, RemoveAbilityMessage::toBytes, RemoveAbilityMessage::new, RemoveAbilityMessage::handle);
        ThreeCore.registerMessage(AbilityKeyMessage.class, AbilityKeyMessage::toBytes, AbilityKeyMessage::new, AbilityKeyMessage::handle);
        ThreeCore.registerMessage(SendSuperpowerToastMessage.class, SendSuperpowerToastMessage::toBytes, SendSuperpowerToastMessage::new, SendSuperpowerToastMessage::handle);
        ThreeCore.registerMessage(BuyConditionMessage.class, BuyConditionMessage::toBytes, BuyConditionMessage::new, BuyConditionMessage::handle);

        // Capability
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
                () -> new CapabilityAbilityContainer());
    }

    public void loadComplete(FMLLoadCompleteEvent e) {
        // abilities.html
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            AbilityType.generateHtmlFile(new File("abilities.html"));
            ConditionType.generateHtmlFile(new File("conditions.html"));
        });
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        SuperpowerCommand.register(e.getCommandDispatcher());
        e.getServer().getResourceManager().addReloadListener(new SuperpowerManager());
    }
}