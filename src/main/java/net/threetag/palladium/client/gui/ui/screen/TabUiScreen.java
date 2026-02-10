package net.threetag.palladium.client.gui.ui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.ui.layout.UiLayout;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

import java.util.ArrayList;
import java.util.List;

public class TabUiScreen extends UiScreen {

    private final List<Tab> tabs;
    private List<Tab> selectableTabs;
    private int tabsPerPage;
    private int currentTabPage = -1;
    private int tabPageAmount = 1;
    private final List<AbstractWidget> tabWidgets = new ArrayList<>();
    private Tab selectedTab;

    public TabUiScreen(List<Tab> tabs) {
        super(tabs.getFirst().layout());
        this.tabs = tabs;
        this.selectedTab = tabs.getFirst();
    }

    @Override
    protected void init() {
        super.init();
        this.refreshTabButtons(true);
    }

    @Override
    protected void changeLayout(UiLayout layout) {
        super.changeLayout(layout);
        this.refreshTabButtons(true);
    }

    public Tab getSelectedTab() {
        return this.selectedTab;
    }

    public void setTab(Tab tab) {
        this.selectedTab = tab;
        this.changeLayout(tab.layout());
    }

    private void fillTabPages(boolean updateCurrentPage) {
        this.tabsPerPage = Mth.floor(this.getLayout().getWidth() / 29F);
        this.tabPageAmount = Mth.ceil((float) this.tabs.size() / this.tabsPerPage);
        this.currentTabPage = updateCurrentPage ? Mth.floor((float) this.tabs.indexOf(this.selectedTab) / this.tabsPerPage) : this.currentTabPage;
        int startIndex = this.currentTabPage * this.tabsPerPage;
        this.selectableTabs = this.tabs.subList(startIndex, Math.min(startIndex + this.tabsPerPage + 1, this.tabs.size()));
    }

    protected void refreshTabButtons(boolean updateCurrentPage) {
        this.fillTabPages(updateCurrentPage);

        for (AbstractWidget tabWidget : this.tabWidgets) {
            this.removeWidget(tabWidget);
        }

        this.tabWidgets.clear();

        for (int i = 0; i < this.selectableTabs.size(); i++) {
            var tab = this.selectableTabs.get(i);
            var widget = new TabWidget(this.leftPos + this.selectedTab.tabXOffset + i * 29, this.topPos - 28, tab,
                    this.leftPos + this.selectedTab.tabXOffset,
                    this.selectedTab.tabAreaWidth > 0 ? this.selectedTab.tabAreaWidth : this.selectedTab.layout().getWidth(),
                    tab == this.selectedTab);
            this.tabWidgets.add(widget);
            this.addRenderableWidget(widget);
        }

        if (this.tabPageAmount > 1) {
            var switchPage = Button.builder(Component.literal("<"), b -> setTabPage(this.currentTabPage - 1)).pos(this.leftPos + this.selectedTab.tabXOffset, this.topPos - 50).size(20, 20).build();
            this.tabWidgets.add(switchPage);
            this.addRenderableWidget(switchPage);
            switchPage = Button.builder(Component.literal(">"), b -> setTabPage(this.currentTabPage + 1)).pos(this.leftPos + this.selectedTab.tabXOffset + +this.selectedTab.tabAreaWidth - 20, this.topPos - 50).size(20, 20).build();
            this.tabWidgets.add(switchPage);
            this.addRenderableWidget(switchPage);
        }
    }

    public void setTabPage(int i) {
        var page = Mth.clamp(i, 0, this.tabPageAmount - 1);

        if (page != this.currentTabPage) {
            this.currentTabPage = page;
            this.refreshTabButtons(false);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        if (this.tabPageAmount > 1) {
            Component page = Component.literal(String.format("%d / %d", this.currentTabPage + 1, this.tabPageAmount));
            guiGraphics.drawCenteredString(this.font, page.getVisualOrderText(), this.leftPos + this.selectedTab.tabXOffset + (this.selectedTab.tabAreaWidth / 2), this.topPos - 44, -1);
        }
    }

    public record Tab(UiLayout layout, Component title, Icon icon, int tabXOffset, int tabAreaWidth) {

        public Tab(UiLayout layout, Component title, Icon icon) {
            this(layout, title, icon, 0, -1);
        }
    }

    private class TabWidget extends AbstractWidget {

        private static final Identifier SPRITE_TAB_ABOVE_LEFT = Palladium.id("tab/above_left");
        private static final Identifier SPRITE_TAB_ABOVE_LEFT_SELECTED = Palladium.id("tab/above_left_selected");
        private static final Identifier SPRITE_TAB_ABOVE_MIDDLE = Palladium.id("tab/above_middle");
        private static final Identifier SPRITE_TAB_ABOVE_MIDDLE_SELECTED = Palladium.id("tab/above_middle_selected");
        private static final Identifier SPRITE_TAB_ABOVE_RIGHT = Palladium.id("tab/above_right");
        private static final Identifier SPRITE_TAB_ABOVE_RIGHT_SELECTED = Palladium.id("tab/above_right_selected");

        private final Tab tab;
        private final TextAlignment alignment;
        private final boolean selected;

        public TabWidget(int x, int y, Tab tab, int tabStartX, int tabAreaWidth, boolean selected) {
            super(x, y, 28, 32, tab.title);
            this.tab = tab;
            this.alignment = x == tabStartX ? TextAlignment.LEFT : x + this.getWidth() == tabAreaWidth ? TextAlignment.RIGHT : TextAlignment.CENTER;
            this.selected = selected;
            this.setTooltip(Tooltip.create(tab.title));
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            Identifier sprite;

            if (this.alignment == TextAlignment.LEFT) {
                sprite = this.selected ? SPRITE_TAB_ABOVE_LEFT_SELECTED : SPRITE_TAB_ABOVE_LEFT;
            } else if (this.alignment == TextAlignment.RIGHT) {
                sprite = this.selected ? SPRITE_TAB_ABOVE_RIGHT_SELECTED : SPRITE_TAB_ABOVE_RIGHT;
            } else {
                sprite = this.selected ? SPRITE_TAB_ABOVE_MIDDLE_SELECTED : SPRITE_TAB_ABOVE_MIDDLE;
            }

            var mc = TabUiScreen.this.getMinecraft();
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            IconRenderer.drawIcon(this.tab.icon(), mc, guiGraphics,
                    DataContext.forEntity(mc.player), this.getX() + this.getWidth() / 2 - 8,
                    this.getY() + this.getHeight() / 2 - (this.selected ? 8 : 6));
        }

        @Override
        public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
            if (!this.selected) {
                TabUiScreen.this.setTab(this.tab);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }
    }
}
