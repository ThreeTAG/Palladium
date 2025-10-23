package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.component.EditButton;
import net.threetag.palladium.client.gui.component.tab.IconTabNavigationBar;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PlayerCustomizationScreen extends Screen {

    public static final String TITLE_TRANSLATION_KEY = "gui.palladium.player_customizations";
    protected final Screen lastScreen;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private IconTabNavigationBar tabNavigationBar;
    private CustomizationPreviewComponent preview;

    public PlayerCustomizationScreen(Screen lastScreen) {
        super(Component.translatable(TITLE_TRANSLATION_KEY));
        this.lastScreen = lastScreen;
    }

    @SubscribeEvent
    static void screenInit(ScreenEvent.Init.Post e) {
        var screen = e.getScreen();
        Button button = null;
        Component text = Component.translatable(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY);

        if (screen instanceof InventoryScreen inv) {
            button = new EditButton(inv.getGuiLeft() + 63, inv.getGuiTop() + 66, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen))) {
                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    this.setPosition(inv.getGuiLeft() + 63, inv.getGuiTop() + 66);
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                }
            };
            button.setTooltip(Tooltip.create(text));
        }

        if (screen instanceof CreativeModeInventoryScreen inv) {
            button = new EditButton(inv.getGuiLeft() + 93, inv.getGuiTop() + 37, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen))) {
                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    this.visible = CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.INVENTORY);
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                }
            };
            button.setTooltip(Tooltip.create(text));
        }

        if (button != null) {
            button.active = Minecraft.getInstance().player != null;
            screen.addRenderableWidget(button);
        }
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

        for (CustomizationCategory slot : Objects.requireNonNull(this.minecraft).level.registryAccess()
                .lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY)
                .stream().sorted(Comparator.comparingInt(CustomizationCategory::sortIndex)).toList()) {
            tabBuilder.addTab(new CustomizationCategoryTab(this, slot));
        }

        this.tabNavigationBar = tabBuilder.build();
        this.addRenderableWidget(this.tabNavigationBar);
        this.tabNavigationBar.selectTab(0, false);

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

        if (this.tabManager.getCurrentTab() instanceof CustomizationCategoryTab tab) {
            tab.tick(Objects.requireNonNull(this.minecraft).isPaused());
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
            CustomizationPreview transformation, LivingEntity entity, @Nullable Consumer<GuiEntityRenderState> stateConsumer
    ) {
        var yHeadRot = entity.getYHeadRot();
        var xRot = entity.getXRot();
        var yBodyRot = entity.yBodyRot;
        entity.setYHeadRot(0F);
        entity.setXRot(0F);
        entity.setYBodyRot(0F);

        var rotation = transformation.rotation();
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(180F));
        quaternionf.mul(new Quaternionf()
                .rotationXYZ((float) (rotation.x * (Math.PI / 180.0)), (float) (rotation.y * (Math.PI / 180.0)), (float) (rotation.z * (Math.PI / 180.0))));

        var translation = new Vector3f(0, entity.getBbHeight() / 2F, 0).add(transformation.translation().toVector3f().div(2));
        var scale = (baseScale / entity.getScale()) * transformation.scale();

        if (stateConsumer == null) {
            InventoryScreen.renderEntityInInventory(guiGraphics, x1, y1, x2, y2,
                    scale,
                    translation,
                    quaternionf,
                    null,
                    entity);
        } else {
            EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super LivingEntity, ?> entityRenderer = entityRenderDispatcher.getRenderer(entity);
            EntityRenderState entityRenderState = entityRenderer.createRenderState(entity, 1.0F);
            entityRenderState.hitboxesRenderState = null;
            var state = new GuiEntityRenderState(entityRenderState, translation, quaternionf, null, x1, y1, x2, y2, scale, guiGraphics.scissorStack.peek());
            stateConsumer.accept(state);
        }

        entity.setYHeadRot(yHeadRot);
        entity.setXRot(xRot);
        entity.setYBodyRot(yBodyRot);
    }
}
