package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.renderer.entity.layer.pack.CompoundPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerDisplayUiComponent extends RenderableUiComponent {

    public static final MapCodec<PlayerDisplayUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("scale", 30F).forGetter(c -> c.scale),
            Codec.BOOL.optionalFieldOf("follow_mouse", false).forGetter(c -> c.followMouse),
            Codec.BOOL.optionalFieldOf("mimic_player", true).forGetter(c -> c.mimicPlayer),
            Vec3.CODEC.optionalFieldOf("rotation", Vec3.ZERO).forGetter(c -> c.rotation),
            PalladiumCodecs.listOrPrimitive(Identifier.CODEC).optionalFieldOf("render_layers", Collections.emptyList()).forGetter(c -> c.renderLayers),
            propertiesCodec()
    ).apply(instance, PlayerDisplayUiComponent::new));

    private final float scale;
    private final boolean followMouse;
    private final boolean mimicPlayer;
    private final Vec3 rotation;
    private final List<Identifier> renderLayers;

    public PlayerDisplayUiComponent(float scale, boolean followMouse, boolean mimicPlayer, Vec3 rotation, List<Identifier> renderLayers, UiComponentProperties properties) {
        super(properties);
        this.scale = scale;
        this.followMouse = followMouse;
        this.mimicPlayer = mimicPlayer;
        this.rotation = rotation;
        this.renderLayers = renderLayers;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        var renderState = this.extractRenderState(minecraft.player);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Vector3f vector3f = new Vector3f(0.0F, renderState.boundingBoxHeight / 2.0F, 0.0F);

        if (this.followMouse) {
            float centerX = x + (width / 2.0F);
            float centerY = y + (height / 2.0F);
            float angleXComponent = (float) Math.atan((centerX - mouseX) / 40.0F);
            float angleYComponent = (float) Math.atan((centerY - mouseY) / 40.0F);
            Quaternionf quaternionf1 = new Quaternionf().rotateX(angleYComponent * 20.0F * (float) (Math.PI / 180.0));
            quaternionf.mul(quaternionf1);

            renderState.bodyRot = 180.0F + angleXComponent * 20.0F;
            renderState.yRot = angleXComponent * 20.0F;
            if (renderState.pose != Pose.FALL_FLYING) {
                renderState.xRot = -angleYComponent * 20.0F;
            } else {
                renderState.xRot = 0.0F;
            }
        } else {
            Quaternionf rotated = new Quaternionf().rotateXYZ(
                    (float) Math.toRadians(-this.rotation.x),
                    (float) Math.toRadians(-this.rotation.y),
                    (float) Math.toRadians(this.rotation.z));
            quaternionf = quaternionf.mul(rotated);
        }

        gui.submitEntityRenderState(
                renderState,
                this.scale,
                vector3f,
                quaternionf,
                null,
                x, y,
                x + width,
                y + height
        );
    }

    private AvatarRenderState extractRenderState(AbstractClientPlayer player) {
        AvatarRenderState renderState;

        if (this.mimicPlayer) {
            EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            AvatarRenderer<AbstractClientPlayer> entityrenderer = entityrenderdispatcher.getPlayerRenderer(player);
            renderState = entityrenderer.createRenderState(player, 1.0F);
        } else {
            renderState = new AvatarRenderState();
            renderState.entityType = EntityType.PLAYER;
            renderState.boundingBoxHeight = 1.8F;
            renderState.boundingBoxWidth = 0.6F;
            renderState.scale = 1F;
            renderState.skin = Objects.requireNonNull(player).getSkin();
            renderState.showHat = player.isModelPartShown(PlayerModelPart.HAT);
            renderState.showJacket = player.isModelPartShown(PlayerModelPart.JACKET);
            renderState.showLeftPants = player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            renderState.showRightPants = player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            renderState.showLeftSleeve = player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            renderState.showRightSleeve = player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            renderState.showCape = player.isModelPartShown(PlayerModelPart.CAPE);
        }

        renderState.lightCoords = 15728880;
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;
        renderState.bodyRot = 180.0F;
        renderState.yRot = 0F;
        renderState.xRot = 0F;
        renderState.boundingBoxWidth = renderState.boundingBoxWidth / renderState.scale;
        renderState.boundingBoxHeight = renderState.boundingBoxHeight / renderState.scale;
        renderState.scale = 1.0F;

        Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State> layers = new HashMap<>(renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.RENDER_LAYERS, Collections.emptyMap()));
        layers.putAll(this.createRenderLayers(player));
        renderState.setRenderData(PalladiumRenderStateKeys.RENDER_LAYERS, layers);
        renderState.setRenderData(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS,
                layers.keySet().stream()
                        .flatMap(l -> l.getProperties().hiddenModelParts().stream())
                        .collect(Collectors.toSet()));

        return renderState;
    }

    @SuppressWarnings("unchecked")
    private Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State> createRenderLayers(Player player) {
        var context = DataContext.forEntity(player);
        Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State> layers = new HashMap<>();

        for (Identifier layerId : this.renderLayers) {
            var layer = PackRenderLayerManager.INSTANCE.get(layerId);

            if (layer != null) {
                if (layer instanceof CompoundPackRenderLayer com) {
                    for (PackRenderLayer<?> comLayer : com.getLayers()) {
                        layers.put((PackRenderLayer<PackRenderLayer.State>) comLayer, comLayer.createState(context));
                    }
                } else {
                    layers.put((PackRenderLayer<PackRenderLayer.State>) layer, layer.createState(context));
                }
            }
        }

        return layers;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.PLAYER_DISPLAY;
    }

    public static class Serializer extends UiComponentSerializer<PlayerDisplayUiComponent> {

        @Override
        public MapCodec<PlayerDisplayUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, PlayerDisplayUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Player Display")
                    .setDescription("Renders your player on the screen")
                    .addOptional("scale", TYPE_NON_NEGATIVE_FLOAT, "Scale of the player.", 30F)
                    .addOptional("follow_mouse", TYPE_BOOLEAN, "When enabled the player will follow the mouse cursor.", false)
                    .addOptional("mimic_player", TYPE_BOOLEAN, "When enabled the player will copy all of the actual player's properties. Equipment, animation, render layers, etc.", true)
                    .addOptional("rotation", TYPE_VECTOR3, "The rotation that is applied to the player. If 'follow_mouse' is active, the rotation will be ignored.")
                    .addOptional("render_layers", SettingType.listOrPrimitive(TYPE_IDENTIFIER), "List of/single render layer ID that will be attached to the rendered player.");
        }
    }
}
