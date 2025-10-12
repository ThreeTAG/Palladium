package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.client.util.ScreenElementAlignment;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class GuiOverlayAbility extends Ability {

    public static final MapCodec<GuiOverlayAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TextureReference.CODEC.fieldOf("texture").forGetter(ab -> ab.texture),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_width", 256).forGetter(ab -> ab.textureWidth),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_height", 256).forGetter(ab -> ab.textureHeight),
                    Vec2.CODEC.optionalFieldOf("translate", Vec2.ZERO).forGetter(ab -> ab.translate),
                    Codec.FLOAT.optionalFieldOf("rotate", 0F).forGetter(ab -> ab.rotate),
                    Vec2.CODEC.optionalFieldOf("scale", Vec2.ONE).forGetter(ab -> ab.scale),
                    ScreenElementAlignment.CODEC.fieldOf("alignment").forGetter(ab -> ab.alignment),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, GuiOverlayAbility::new));

    public final TextureReference texture;
    public final int textureWidth, textureHeight;
    public final Vec2 translate;
    public final float rotate;
    public final Vec2 scale;
    public final ScreenElementAlignment alignment;

    public GuiOverlayAbility(TextureReference texture, int textureWidth, int textureHeight, Vec2 translate, float rotate, Vec2 scale, ScreenElementAlignment alignment, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.translate = translate;
        this.rotate = rotate;
        this.scale = scale;
        this.alignment = alignment;
    }

    @Override
    public AbilitySerializer<GuiOverlayAbility> getSerializer() {
        return AbilitySerializers.GUI_OVERLAY.get();
    }

    public static class Serializer extends AbilitySerializer<GuiOverlayAbility> {

        @Override
        public MapCodec<GuiOverlayAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, GuiOverlayAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Renders an image on the screen.")
                    .add("texture", TYPE_TEXTURE_REFERENCE, "The texture to render.")
                    .addOptional("texture_width", TYPE_INT, "The width of the texture.", 256)
                    .addOptional("texture_height", TYPE_INT, "The height of the texture.", 256)
                    .addOptional("translate", TYPE_VECTOR2, "The translation of the texture.", Vec2.ZERO)
                    .addOptional("rotate", TYPE_FLOAT, "The rotation of the texture.", 0F)
                    .addOptional("scale", TYPE_VECTOR2, "The scale of the texture.", Vec2.ONE)
                    .add("alignment", SettingType.enumList(ScreenElementAlignment.values()), "Determines how the image is aligned on the screen.")
                    .setExampleObject(new GuiOverlayAbility(TextureReference.normal(ResourceLocation.withDefaultNamespace("textures/gui/presets/isles.png")), 256, 256, Vec2.ZERO, 0, Vec2.ONE, ScreenElementAlignment.TOP_LEFT, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }

}
