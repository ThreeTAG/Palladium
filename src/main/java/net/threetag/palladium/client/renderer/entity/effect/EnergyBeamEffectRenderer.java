package net.threetag.palladium.client.renderer.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.entity.effect.EnergyBeamEffect;
import net.threetag.palladium.entity.effect.EntityEffects;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.BeamAbility;

import java.util.Objects;

public class EnergyBeamEffectRenderer implements EntityEffectRenderer<EnergyBeamEffect> {

    @SuppressWarnings("unchecked")
    @Override
    public void render(EnergyBeamEffect effect, EffectEntityRenderer.EffectEntityRenderState renderState, Entity anchor, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        if (anchor instanceof AbstractClientPlayer player) {
            var ref = getAbilityReference(renderState.extraData);
            AbilityInstance<?> instance = ref.getInstance(player);
            if (instance != null && instance.getAbility() instanceof BeamAbility) {
                var ability = (AbilityInstance<BeamAbility>) instance;
                var hitResult = ability.getAbility().updateTargetPos(player, ability, partialTicks);
                var beam = BeamManager.INSTANCE.get(ability.getAbility().beamId);

                if (beam != null) {
                    var entityPos = renderState.position;
                    var target = hitResult.getLocation();
                    beam.submitOnPlayer(player, entityPos, target, ability.getAbility().beamLengthMultiplier(ability, partialTicks), 1F, poseStack, submitNodeCollector, packedLightIn, partialTicks);
                }
            }
        }
    }

    public static AbilityReference getAbilityReference(CompoundTag tag) {
        return AbilityReference.parse(tag.getString("ability").orElse(""));
    }

    public static void start(Player player, AbilityReference abilityReference) {
        EffectEntity effectEntity = new EffectEntity(player.level(), player, EntityEffects.ENERGY_BEAM.value());
        EnergyBeamEffect.setAbilityReference(effectEntity, abilityReference);
        Objects.requireNonNull(Minecraft.getInstance().level).addEntity(effectEntity);
    }
}
