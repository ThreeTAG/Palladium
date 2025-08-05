package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.ArmSetting;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class AimAbility extends Ability {

    public static final MapCodec<AimAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ArmSetting.CODEC.optionalFieldOf("arm", ArmSetting.MAIN_ARM).forGetter(ab -> ab.arm),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, AimAbility::new));

    public final ArmSetting arm;

    public AimAbility(ArmSetting arm, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.arm = arm;
    }

    public static Float[] getTimer(LivingEntity entity, float partialTicks) {
        Float[] f = new Float[]{0F, 0F};

        for (AbilityInstance<AimAbility> instance : AbilityUtil.getInstances(entity, AbilitySerializers.AIM.get())) {
            var armType = instance.getAbility().arm;
            var progress = instance.getAnimationTimerValueEased(partialTicks);

            if (!armType.isNone()) {
                if (armType.isRight(entity)) {
                    f[1] = Math.max(f[1], progress);
                }

                if (armType.isLeft(entity)) {
                    f[0] = Math.max(f[0], progress);
                }
            }
        }

        return f;
    }

    @Override
    public AbilitySerializer<AimAbility> getSerializer() {
        return AbilitySerializers.AIM.get();
    }

    public static class Serializer extends AbilitySerializer<AimAbility> {

        @Override
        public MapCodec<AimAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, AimAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the player to aim their arms.")
                    .addOptional("arm", SettingType.enumList(ArmSetting.values()), "The arm(s) that should point.", ArmSetting.MAIN_ARM)
                    .setExampleObject(new AimAbility(ArmSetting.MAIN_ARM, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
