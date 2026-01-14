package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class ShaderEffectAbility extends Ability {

    public static final MapCodec<ShaderEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("shader").forGetter(ab -> ab.shader),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ShaderEffectAbility::new));

    public final Identifier shader;

    public ShaderEffectAbility(Identifier shader, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.shader = shader;
    }

    @Override
    public AbilitySerializer<ShaderEffectAbility> getSerializer() {
        return AbilitySerializers.SHADER_EFFECT.get();
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> entry) {
        Palladium.PROXY.applyShader(entity, this.shader);
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> entry) {
        Palladium.PROXY.removeShader(entity, this.shader);
    }

    public static Identifier get(Player player) {
        for (AbilityInstance<ShaderEffectAbility> instance : AbilityUtil.getEnabledInstances(player, AbilitySerializers.SHADER_EFFECT.get())) {
            return instance.getAbility().shader;
        }
        return null;
    }

    public static class Serializer extends AbilitySerializer<ShaderEffectAbility> {

        @Override
        public MapCodec<ShaderEffectAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ShaderEffectAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Applies a shader effect to the player.")
                    .add("shader", TYPE_IDENTIFIER, "The ID of the shader that shall be applied.")
                    .setExampleObject(new ShaderEffectAbility(Identifier.withDefaultNamespace("creeper"), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
