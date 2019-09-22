package net.threetag.threecore.util.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.sizechanging.capability.ISizeChanging;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class TCAttributes {

    public static final IAttribute STEP_HEIGHT = (new RangedAttribute((IAttribute) null, "threecore.stepHeight", 0.5D, 0D, 20D)).setDescription("Step Height").setShouldWatch(true);
    public static final IAttribute FALL_RESISTANCE = (new RangedAttribute((IAttribute) null, "threecore.fallResistance", 0D, 0D, Double.MAX_VALUE)).setDescription("Fall Resistance");
    public static final IAttribute JUMP_HEIGHT = (new RangedAttribute((IAttribute) null, "threecore.jumpHeight", 0D, 0D, Double.MAX_VALUE)).setDescription("Jump Height").setShouldWatch(true);
    public static final IAttribute SPRINT_SPEED = (new RangedAttribute((IAttribute) null, "threecore.sprintSpeed", 0D, 0D, Double.MAX_VALUE)).setDescription("Sprint Speed").setShouldWatch(true);
    public static float stepHeight;
    public static final UUID SPRINT_UUID = UUID.fromString("11faf62f-c271-4601-809e-83d982687b69");

    @SubscribeEvent
    public static void onEntityConstruct(EntityEvent.EntityConstructing e) {
        if (e.getEntity() instanceof LivingEntity) {
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(STEP_HEIGHT).setBaseValue(1D);
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(FALL_RESISTANCE);
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(JUMP_HEIGHT);
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(SPRINT_SPEED).setBaseValue(1D);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent e) {
        IAttributeInstance attributeInstance = e.getEntityLiving().getAttribute(FALL_RESISTANCE);
        attributeInstance.setBaseValue(e.getDamageMultiplier());
        e.setDamageMultiplier((float) attributeInstance.getValue());
    }

    @SubscribeEvent
    public static void onFall(LivingEvent.LivingJumpEvent e) {
        if (!e.getEntityLiving().isSneaking()) {
            e.getEntityLiving().setMotion(e.getEntity().getMotion().x, e.getEntity().getMotion().y + 0.1F * e.getEntityLiving().getAttribute(JUMP_HEIGHT).getValue(), e.getEntity().getMotion().z);
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
            IAttributeInstance attributeInstance = e.player.getAttribute(STEP_HEIGHT);
            attributeInstance.setBaseValue(stepHeight);
            e.player.stepHeight = (float) attributeInstance.getValue();
            ISizeChanging sizeChanging = e.player.getCapability(CapabilitySizeChanging.SIZE_CHANGING).orElseGet(() -> null);
            if(sizeChanging != null)
                e.player.stepHeight *= sizeChanging.getHeight();
        }

        e.player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPRINT_UUID);
        if (e.player.isSprinting() && e.player.getAttribute(SPRINT_SPEED).getModifiers().size() > 0) {
            double amount = e.player.getAttribute(SPRINT_SPEED).getValue();
            e.player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(SPRINT_UUID, "Sprint modifier", amount, AttributeModifier.Operation.MULTIPLY_BASE));
        }

    }

}
