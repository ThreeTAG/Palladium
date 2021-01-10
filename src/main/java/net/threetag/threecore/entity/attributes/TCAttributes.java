package net.threetag.threecore.entity.attributes;

import net.minecraft.entity.ai.attributes.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.capability.ISizeChanging;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class TCAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ThreeCore.MODID);

    public static final RegistryObject<Attribute> STEP_HEIGHT = ATTRIBUTES.register("step_height", () -> new RangedAttribute("threecore.stepHeight", 0.5D, 0D, 20D).setShouldWatch(true));
    public static final RegistryObject<Attribute> FALL_RESISTANCE = ATTRIBUTES.register("fall_resistance", () -> new RangedAttribute("threecore.fallResistance", 0D, 0D, Double.MAX_VALUE));
    public static final RegistryObject<Attribute> JUMP_HEIGHT = ATTRIBUTES.register("jump_height", () -> new RangedAttribute("threecore.jumpHeight", 0D, 0D, Double.MAX_VALUE).setShouldWatch(true));
    public static final RegistryObject<Attribute> SPRINT_SPEED = ATTRIBUTES.register("sprint_speed", () -> new RangedAttribute("threecore.sprintSpeed", 0D, 0D, Double.MAX_VALUE).setShouldWatch(true));
    public static final RegistryObject<Attribute> SIZE_WIDTH = ATTRIBUTES.register("size_width", () -> new RangedAttribute("threecore.sizeWidth", 1D, 0.1D, 32D).setShouldWatch(true));
    public static final RegistryObject<Attribute> SIZE_HEIGHT = ATTRIBUTES.register("size_height", () -> new RangedAttribute("threecore.sizeHeight", 1D, 0.1D, 32D).setShouldWatch(true));
    public static float stepHeight;
    public static final UUID SPRINT_UUID = UUID.fromString("11faf62f-c271-4601-809e-83d982687b69");

    @SubscribeEvent
    public static void onFall(LivingFallEvent e) {
        ModifiableAttributeInstance fallAttribute = e.getEntityLiving().getAttribute(FALL_RESISTANCE.get());
        if (fallAttribute != null) {
            fallAttribute.setBaseValue(e.getDamageMultiplier());
            e.setDamageMultiplier((float) fallAttribute.getValue());
        }
    }

    @SubscribeEvent
    public static void onFall(LivingEvent.LivingJumpEvent e) {
        if (!e.getEntityLiving().isCrouching() && e.getEntityLiving().getAttribute(JUMP_HEIGHT.get()) != null) {
            e.getEntityLiving().setMotion(e.getEntity().getMotion().x, e.getEntity().getMotion().y + 0.1F * e.getEntityLiving().getAttribute(JUMP_HEIGHT.get()).getValue(), e.getEntity().getMotion().z);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            if (e.player.ticksExisted > 20 && e.player.world.isRemote) {
                e.player.stepHeight = stepHeight;
            }
            return;
        }

        if (e.player.ticksExisted > 20 && e.player.world.isRemote) {
            stepHeight = e.player.stepHeight;
            ModifiableAttributeInstance attributeInstance = e.player.getAttribute(STEP_HEIGHT.get());
            attributeInstance.setBaseValue(stepHeight);
            e.player.stepHeight = (float) attributeInstance.getValue();
            ISizeChanging sizeChanging = e.player.getCapability(CapabilitySizeChanging.SIZE_CHANGING).orElseGet(() -> null);
            if (sizeChanging != null)
                e.player.stepHeight *= sizeChanging.getHeight();
        }

        e.player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPRINT_UUID);
        if (e.player.isSprinting() && e.player.getAttribute(SPRINT_SPEED.get()).getModifierListCopy().size() > 0) {
            double amount = e.player.getAttribute(SPRINT_SPEED.get()).getValue();
            e.player.getAttribute(Attributes.MOVEMENT_SPEED)
                    .applyNonPersistentModifier(new AttributeModifier(SPRINT_UUID, "Sprint modifier", amount, AttributeModifier.Operation.MULTIPLY_BASE));
        }

    }

}
