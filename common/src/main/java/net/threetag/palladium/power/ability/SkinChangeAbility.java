package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.client.renderer.entity.PlayerSkinHandler;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.ChangedPlayerModelTypeProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.SkinTypedTextureReferenceProperty;

public class SkinChangeAbility extends Ability {

    public static final PalladiumProperty<SkinTypedValue<TextureReference>> TEXTURE = new SkinTypedTextureReferenceProperty("texture").configurable("Texture for the skin change");
    public static final PalladiumProperty<ChangedPlayerModelTypeProperty.ChangedModelType> MODEL_TYPE = new ChangedPlayerModelTypeProperty("model_type").configurable("Model type for the player. 'normal' = Wide-armed Steve model; 'slim' = Slim-armed Alex model; 'keep' = Does not change the player's default model");
    public static final PalladiumProperty<Integer> PRIORITY = new IntegerProperty("priority").configurable("Priority for the skin (in case multiple skin changes are applied, the one with the highest priority will be used)");

    public SkinChangeAbility() {
        this.withProperty(TEXTURE, new SkinTypedValue<>(TextureReference.normal(new ResourceLocation("textures/entity/zombie/drowned.png"))));
        this.withProperty(MODEL_TYPE, ChangedPlayerModelTypeProperty.ChangedModelType.KEEP);
        this.withProperty(PRIORITY, 50);
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows you to change a player's skin.";
    }

    @Environment(EnvType.CLIENT)
    public static class SkinProvider implements PlayerSkinHandler.ISkinProvider {

        @Override
        public ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin) {
            var abilities = AbilityUtil.getEnabledEntries(player, Abilities.SKIN_CHANGE.get()).stream().filter(AbilityEntry::isEnabled).sorted((a1, a2) -> a2.getProperty(SkinChangeAbility.PRIORITY) - a1.getProperty(SkinChangeAbility.PRIORITY)).toList();

            if (!abilities.isEmpty()) {
                var ability = abilities.get(0);
                return ability.getProperty(SkinChangeAbility.TEXTURE).get(player).getTexture(DataContext.forAbility(player, ability));
            }

            return previousSkin;
        }

        @Override
        public ChangedPlayerModelTypeProperty.ChangedModelType getModelType(AbstractClientPlayer player) {
            var abilities = AbilityUtil.getEnabledEntries(player, Abilities.SKIN_CHANGE.get()).stream().filter(AbilityEntry::isEnabled).sorted((a1, a2) -> a2.getProperty(SkinChangeAbility.PRIORITY) - a1.getProperty(SkinChangeAbility.PRIORITY)).toList();

            if (!abilities.isEmpty()) {
                var ability = abilities.get(0);
                return ability.getProperty(SkinChangeAbility.MODEL_TYPE);
            }

            return PlayerSkinHandler.ISkinProvider.super.getModelType(player);
        }
    }

}
