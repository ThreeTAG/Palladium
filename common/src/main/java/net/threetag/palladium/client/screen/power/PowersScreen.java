package net.threetag.palladium.client.screen.power;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.components.IconButton;
import net.threetag.palladium.event.PalladiumClientEvents;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHandler;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladiumcore.event.ScreenEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PowersScreen extends Screen {

    public static final ResourceLocation WINDOW = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/window.png");
    public static final ResourceLocation TABS = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/tabs.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/widgets.png");

    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 196;
    public static final int WINDOW_INSIDE_X = 9;
    public static final int WINDOW_INSIDE_Y = 18;
    public static final int WINDOW_INSIDE_WIDTH = 234;
    public static final int WINDOW_INSIDE_HEIGHT = 169;
    private static final int WINDOW_TITLE_X = 8;
    private static final int WINDOW_TITLE_Y = 6;
    public static final int BACKGROUND_TILE_WIDTH = 16;
    public static final int BACKGROUND_TILE_HEIGHT = 16;
    public static final int BACKGROUND_TILE_COUNT_X = 14;
    public static final int BACKGROUND_TILE_COUNT_Y = 7;
    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");
    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");
    private static final Component TITLE = Component.translatable("gui.palladium.powers");
    private final List<PowerTab> tabs = new ArrayList<>();
    @Nullable
    public PowerTab selectedTab;
    private boolean isScrolling;
    private static int tabPage;
    private static int maxPages;
    public Screen overlayScreen = null;

    public PowersScreen() {
        super(Component.empty());
    }

    public static void register() {
        ScreenEvents.INIT_POST.register((screen) -> {
            var guiPos = RotatingIconButton.getPos(screen);

            if (guiPos != null) {
                IconButton button;
                screen.addRenderableWidget(button = new RotatingIconButton(guiPos.x, guiPos.y, screen, new ItemIcon(ItemStack.EMPTY), b -> Minecraft.getInstance().setScreen(new PowersScreen())));
                button.setTooltip(Tooltip.create(TITLE));
            }
        });
    }

    @Override
    protected void init() {
        this.tabs.clear();

        if (this.selectedTab != null) {
            this.selectedTab.onClosed();
        }
        this.selectedTab = null;

        AtomicInteger i = new AtomicInteger();
        PowerManager.getPowerHandler(Objects.requireNonNull(this.minecraft).player).ifPresent(handler -> handler.getPowerHolders()
                .values()
                .stream()
                .sorted(Comparator.comparingInt(holder -> PowerManager.getInstance(false).getPowers().stream().toList().indexOf(holder.getPower())))
                .forEach(holder -> {
                    if (!holder.getPower().isHidden() && holder.getAbilities().values().stream().anyMatch(en -> !en.getProperty(Ability.HIDDEN_IN_GUI))) {
                        var type = holder.getPower().getGuiDisplayType();

                        if (type == Power.GuiDisplayType.AUTO) {
                            type = TreePowerTab.canBeTree(holder) ? Power.GuiDisplayType.TREE : Power.GuiDisplayType.LIST;
                        }

                        PowerTab tab = null;

                        if (type == Power.GuiDisplayType.TREE)
                            tab = TreePowerTab.create(this.minecraft, this, i.getAndIncrement(), holder);
                        else
                            tab = ListPowerTab.create(this.minecraft, this, i.getAndIncrement(), holder);

                        if (tab != null) {
                            this.tabs.add(tab);
                        }
                    }
                }));

        if (this.tabs.size() > PowerTabType.MAX_TABS) {
            int guiLeft = (this.width - WINDOW_WIDTH) / 2;
            int guiTop = (this.height - WINDOW_HEIGHT) / 2;
            this.addRenderableWidget(Button.builder(Component.literal("<"), (b) -> {
                tabPage = Math.max(tabPage - 1, 0);
            }).bounds(guiLeft, guiTop - 50, 20, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal(">"), (b) -> {
                tabPage = Math.min(tabPage + 1, maxPages);
            }).bounds(guiLeft + WINDOW_WIDTH - 20, guiTop - 50, 20, 20).build());
            maxPages = this.tabs.size() / PowerTabType.MAX_TABS;
        }

        if (!this.tabs.isEmpty()) {
            this.selectedTab = this.tabs.get(0);
            this.selectedTab.onOpened();
        }

        if (this.overlayScreen != null) {
            this.overlayScreen.init(this.minecraft, this.width, this.height);
        }
    }

    @Override
    public void removeWidget(GuiEventListener listener) {
        super.removeWidget(listener);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int i = (this.width - WINDOW_WIDTH) / 2;
            int j = (this.height - WINDOW_HEIGHT) / 2;

            if (this.isOverOverlayScreen(mouseX, mouseY)) {
                return this.overlayScreen.mouseClicked(mouseX, mouseY, button);
            } else {
                for (PowerTab powerTab : this.tabs) {
                    if (powerTab.isMouseOver(i, j, mouseX, mouseY)) {
                        if (this.selectedTab != null) {
                            this.selectedTab.onClosed();
                        }
                        this.selectedTab = powerTab;
                        this.selectedTab.onOpened();
                        break;
                    }
                }

                if (this.selectedTab != null) {
                    this.selectedTab.mouseClicked(mouseX, mouseY, button);
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // TODO key for this?
//        if (this.minecraft.options.keyAdvancements.matches(keyCode, scanCode)) {
//            this.minecraft.setScreen(null);
//            this.minecraft.mouseHandler.grabMouse();
//            return true;
//        } else {
        return this.overlayScreen == null ? super.keyPressed(keyCode, scanCode, modifiers) : this.overlayScreen.keyPressed(keyCode, scanCode, modifiers);
//        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - WINDOW_WIDTH) / 2;
        int j = (this.height - WINDOW_HEIGHT) / 2;
        this.renderBackground(guiGraphics);
        this.renderInside(guiGraphics, mouseX, mouseY, i, j, partialTick);
        this.renderWindow(guiGraphics, i, j);
        this.renderTooltips(guiGraphics, mouseX, mouseY, i, j, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (this.selectedTab != null && this.overlayScreen != null) {
            guiGraphics.pose().translate(0, 0, 500);
            this.overlayScreen.render(guiGraphics, mouseX, mouseY, partialTick);
            this.selectedTab.fade = Mth.clamp(this.selectedTab.fade + 0.02F, 0, 0.5F);
            guiGraphics.pose().translate(0, 0, -500);
        }

        var powerId = this.selectedTab != null ? this.selectedTab.powerHolder.getPower().getId() : null;
        PalladiumClientEvents.RENDER_POWER_SCREEN.invoker().renderPowerScreen(this, guiGraphics, mouseX, mouseY, partialTick, powerId);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int x1 = ((this.width - WINDOW_WIDTH) / 2) + WINDOW_INSIDE_X;
        int y1 = ((this.height - WINDOW_HEIGHT) / 2) + WINDOW_INSIDE_Y;
        boolean inWindow = mouseX >= x1 && mouseX <= x1 + WINDOW_INSIDE_WIDTH && mouseY >= y1 && mouseY <= y1 + WINDOW_INSIDE_HEIGHT;

        if (button != 0) {
            this.isScrolling = false;
            return false;
        } else if (this.isScrolling || inWindow) {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab instanceof TreePowerTab tree) {
                tree.scroll(dragX, dragY);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isScrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void renderInside(GuiGraphics guiGraphics, int mouseX, int mouseY, int offsetX, int offsetY, float partialTick) {
        PowerTab tab = this.selectedTab;
        if (tab == null) {
            guiGraphics.fill(offsetX + WINDOW_INSIDE_X, offsetY + WINDOW_INSIDE_Y, offsetX + WINDOW_INSIDE_X + WINDOW_INSIDE_WIDTH, offsetY + WINDOW_INSIDE_Y + WINDOW_INSIDE_HEIGHT, -16777216);
            int i = offsetX + WINDOW_INSIDE_X + 117;
            guiGraphics.drawCenteredString(this.font, NO_ADVANCEMENTS_LABEL, i, offsetY + WINDOW_INSIDE_Y + 56 - 4, -1);
            guiGraphics.drawCenteredString(this.font, VERY_SAD_LABEL, i, offsetY + WINDOW_INSIDE_Y + WINDOW_INSIDE_HEIGHT - 9, -1);
        } else {
            tab.drawContents(guiGraphics, offsetX + WINDOW_INSIDE_X, offsetY + WINDOW_INSIDE_Y, mouseX, mouseY, partialTick);
        }
    }

    public void renderWindow(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        guiGraphics.blit(WINDOW, offsetX, offsetY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (this.tabs.size() > 1) {
            RenderSystem.setShaderTexture(0, TABS);

            for (PowerTab tab : this.tabs) {
                tab.drawTab(guiGraphics, offsetX, offsetY, tab == this.selectedTab);
            }

            RenderSystem.defaultBlendFunc();

            for (PowerTab tab : this.tabs) {
                tab.drawIcon(guiGraphics, offsetX, offsetY);
            }

            RenderSystem.disableBlend();
        }

        guiGraphics.drawString(Objects.requireNonNull(this.minecraft).font, TITLE, offsetX + 8, offsetY + 6, 4210752, false);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int offsetX, int offsetY, float partialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float) (offsetX + WINDOW_INSIDE_X), (float) (offsetY + WINDOW_INSIDE_Y), 400.0F);
//            RenderSystem.enableDepthTest();
            this.selectedTab.drawTooltips(guiGraphics, mouseX - offsetX - WINDOW_INSIDE_X, mouseY - offsetY - WINDOW_INSIDE_Y, offsetX, offsetY, partialTick, this.overlayScreen != null);
//            RenderSystem.disableDepthTest();
            guiGraphics.pose().popPose();
        }

        if (this.tabs.size() > 1) {
            for (PowerTab tab : this.tabs) {
                if (tab.isMouseOver(offsetX, offsetY, mouseX, mouseY)) {
                    guiGraphics.renderTooltip(this.font, tab.getTitle(), mouseX, mouseY);
                }
            }
        }
    }

    public void closeOverlayScreen() {
        this.overlayScreen = null;
    }

    public void openOverlayScreen(Screen screen) {
        this.closeOverlayScreen();
        this.overlayScreen = screen;
        this.overlayScreen.init(Objects.requireNonNull(this.minecraft), this.width, this.height);
    }

    public boolean isOverOverlayScreen(double mouseX, double mouseY) {
        return overlayScreen != null;
    }

    public static class RotatingIconButton extends IconButton {

        private final Screen screen;

        public RotatingIconButton(int x, int y, Screen screen, IIcon icon, OnPress onPress) {
            super(x, y, icon, onPress, DEFAULT_NARRATION);
            this.screen = screen;
        }

        @Override
        public IIcon getIcon() {
            List<IIcon> icons = Lists.newArrayList();
            Minecraft mc = Minecraft.getInstance();
            PowerManager.getPowerHandler(mc.player).ifPresent(handler -> handler.getPowerHolders().values().stream().filter(holder -> !holder.getPower().isHidden() && holder.getAbilities().values().stream().anyMatch(en -> !en.getProperty(Ability.HIDDEN_IN_GUI))).forEach(holder -> icons.add(holder.getPower().getIcon())));
            if (icons.isEmpty()) {
                this.visible = false;
                icons.add(new ItemIcon(Blocks.BARRIER));
            } else {
                this.visible = !(this.screen instanceof CreativeModeInventoryScreen) || CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INVENTORY);
            }
            int i = (Objects.requireNonNull(mc.player).tickCount / 20) % icons.size();
            return icons.get(i);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            this.visible = !(this.screen instanceof CreativeModeInventoryScreen) || CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INVENTORY);

            var pos = getPos(this.screen);
            if (pos != null) {
                this.setPosition(pos.x, pos.y);
            }
            this.active = this.visible && !PowerManager.getPowerHandler(Minecraft.getInstance().player).orElse(new PowerHandler(null)).getPowerHolders().isEmpty();
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

        public static Vector2i getPos(Screen screen) {
            if (screen instanceof InventoryScreen || screen.getClass().toString().equals("class top.theillusivec4.curios.client.gui.CuriosScreen")) {
                return new Vector2i(((AbstractContainerScreen<?>) screen).leftPos + 134, screen.height / 2 - 23);
            } else if (screen instanceof CreativeModeInventoryScreen) {
                return new Vector2i(((AbstractContainerScreen<?>) screen).leftPos + 148, screen.height / 2 - 50);
            }
            return null;
        }
    }

    @FunctionalInterface
    public interface RenderCallback {

        void postRender(PowersScreen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, ResourceLocation tab);

    }

}
