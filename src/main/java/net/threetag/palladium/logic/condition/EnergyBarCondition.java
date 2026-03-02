package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.energybar.EnergyBarInstance;
import net.threetag.palladium.power.energybar.EnergyBarReference;

public record EnergyBarCondition(EnergyBarReference energyBar, int min, int max) implements Condition {

    public static final MapCodec<EnergyBarCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(EnergyBarCondition::energyBar),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(EnergyBarCondition::min),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(EnergyBarCondition::max)
            ).apply(instance, EnergyBarCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyBarCondition> STREAM_CODEC = StreamCodec.composite(
            EnergyBarReference.STREAM_CODEC, EnergyBarCondition::energyBar,
            ByteBufCodecs.VAR_INT, EnergyBarCondition::min,
            ByteBufCodecs.VAR_INT, EnergyBarCondition::max,
            EnergyBarCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        EnergyBarInstance energyBarInstance = this.energyBar.getBar(entity, context.getPowerInstance());

        if (energyBarInstance == null) {
            return false;
        } else {
            return this.min <= energyBarInstance.get() && energyBarInstance.get() <= this.max;
        }
    }

    @Override
    public ConditionSerializer<EnergyBarCondition> getSerializer() {
        return ConditionSerializers.ENERGY_BAR.get();
    }

    public static class Serializer extends ConditionSerializer<EnergyBarCondition> {

        @Override
        public MapCodec<EnergyBarCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, EnergyBarCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Energy Bar")
                    .setDescription("Checks if an energy bar has a required amount in it.")
                    .add("energy_bar", TYPE_ENERGY_BAR_REFERENCE, "The energy bar is being looked into.")
                    .addOptional("min", TYPE_NON_NEGATIVE_INT, "The minimum required value of the energy bar.")
                    .addOptional("max", TYPE_NON_NEGATIVE_INT, "The maximum required value of the energy bar.")
                    .addExampleObject(new EnergyBarCondition(new EnergyBarReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "energy_bar_key"
                    ), 5, 10));
        }
    }
}
