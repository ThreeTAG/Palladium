package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.SkinTypedTextureReferenceProperty;

public class SkinChangeAbility extends Ability {

    public static final PalladiumProperty<SkinTypedValue<TextureReference>> TEXTURE = new SkinTypedTextureReferenceProperty("texture").configurable("Texture for the skin change");
    public static final PalladiumProperty<Integer> PRIORITY = new IntegerProperty("priority").configurable("Priority for the skin (in case multiple skin changes are applied, the one with the highest priority will be used)");

    public SkinChangeAbility() {
        this.withProperty(TEXTURE, new SkinTypedValue<>(TextureReference.normal(new ResourceLocation("textures/entity/zombie/drowned.png"))));
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
}
