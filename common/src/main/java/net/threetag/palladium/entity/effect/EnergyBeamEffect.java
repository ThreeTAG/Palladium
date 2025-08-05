package net.threetag.palladium.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.energybeam.EnergyBeamManager;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.EnergyBeamAbility;

import java.util.Objects;

public class EnergyBeamEffect extends EntityEffect {

    @SuppressWarnings("unchecked")
    @Override
    @Environment(EnvType.CLIENT)
    public void render(EffectEntityRenderer.EffectEntityRenderState renderState, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        if (anchor instanceof AbstractClientPlayer player) {
            var ref = getAbilityReference(renderState.extraData);
            AbilityInstance<?> instance = ref.getInstance(player);
            if (instance != null && instance.getAbility() instanceof EnergyBeamAbility) {
                var ability = (AbilityInstance<EnergyBeamAbility>) instance;
                var hitResult = ability.getAbility().updateTargetPos(player, ability, partialTicks);
                var beam = EnergyBeamManager.INSTANCE.get(ability.getAbility().beamId);

                if (beam != null) {
                    var entityPos = renderState.position;
                    var target = hitResult.getLocation();
                    beam.render(player, entityPos, target, ability.getAbility().beamLengthMultiplier(ability, partialTicks), poseStack, bufferSource, packedLightIn, isFirstPerson, partialTicks);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Environment(EnvType.CLIENT)
    public void tick(EffectEntity entity, Entity anchor) {
        if (anchor instanceof Player player) {
            var ref = getAbilityReference(entity.getExtraData());
            AbilityInstance<?> instance = ref.getInstance(player);

            if (instance != null && instance.getAbility() instanceof EnergyBeamAbility) {
                var ability = (AbilityInstance<EnergyBeamAbility>) instance;
                var beam = EnergyBeamManager.INSTANCE.get(ability.getAbility().beamId);

                if (beam == null) {
                    this.stopPlaying(entity);
                    return;
                }

                boolean isDonePlaying = ability.getAbility().beamLengthMultiplier(ability, 0F) <= 0F
                        && !ability.isEnabled();
                if (isDonePlaying != entity.isDonePlaying()) {
                    this.stopPlaying(entity);
                }
            } else {
                this.stopPlaying(entity);
            }
        } else {
            this.stopPlaying(entity);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void start(Player player, AbilityReference abilityReference) {
        EffectEntity effectEntity = new EffectEntity(player.level(), player, EntityEffects.ENERGY_BEAM.value());
        setAbilityReference(effectEntity, abilityReference);
        Objects.requireNonNull(Minecraft.getInstance().level).addEntity(effectEntity);
    }

    public static AbilityReference getAbilityReference(CompoundTag tag) {
        return AbilityReference.parse(tag.getString("ability").orElse(""));
    }

    public static void setAbilityReference(EffectEntity entity, AbilityReference abilityReference) {
        entity.changeExtraData(compound -> compound.putString("ability", abilityReference.toString()));
    }
}
