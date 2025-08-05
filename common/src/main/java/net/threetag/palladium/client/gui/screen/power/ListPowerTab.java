package net.threetag.palladium.client.gui.screen.power;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ListPowerTab extends PowerTab {

    private AbilityList list;
    private AbilityInstance<?> hovered;

    protected ListPowerTab(Minecraft minecraft, PowersScreen screen, PowerTabType type, int tabIndex, PowerHolder powerHolder) {
        super(minecraft, screen, type, tabIndex, powerHolder);
    }

    @Override
    public void populate() {
        int i = (this.screen.width - PowersScreen.WINDOW_WIDTH) / 2;
        int j = (this.screen.height - PowersScreen.WINDOW_HEIGHT) / 2;
        this.list = new AbilityList(minecraft,
                this,
                PowersScreen.WINDOW_INSIDE_WIDTH,
                PowersScreen.WINDOW_INSIDE_HEIGHT,
                i + PowersScreen.WINDOW_INSIDE_X,
                j + PowersScreen.WINDOW_INSIDE_Y,
                30
        );
        this.list.populate(this.powerHolder);
    }

    @Override
    public void onOpened() {
        this.populate();

        if (this.list != null)
            this.screen.addWidget(this.list);
    }

    @Override
    public void onClosed() {
        if (this.list != null)
            this.screen.removeWidget(this.list);
    }

    @Override
    public void drawContents(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        TextureReference backgroundTexture = this.powerHolder.getPower().value().getBackground();
        var texture = backgroundTexture != null ? backgroundTexture.getTexture(DataContext.forPower(minecraft.player, this.powerHolder)) : ResourceLocation.withDefaultNamespace("textures/block/red_wool.png");

        for (int m = -1; m <= 13; ++m) {
            for (int n = -1; n <= 9; ++n) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Objects.requireNonNull(texture), x + 10 + 16 * m, y + 10 + 16 * n, 0.0F, 0.0F, 16, 16, 16, 16, 256, 256);
            }
        }

        this.hovered = null;

        if (this.list != null)
            this.list.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height, float partialTick, boolean overlayActive) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.fill(0, 0, PowersScreen.WINDOW_INSIDE_WIDTH, PowersScreen.WINDOW_INSIDE_HEIGHT, Mth.floor(this.fade * 255.0F) << 24);
        guiGraphics.pose().popMatrix();

        if (this.hovered != null && !overlayActive) {
            var description = this.hovered.getAbility().getProperties().getDescription();

            if (description != null) {
                guiGraphics.setTooltipForNextFrame(this.minecraft.font, this.minecraft.font.split(description.get(this.hovered.isUnlocked()), 150), mouseX, mouseY);
            }
        }

        if (!overlayActive) {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }
    }

    @Nullable
    public static ListPowerTab create(Minecraft minecraft, PowersScreen screen, int tabIndex, PowerHolder powerHolder) {
        PowerTabType[] tabTypes = PowerTabType.values();

        for (PowerTabType tabType : tabTypes) {
            if (tabIndex < tabType.getMax()) {
                return new ListPowerTab(minecraft, screen, tabType, tabIndex, powerHolder);
            }

            tabIndex -= tabType.getMax();
        }

        return null;
    }

    public static class AbilityList extends AbstractSelectionList<ListEntry> {

        private final ListPowerTab parent;
        private final int listWidth;

        public AbilityList(Minecraft minecraft, ListPowerTab screen, int width, int height, int x, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
            this.setPosition(x, y);
            this.populate(screen.powerHolder);
            this.parent = screen;
            this.listWidth = width;
            this.headerHeight = 0;
        }

        @Override
        protected void renderListBackground(GuiGraphics guiGraphics) {

        }

        public void populate(PowerHolder powerHolder) {
            this.clearEntries();

            for (AbilityInstance<?> ability : powerHolder.getAbilities().values()) {
                if (!ability.getAbility().getProperties().isHiddenInGUI()) {
                    this.addEntry(new ListEntry(ability, this, minecraft));
                }
            }
        }

        @Override
        public int getRowWidth() {
            return this.listWidth;
        }

        // TODO?
        @Override
        protected int scrollBarX() {
            return this.getX() + this.listWidth - 9;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }
    }

    public static class ListEntry extends AbstractSelectionList.Entry<ListEntry> {

        private final AbilityInstance<?> abilityInstance;
        private final AbilityList list;
        private final Minecraft minecraft;

        public ListEntry(AbilityInstance<?> abilityInstance, AbilityList list, Minecraft minecraft) {
            this.abilityInstance = abilityInstance;
            this.list = list;
            this.minecraft = minecraft;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,PowersScreen.WIDGETS, left, top, 0, 130 + (this.abilityInstance.isUnlocked() ? (hovering ? 2 : 0) : 1) * 26, width, height, 256, 256);

            if (this.abilityInstance.isUnlocked()) {
                this.abilityInstance.getAbility().getProperties().getIcon().draw(this.minecraft, guiGraphics, DataContext.forAbility(this.minecraft.player, this.abilityInstance), left + 5, top + 5);
            } else {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,PowersScreen.WIDGETS, left + 5, top + 5, 90, 83, 16, 16, 256, 256);

                // TODO
//                if (this.abilityInstance.getAbility().getConditions().isBuyable()) {
//                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,PowersScreen.WIDGETS, left + 14, top + 16, 106, 83, 7, 7);
//                }
            }

            guiGraphics.drawString(this.minecraft.font, Language.getInstance().getVisualOrder(this.minecraft.font.substrByWidth(abilityInstance.getAbility().getDisplayName(), 180)), left + 30, top + 9, this.abilityInstance.isUnlocked() ? (hovering ? 0xfcfc7e : 0x675d49) : 0x332e25, false);

            if (hovering) {
                this.list.parent.hovered = this.abilityInstance;
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.abilityInstance.getAbility().getStateManager().getUnlockingHandler().onClicked(this.minecraft.player, this.abilityInstance);
            return true;
        }
    }

}
