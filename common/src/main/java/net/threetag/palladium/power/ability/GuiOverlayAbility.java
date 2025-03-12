package net.threetag.palladium.power.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.HudElementAlignment;

import java.util.List;

public class GuiOverlayAbility extends Ability {

    // TODO

    public static final MapCodec<GuiOverlayAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TextureReference.CODEC.fieldOf("texture").forGetter(ab -> ab.texture),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_width", 256).forGetter(ab -> ab.textureWidth),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_height", 256).forGetter(ab -> ab.textureHeight),
                    Vec3.CODEC.optionalFieldOf("translate", Vec3.ZERO).forGetter(ab -> ab.translate),
                    Vec3.CODEC.optionalFieldOf("rotate", Vec3.ZERO).forGetter(ab -> ab.rotate),
                    Vec3.CODEC.optionalFieldOf("scale", new Vec3(1, 1, 1)).forGetter(ab -> ab.scale),
                    HudElementAlignment.CODEC.fieldOf("alignment").forGetter(ab -> ab.alignment),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, GuiOverlayAbility::new));

    public final TextureReference texture;
    public final int textureWidth, textureHeight;
    public final Vec3 translate, rotate, scale;
    public final HudElementAlignment alignment;

    public GuiOverlayAbility(TextureReference texture, int textureWidth, int textureHeight, Vec3 translate, Vec3 rotate, Vec3 scale, HudElementAlignment alignment, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
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

    @Environment(EnvType.CLIENT)
    public static class Renderer implements LayeredDraw.Layer {

        @Override
        public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
            var minecraft = Minecraft.getInstance();
            var width = minecraft.getWindow().getGuiScaledWidth();
            var height = minecraft.getWindow().getGuiScaledHeight();

            List<AbilityInstance<GuiOverlayAbility>> abilityInstances = AbilityUtil.getEnabledInstances(minecraft.player, AbilitySerializers.GUI_OVERLAY.get()).stream().sorted((a1, a2) -> (int) (a1.getAbility().translate.z - a2.getAbility().translate.z)).toList();
            for (AbilityInstance<GuiOverlayAbility> instance : abilityInstances) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                var texture = instance.getAbility().texture.getTexture(DataContext.forAbility(minecraft.player, instance));

                var textureWidth = instance.getAbility().textureWidth;
                var textureHeight = instance.getAbility().textureHeight;
                var alignment = instance.getAbility().alignment;
                var translate = instance.getAbility().translate;
                var rotate = instance.getAbility().rotate;
                var scale = instance.getAbility().scale;

                guiGraphics.pose().pushPose();

                if (alignment.isStretched()) {
                    scale = scale.multiply(width / (float) textureWidth, height / (float) textureHeight, 1);
                }

                guiGraphics.pose().translate(translate.x + (textureWidth * scale.x) / 2F, translate.y + (textureWidth * scale.y) / 2F, translate.z);

                if (!alignment.isStretched()) {
                    var horizontal = alignment.getHorizontal();
                    var vertical = alignment.getVertical();

                    if (horizontal > 0) {
                        guiGraphics.pose().translate(horizontal == 1 ? (width - textureWidth) / 2F : width - textureWidth, 0, 0);
                    }

                    if (vertical > 0) {
                        guiGraphics.pose().translate(0, vertical == 1 ? (height - textureHeight) / 2F : height - textureHeight, 0);
                    }
                }

                if (rotate.x != 0D) {
                    guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) rotate.x));
                }

                if (rotate.y != 0D) {
                    guiGraphics.pose().mulPose(Axis.YP.rotationDegrees((float) rotate.y));
                }

                if (rotate.z != 0D) {
                    guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) rotate.z));
                }


                renderImage(guiGraphics, texture, scale, textureWidth, textureHeight);
                guiGraphics.pose().popPose();
            }
        }

        private void renderImage(GuiGraphics guiGraphics, ResourceLocation texture, Vec3 scale, int textureWidth, int textureHeight) {
            guiGraphics.pose().scale((float) scale.x, (float) scale.y, (float) scale.z);
            guiGraphics.blit(RenderType::guiTextured, texture, -textureWidth / 2, -textureHeight / 2, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        }

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
                    .addOptional("translate", TYPE_VECTOR3, "The translation of the texture.", Vec3.ZERO)
                    .addOptional("rotate", TYPE_VECTOR3, "The rotation of the texture.", Vec3.ZERO)
                    .addOptional("scale", TYPE_VECTOR3, "The scale of the texture.", new Vec3(1, 1, 1))
                    .add("alignment", SettingType.enumList(HudElementAlignment.values()), "Determines how the image is aligned on the screen.")
                    .setExampleObject(new GuiOverlayAbility(TextureReference.normal(ResourceLocation.withDefaultNamespace("textures/gui/presets/isles.png")), 256, 256, Vec3.ZERO, Vec3.ZERO, new Vec3(1, 1, 1), HudElementAlignment.TOP_LEFT, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }

}
