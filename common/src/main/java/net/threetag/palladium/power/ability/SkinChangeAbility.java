package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.renderer.entity.PlayerSkinHandler;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.PlayerModelChangeType;
import net.threetag.palladium.entity.SkinTypedValue;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class SkinChangeAbility extends Ability {

    public static final MapCodec<SkinChangeAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    SkinTypedValue.codec(TextureReference.CODEC).fieldOf("texture").forGetter(ab -> ab.texture),
                    PlayerModelChangeType.CODEC.optionalFieldOf("model_type", PlayerModelChangeType.KEEP).forGetter(ab -> ab.modelChangeType),
                    Codec.INT.optionalFieldOf("priority", 50).forGetter(ab -> ab.priority),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, SkinChangeAbility::new));

    public final SkinTypedValue<TextureReference> texture;
    public final PlayerModelChangeType modelChangeType;
    public final int priority;

    public SkinChangeAbility(SkinTypedValue<TextureReference> texture, PlayerModelChangeType modelChangeType, int priority, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.texture = texture;
        this.modelChangeType = modelChangeType;
        this.priority = priority;
    }

    @Override
    public AbilitySerializer<SkinChangeAbility> getSerializer() {
        return AbilitySerializers.SKIN_CHANGE.get();
    }

    @Environment(EnvType.CLIENT)
    public static class SkinProvider implements PlayerSkinHandler.SkinProvider {

        @Override
        public ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin, PlayerSkin.Model model) {
            var abilities = AbilityUtil.getEnabledInstances(player, AbilitySerializers.SKIN_CHANGE.get()).stream().sorted((a1, a2) -> a2.getAbility().priority - a1.getAbility().priority).toList();

            if (!abilities.isEmpty()) {
                var ability = abilities.getFirst();
                return ability.getAbility().texture.get(model == PlayerSkin.Model.SLIM).getTexture(DataContext.forAbility(player, ability));
            }

            return previousSkin;
        }

        @Override
        public PlayerModelChangeType getModelType(AbstractClientPlayer player) {
            var abilities = AbilityUtil.getEnabledInstances(player, AbilitySerializers.SKIN_CHANGE.get()).stream().sorted((a1, a2) -> a2.getAbility().priority - a1.getAbility().priority).toList();

            if (!abilities.isEmpty()) {
                var ability = abilities.getFirst();
                return ability.getAbility().modelChangeType;
            }

            return PlayerSkinHandler.SkinProvider.super.getModelType(player);
        }
    }

    public static class Serializer extends AbilitySerializer<SkinChangeAbility> {

        @Override
        public MapCodec<SkinChangeAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, SkinChangeAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that changes the player's skin.")
                    .add("texture", TYPE_TEXTURE_REFERENCE, "The texture that should be used for the player's skin.")
                    .addOptional("model_type", SettingType.enumList(PlayerModelChangeType.values()), "Model type for the player. 'wide' = Wide-armed Steve model; 'slim' = Slim-armed Alex model; 'keep' = Does not change the player's default model", PlayerModelChangeType.KEEP)
                    .addOptional("priority", TYPE_INT, "Priority for the skin (in case multiple skin changes are applied, the one with the highest priority will be used)", 50)
                    .setExampleObject(new SkinChangeAbility(new SkinTypedValue<>(TextureReference.normal(ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png"))), PlayerModelChangeType.WIDE, 50, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }

}
