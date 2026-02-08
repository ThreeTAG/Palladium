package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.client.gui.widget.tab.IconTabNavigationBar;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationHelper;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

public class PlayerCustomizationScreen extends Screen {

    public static final String TITLE_TRANSLATION_KEY = "gui.palladium.player_customizations";

    protected final Screen lastScreen;
    protected final ResourceKey<CustomizationCategory> preselected;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    public final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private IconTabNavigationBar tabNavigationBar;
    private CustomizationPreviewComponent preview;

    public PlayerCustomizationScreen(Screen lastScreen) {
        this(lastScreen, null);
    }

    public PlayerCustomizationScreen(Screen lastScreen, @Nullable ResourceKey<CustomizationCategory> preselected) {
        super(Component.translatable(TITLE_TRANSLATION_KEY));
        this.lastScreen = lastScreen;
        this.preselected = preselected;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);
        this.addContents();
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    private void addContents() {
        var tabBuilder = IconTabNavigationBar.builder(this.tabManager, this.width);
        var registry = Objects.requireNonNull(Objects.requireNonNull(this.minecraft).level).registryAccess()
                .lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);
        int i = 0;
        int selectedTab = 0;

        for (CustomizationCategory category : registry
                .stream().filter(category -> CustomizationHelper.hasSelectableCustomization(this.minecraft.player, category))
                .sorted(Comparator.comparingInt(CustomizationCategory::sortIndex)).toList()) {
            tabBuilder.addTab(new CustomizationCategoryTab(this, registry.wrapAsHolder(category)));

            if (this.preselected != null && this.preselected.equals(registry.getResourceKey(category).orElse(null))) {
                selectedTab = i;
            }

            i++;
        }

        this.tabNavigationBar = tabBuilder.build();
        this.addRenderableWidget(this.tabNavigationBar);
        this.tabNavigationBar.selectTab(selectedTab, false);

        this.addRenderableWidget(this.preview = new CustomizationPreviewComponent(
                this.width / 3 * 2, this.tabNavigationBar.getRectangle().bottom(),
                this.width / 3, this.layout.getContentHeight()
        ));
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();

        if (this.tabNavigationBar != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width, this.height - this.layout.getFooterHeight() - i);
            this.tabManager.setTabArea(screenRectangle);
            this.layout.setHeaderHeight(i);
            this.layout.arrangeElements();

            if (this.preview != null) {
                this.preview.setPosition(screenRectangle.width() / 3 * 2, i);
                this.preview.setSize(screenRectangle.width() / 3, screenRectangle.height());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.preview != null) {
            this.preview.tick();
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    public void changePreview(CustomizationPreview transformation) {
        this.preview.setTargetTransformation(transformation);
    }

    public static void renderEntity(
            GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, float baseScale,
            CustomizationPreview transformation, LivingEntityRenderState entityRenderState, @Nullable Consumer<GuiEntityRenderState> stateConsumer
    ) {
        var rotation = transformation.rotation();
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(180F));
        quaternionf.mul(new Quaternionf()
                .rotationXYZ((float) (rotation.x * (Math.PI / 180.0)), (float) (rotation.y * (Math.PI / 180.0)), (float) (rotation.z * (Math.PI / 180.0))));

        var translation = new Vector3f(0, entityRenderState.boundingBoxHeight / 2F, 0).add(transformation.translation().toVector3f().div(2));
        var scale = (baseScale / entityRenderState.scale) * transformation.scale();

        entityRenderState.lightCoords = 15728880;
        entityRenderState.shadowPieces.clear();
        entityRenderState.outlineColor = 0;
        guiGraphics.submitEntityRenderState(entityRenderState, scale, translation, quaternionf, null, x1, y1, x2, y2);
    }
}
