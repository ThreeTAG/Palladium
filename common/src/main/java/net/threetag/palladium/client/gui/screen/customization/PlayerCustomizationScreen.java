package net.threetag.palladium.client.gui.screen.customization;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.EditButton;
import net.threetag.palladium.client.gui.component.tab.IconTabNavigationBar;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Comparator;
import java.util.Objects;

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

    public static void register() {
        ClientGuiEvent.INIT_POST.register((screen, access) -> {
            Button button = null;
            Component text = Component.translatable(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY);

            if (screen instanceof InventoryScreen inv) {
                button = new EditButton(inv.leftPos + 63, inv.topPos + 66, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen))) {
                    @Override
                    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        this.setPosition(inv.leftPos + 63, inv.topPos + 66);
                        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    }
                };
                button.setTooltip(Tooltip.create(text));
            }

            if (screen instanceof CreativeModeInventoryScreen inv) {
                button = new EditButton(inv.leftPos + 93, inv.topPos + 37, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen))) {
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
                access.addRenderableWidget(button);
            }
        });
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

    public void changePreview(PoseStackTransformation transformation) {
        this.preview.setTargetTransformation(transformation);
    }

    public static void renderEntityInInventory(
            GuiGraphics guiGraphics, float x, float y, float baseScale, PoseStackTransformation transformation, LivingEntity entity
    ) {
        entity.setYHeadRot(0F);
        entity.setXRot(0F);
        entity.setYBodyRot(0F);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 50.0);
        guiGraphics.pose().scale(baseScale, baseScale, -baseScale);

        guiGraphics.pose().translate(0, 0, 500);
        guiGraphics.fill(-10, -10, 10, 10, 0xFFFF0000);
        guiGraphics.pose().translate(0, 0, -500);

        transformation.apply(guiGraphics.pose());

        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(180));
        guiGraphics.pose().translate(0, entity.getBbHeight() / -2F, 0);

        guiGraphics.flush();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        entityRenderDispatcher.setRenderShadow(false);
        guiGraphics.drawSpecial(multiBufferSource -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 1.0F, guiGraphics.pose(), multiBufferSource, 15728880));
        guiGraphics.flush();
        entityRenderDispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
}
