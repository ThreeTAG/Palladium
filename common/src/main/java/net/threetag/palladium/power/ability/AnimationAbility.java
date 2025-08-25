package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;

public class AnimationAbility extends Ability {

    public static final MapCodec<AnimationAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.listOrPrimitive(ResourceLocation.CODEC).fieldOf("animations").forGetter(a -> a.animations),
            propertiesCodec(), stateCodec(), energyBarUsagesCodec()
    ).apply(instance, AnimationAbility::new));

    public final List<ResourceLocation> animations;

    public AnimationAbility(List<ResourceLocation> animations, AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
        this.animations = animations;
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.ANIMATION.get();
    }

    public static class Serializer extends AbilitySerializer<AnimationAbility> {

        @Override
        public MapCodec<AnimationAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, AnimationAbility> builder, HolderLookup.Provider provider) {
            builder.setName("Animation")
                    .setDescription("Applies animations to the entity. Animations must be defined in 'assets/<namespace>/palladium/animations'")
                    .add("animations", SettingType.listOrPrimitive(TYPE_RESOURCE_LOCATION), "ID(s) of the animations")
                    .setExampleObject(new AnimationAbility(Collections.singletonList(ResourceLocation.parse("test:animation_id")), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
