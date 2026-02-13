package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.threetag.palladium.client.gui.widget.grid.AbstractSelectionGrid;
import net.threetag.palladium.client.renderer.entity.layer.pack.CompoundPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.Easing;

import java.util.*;

public class CustomizationPreviewComponent extends AbstractWidget {

    private static final int TRANSITION_TIME = 10;

    private final Minecraft minecraft;
    private CustomizationPreview targetTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private CustomizationPreview prevTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private CustomizationPreview renderedTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private int transitionTicks, prevTransitionTicks;
    private final AvatarRenderState playerPreviewState;

    public CustomizationPreviewComponent(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.minecraft = Minecraft.getInstance();

        var player = this.minecraft.player;
        this.playerPreviewState = new AvatarRenderState();
        this.playerPreviewState.entityType = EntityType.PLAYER;
        this.playerPreviewState.boundingBoxHeight = 1.8F;
        this.playerPreviewState.boundingBoxWidth = 0.6F;
        this.playerPreviewState.scale = 1F;
        this.playerPreviewState.skin = Objects.requireNonNull(player).getSkin();
        this.playerPreviewState.showHat = player.isModelPartShown(PlayerModelPart.HAT);
        this.playerPreviewState.showJacket = player.isModelPartShown(PlayerModelPart.JACKET);
        this.playerPreviewState.showLeftPants = player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
        this.playerPreviewState.showRightPants = player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
        this.playerPreviewState.showLeftSleeve = player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
        this.playerPreviewState.showRightSleeve = player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
        this.playerPreviewState.showCape = player.isModelPartShown(PlayerModelPart.CAPE);
    }

    public static void updateRenderStateForCategory(LivingEntityRenderState renderState, CustomizationCategory category, LivingEntity entity) {
        var handler = EntityCustomizationHandler.get(entity);
        var context = DataContext.forEntity(entity);
        var registry = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);

        if (category.isVisible(context)) {
            var customization = handler.get(registry.wrapAsHolder(category));

            if (customization != null) {
                updateRenderStateForCustomization(renderState, customization.value(), context);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void updateRenderStateForCustomization(LivingEntityRenderState renderState, Customization customization, DataContext context) {
        Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State> existing = new HashMap<>(renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.RENDER_LAYERS, Collections.emptyMap()));
        Set<String> hiddenBodyParts = new HashSet<>(renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS, Collections.emptySet()));
        var layer = PackRenderLayerManager.INSTANCE.get(customization.getRenderLayerId(Objects.requireNonNull(Minecraft.getInstance().player).registryAccess(), true));

        if (layer != null) {
            if (layer instanceof CompoundPackRenderLayer com) {
                for (PackRenderLayer<?> comLayer : com.getLayers()) {
                    if (!existing.containsKey(comLayer)) {
                        existing.put((PackRenderLayer<PackRenderLayer.State>) comLayer, comLayer.createState(context));
                        hiddenBodyParts.addAll(comLayer.getProperties().hiddenModelParts());
                    }
                }
            } else if (!existing.containsKey(layer)) {
                existing.put((PackRenderLayer<PackRenderLayer.State>) layer, layer.createState(context));
                hiddenBodyParts.addAll(layer.getProperties().hiddenModelParts());
            }
        }

        renderState.setRenderData(PalladiumRenderStateKeys.RENDER_LAYERS, existing);
        renderState.setRenderData(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS, hiddenBodyParts);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        this.renderedTransform = CustomizationPreview.lerp(Easing.INOUTSINE.apply(1F - (Mth.lerp(partialTick, this.prevTransitionTicks, this.transitionTicks) / TRANSITION_TIME)),
                this.prevTransform, this.targetTransform);
        PlayerCustomizationScreen.renderEntity(guiGraphics, this.getX(), this.getY(), this.getRight(), this.getBottom(), 70, this.renderedTransform, this.playerPreviewState, null);
        this.renderSeparators(guiGraphics);
    }

    protected void renderSeparators(GuiGraphics guiGraphics) {
        Identifier identifier = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        Identifier identifier2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY() - 2, 0, 0, this.getWidth(), 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier2, this.getX(), this.getBottom(), 0, 0, this.getWidth(), 2, 32, 2);
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        Identifier identifier = this.minecraft.level == null ? AbstractSelectionGrid.MENU_LIST_BACKGROUND : AbstractSelectionGrid.INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), this.getRight(), this.getBottom(), this.getWidth(), this.getHeight(), 32, 32);
    }

    public void tick() {
        this.prevTransitionTicks = this.transitionTicks;

        if (this.transitionTicks > 0) {
            this.transitionTicks--;
        }

        this.playerPreviewState.setRenderData(PalladiumRenderStateKeys.RENDER_LAYERS, null);
        this.playerPreviewState.setRenderData(PalladiumRenderStateKeys.HIDDEN_MODEL_PARTS, null);
        for (CustomizationCategory category : Objects.requireNonNull(this.minecraft.level).registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY)) {
            updateRenderStateForCategory(this.playerPreviewState, category, this.minecraft.player);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void setTargetTransformation(CustomizationPreview transformation) {
        this.prevTransform = this.renderedTransform;
        this.targetTransform = transformation != null ? transformation.invertYRot() : CustomizationCategory.DEFAULT_PREVIEW.invertYRot();
        this.transitionTicks = TRANSITION_TIME;
        this.prevTransitionTicks = TRANSITION_TIME;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }
}
