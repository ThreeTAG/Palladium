package net.threetag.palladium.client.gui.component.tab;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.threetag.palladium.data.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class IconTabNavigationBar extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    private static final int NO_TAB = -1;
    private static final int MAX_WIDTH = 400;
    private static final int HEIGHT = 24;
    private static final Component USAGE_NARRATION = Component.translatable("narration.tab_navigation.usage");
    private final LinearLayout layout = LinearLayout.horizontal();
    private int width;
    private final TabManager tabManager;
    private final ImmutableList<Tab> tabs;
    private final ImmutableList<TabButton> tabButtons;

    public IconTabNavigationBar(int width, TabManager tabManager, Iterable<Tab> tabs) {
        this.width = width;
        this.tabManager = tabManager;
        this.tabs = ImmutableList.copyOf(tabs);
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        ImmutableList.Builder<TabButton> builder = ImmutableList.builder();
        this.layout.setY(33);

        for(Tab tab : tabs) {
            builder.add(this.layout.addChild(new IconTabButton(tabManager, tab, 0, HEIGHT)));
        }

        this.tabButtons = builder.build();
    }

    public static Builder builder(TabManager tabManager, int width) {
        return new Builder(tabManager, width);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= (double)this.layout.getX() && mouseY >= (double)this.layout.getY() && mouseX < (double)(this.layout.getX() + this.layout.getWidth()) && mouseY < (double)(this.layout.getY() + this.layout.getHeight());
    }

    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (this.getFocused() != null) {
            this.getFocused().setFocused(focused);
        }

    }

    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);
        if (focused instanceof TabButton tabButton) {
            this.tabManager.setCurrentTab(tabButton.tab(), true);
        }

    }

    @Nullable
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (!this.isFocused()) {
            TabButton tabButton = this.currentTabButton();
            if (tabButton != null) {
                return ComponentPath.path(this, ComponentPath.leaf(tabButton));
            }
        }

        return event instanceof FocusNavigationEvent.TabNavigation ? null : super.nextFocusPath(event);
    }

    public List<? extends GuiEventListener> children() {
        return this.tabButtons;
    }

    public NarratableEntry.NarrationPriority narrationPriority() {
        return this.tabButtons.stream().map(AbstractWidget::narrationPriority).max(Comparator.naturalOrder()).orElse(NarrationPriority.NONE);
    }

    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        Optional<TabButton> optional = this.tabButtons.stream().filter(AbstractWidget::isHovered).findFirst().or(() -> Optional.ofNullable(this.currentTabButton()));
        optional.ifPresent((tabButton) -> {
            this.narrateListElementPosition(narrationElementOutput.nest(), tabButton);
            tabButton.updateNarration(narrationElementOutput);
        });
        if (this.isFocused()) {
            narrationElementOutput.add(NarratedElementType.USAGE, USAGE_NARRATION);
        }

    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput, TabButton tabButton) {
        if (this.tabs.size() > 1) {
            int i = this.tabButtons.indexOf(tabButton);
            if (i != NO_TAB) {
                narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.tab", i + 1, this.tabs.size()));
            }
        }

    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(RenderType::guiTextured, Screen.HEADER_SEPARATOR, 0, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, this.tabButtons.get(0).getX(), 2, 32, 2);
        int i = this.tabButtons.get(this.tabButtons.size() - 1).getRight();
        guiGraphics.blit(RenderType::guiTextured, Screen.HEADER_SEPARATOR, i, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);

        for (TabButton tabButton : this.tabButtons) {
            tabButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

    }

    public ScreenRectangle getRectangle() {
        return this.layout.getRectangle();
    }

    public void arrangeElements() {
        int i = Math.min(MAX_WIDTH, this.width) - 28;
        int j = Mth.roundToward(i / this.tabs.size(), 2);

        for (TabButton tabButton : this.tabButtons) {
            tabButton.setWidth(j);
        }

        this.layout.arrangeElements();
        this.layout.setX(Mth.roundToward((this.width - i) / 2, 2));
        this.layout.setY(33);
    }

    public void selectTab(int index, boolean playClickSound) {
        if (this.isFocused()) {
            this.setFocused(this.tabButtons.get(index));
        } else {
            this.tabManager.setCurrentTab(this.tabs.get(index), playClickSound);
        }

    }

    public boolean keyPressed(int keycode) {
        if (Screen.hasControlDown()) {
            int i = this.getNextTabIndex(keycode);
            if (i != NO_TAB) {
                this.selectTab(Mth.clamp(i, 0, this.tabs.size() - 1), true);
                return true;
            }
        }

        return false;
    }

    private int getNextTabIndex(int keycode) {
        if (keycode >= 49 && keycode <= 57) {
            return keycode - 49;
        } else {
            if (keycode == 258) {
                int i = this.currentTabIndex();
                if (i != NO_TAB) {
                    int j = Screen.hasShiftDown() ? i - 1 : i + 1;
                    return Math.floorMod(j, this.tabs.size());
                }
            }

            return NO_TAB;
        }
    }

    private int currentTabIndex() {
        Tab tab = this.tabManager.getCurrentTab();
        int i = this.tabs.indexOf(tab);
        return i;
    }

    @Nullable
    private TabButton currentTabButton() {
        int i = this.currentTabIndex();
        return i != NO_TAB ? this.tabButtons.get(i) : null;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final int width;
        private final TabManager tabManager;
        private final List<Tab> tabs = new ArrayList<>();

        Builder(TabManager tabManager, int width) {
            this.tabManager = tabManager;
            this.width = width;
        }

        public Builder addTab(Tab tab) {
            this.tabs.add(tab);
            return this;
        }

        public Builder addTabs(Tab... tabs) {
            Collections.addAll(this.tabs, tabs);
            return this;
        }

        public IconTabNavigationBar build() {
            return new IconTabNavigationBar(this.width, this.tabManager, this.tabs);
        }
    }

    public static class IconTabButton extends TabButton {

        public IconTabButton(TabManager tabManager, Tab tab, int width, int height) {
            super(tabManager, tab, width, height);
            this.setTooltip(Tooltip.create(tab.getTabTitle()));
        }

        @Override
        public void renderString(GuiGraphics guiGraphics, Font font, int color) {
            if(this.tab() instanceof IconTab iconTab) {
                var mc = Minecraft.getInstance();
                iconTab.getIcon().draw(mc, guiGraphics, DataContext.forEntity(mc.player), this.getX() + (this.getWidth() / 2) - 8, this.getY() + (this.getHeight() / 2) - 7);
            } else {
                super.renderString(guiGraphics, font, color);
            }
        }
    }
}
