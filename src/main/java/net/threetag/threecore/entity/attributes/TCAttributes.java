package net.threetag.threecore.entity.attributes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.capability.ISizeChanging;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class TCAttributes {

    public static final Attribute STEP_HEIGHT = (new RangedAttribute( "threecore.stepHeight", 0.5D, 0D, 20D)).func_233753_a_(true);
    public static final Attribute FALL_RESISTANCE = (new RangedAttribute( "threecore.fallResistance", 0D, 0D, Double.MAX_VALUE));
    public static final Attribute JUMP_HEIGHT = (new RangedAttribute( "threecore.jumpHeight", 0D, 0D, Double.MAX_VALUE)).func_233753_a_(true);
    public static final Attribute SPRINT_SPEED = (new RangedAttribute( "threecore.sprintSpeed", 0D, 0D, Double.MAX_VALUE)).func_233753_a_(true);
    public static final Attribute SIZE_WIDTH = (new RangedAttribute( "threecore.sizeWidth", 1D, 0.1D, 32D)).func_233753_a_(true);
    public static final Attribute SIZE_HEIGHT = (new RangedAttribute( "threecore.sizeHeight", 1D, 0.1D, 32D)).func_233753_a_(true);
    public static float stepHeight;
    public static final UUID SPRINT_UUID = UUID.fromString("11faf62f-c271-4601-809e-83d982687b69");


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
                    newMap.func_233815_a_(STEP_HEIGHT, 1D);
                    newMap.func_233814_a_(FALL_RESISTANCE);
                    newMap.func_233814_a_(JUMP_HEIGHT);
                    newMap.func_233815_a_(SPRINT_SPEED, 1D);
                    GlobalEntityTypeAttributes.put((EntityType<? extends LivingEntity>) value, newMap.func_233813_a_());
                }
        });
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent e) {
        ModifiableAttributeInstance fallAttribute = e.getEntityLiving().getAttribute(FALL_RESISTANCE);
        if (fallAttribute != null) {
            fallAttribute.setBaseValue(e.getDamageMultiplier());
            e.setDamageMultiplier((float) fallAttribute.getValue());
        }
    }

    @SubscribeEvent
    public static void onFall(LivingEvent.LivingJumpEvent e) {
        if (!e.getEntityLiving().isCrouching()) {
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
            ModifiableAttributeInstance attributeInstance = e.player.getAttribute(STEP_HEIGHT);
            attributeInstance.setBaseValue(stepHeight);
            e.player.stepHeight = (float) attributeInstance.getValue();
            ISizeChanging sizeChanging = e.player.getCapability(CapabilitySizeChanging.SIZE_CHANGING).orElseGet(() -> null);
            if(sizeChanging != null)
                e.player.stepHeight *= sizeChanging.getHeight();
        }

        e.player.getAttribute(Attributes.field_233821_d_).removeModifier(SPRINT_UUID);
        if (e.player.isSprinting() && e.player.getAttribute(SPRINT_SPEED).func_225505_c_().size() > 0) {
            double amount = e.player.getAttribute(SPRINT_SPEED).getValue();
            e.player.getAttribute(Attributes.field_233821_d_).func_233769_c_(new AttributeModifier(SPRINT_UUID, "Sprint modifier", amount, AttributeModifier.Operation.MULTIPLY_BASE));
        }

    }

}
