package net.threetag.palladium.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.EnergyBlastAbility;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.awt.*;

public class EnergyBlastEffect extends EntityEffect {

    public static final PalladiumProperty<ResourceLocation> POWER_ID = new ResourceLocationProperty("power_id");
    public static final PalladiumProperty<String> ABILITY_ID = new StringProperty("ability_id");

    @Override
    public void registerProperties(PropertyManager manager) {
        super.registerProperties(manager);
        manager.register(POWER_ID, null);
        manager.register(ABILITY_ID, null);
    }

    @Override
    public void render(EffectEntity entity, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        AbilityEntry entry = this.getAbility(entity, anchor);

        if (entry == null || !isFirstPerson) {
            return;
        }

        int timer = entry.getOwnProperty(EnergyBlastAbility.ANIMATION_TIMER);

        if (timer > 0) {
            double distance = entry.getOwnProperty(EnergyBlastAbility.DISTANCE) * (timer / 5F);
            Color origColor = entry.getProperty(EnergyBlastAbility.COLOR);
            Color color = new Color(origColor.getRed(), origColor.getGreen(), origColor.getBlue(), (int) (timer / 5F * 255F));
            var builder = bufferSource.getBuffer(PalladiumRenderTypes.LASER);

            poseStack.pushPose();
            entry.getProperty(EnergyBlastAbility.ORIGIN).render(poseStack, builder, (LivingEntity) anchor, color, null, distance, partialTicks);
            poseStack.popPose();
        }
    }

    @Override
    public void tick(EffectEntity entity, Entity anchor) {
        AbilityEntry entry = this.getAbility(entity, anchor);
        boolean isDonePlaying = entry == null || (entry.getOwnProperty(EnergyBlastAbility.ANIMATION_TIMER) <= 0F && !entry.isEnabled());
        if (isDonePlaying != this.get(entity, IS_DONE_PLAYING)) {
            this.set(entity, IS_DONE_PLAYING, isDonePlaying);
        }
    }

    public AbilityEntry getAbility(EffectEntity entity, Entity anchor) {
        ResourceLocation powerId = this.get(entity, POWER_ID);
        String abilityId = this.get(entity, ABILITY_ID);

        if (powerId != null && abilityId != null && anchor instanceof LivingEntity livingEntity) {
            Power power = PowerManager.getInstance(anchor.level).getPower(this.get(entity, POWER_ID));

            if (power != null) {
                IPowerHolder holder = PowerManager.getPowerHandler(livingEntity).getPowerHolder(power);

                if (holder != null) {
                    return holder.getAbilities().get(abilityId);
                }
            }
        }
        return null;
    }

    public static void start(Entity anchor, Power power, AbilityEntry entry) {
        if (!anchor.level.isClientSide) {
            EffectEntity effectEntity = new EffectEntity(anchor.level, anchor, EntityEffects.ENERGY_BLAST.get());
            ABILITY_ID.set(effectEntity, entry.getConfiguration().getId());
            POWER_ID.set(effectEntity, power.getId());
            anchor.level.addFreshEntity(effectEntity);
        }
    }
}
