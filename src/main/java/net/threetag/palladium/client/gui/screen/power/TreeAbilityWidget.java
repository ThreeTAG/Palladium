package net.threetag.palladium.client.gui.screen.power;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static net.threetag.palladium.client.gui.screen.power.PowersScreen.TITLE_BOX_SPRITE;

public class TreeAbilityWidget {

    private static final int HEIGHT = 26;
    private static final int BOX_X = 0;
    private static final int BOX_WIDTH = 200;
    private static final int FRAME_WIDTH = 26;
    private static final int ICON_X = 8;
    private static final int ICON_Y = 5;
    private static final int ICON_WIDTH = 26;
    private static final int TITLE_PADDING_LEFT = 3;
    private static final int TITLE_PADDING_RIGHT = 5;
    private static final int TITLE_X = 32;
    private static final int TITLE_Y = 9;
    private static final int TITLE_MAX_WIDTH = 163;
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    private final TreePowerTab tab;
    public final AbilityInstance<?> abilityInstance;
    private final List<FormattedCharSequence> titleLines;
    private final int width;
    private final List<FormattedCharSequence> description;
    private final Minecraft minecraft;
    List<TreeAbilityWidget> parents = new LinkedList<>();
    List<TreeAbilityWidget> children = new LinkedList<>();
    private int x;
    private int y;
    public double gridX, gridY;
    public boolean fixedPosition = false;
    public boolean parentsUnlocked = true;

    public TreeAbilityWidget(TreePowerTab tab, Minecraft mc, AbilityInstance<?> abilityInstance) {
        this.tab = tab;
        this.abilityInstance = abilityInstance;
        this.minecraft = mc;
        this.titleLines = minecraft.font.split(abilityInstance.getAbility().getDisplayName(), 163);
        int i = Math.max(this.titleLines.stream().mapToInt(minecraft.font::width).max().orElse(0), 80);
        int k = 29 + i;
        var description = abilityInstance.getAbility().getProperties().getDescription();
        this.description = Language.getInstance()
                .getVisualOrder(
                        this.findOptimalLines(ComponentUtils.mergeStyles(description != null ? description.get(this.abilityInstance.isUnlocked()).copy() : Component.empty(), Style.EMPTY.withColor(ChatFormatting.WHITE)), k)
                );

        for (FormattedCharSequence formattedCharSequence : this.description) {
            k = Math.max(k, minecraft.font.width(formattedCharSequence));
        }

        this.width = k + 3 + 5;
    }

    public void updatePosition(double x, double y, TreePowerTab tab) {
        this.gridX = x;
        this.gridY = y;
        this.x = (int) (x * TreePowerTab.GRID_SIZE) - 16;
        this.y = (int) (tab.getFreeYPos(x, y) * TreePowerTab.GRID_SIZE) - 13;

        for (TreeAbilityWidget child : this.children) {
            child.updatePosition(this.gridX + 1, y, tab);
        }

    }

    public TreeAbilityWidget setPosition(double x, double y) {
        this.gridX = x;
        this.gridY = y;
        this.x = (int) (x * TreePowerTab.GRID_SIZE) - 16;
        this.y = (int) (y * TreePowerTab.GRID_SIZE) - 13;
        return this;
    }

    public void setPositionFixed(double x, double y) {
        this.fixedPosition = true;
        this.setPosition(x, y);
    }

    public void updateRelatives(Collection<TreeAbilityWidget> list) {
        List<? extends AbilityInstance<?>> parents = this.abilityInstance.getAbility().getStateManager().getUnlockingHandler().getParentAbilities().stream()
                .map(reference -> reference.getInstance(this.minecraft.player, this.abilityInstance.getHolder())).toList();

        if (!parents.isEmpty()) {
            for (TreeAbilityWidget widget : list) {
                if (parents.contains(widget.abilityInstance)) {
                    this.parents.add(widget);
                    widget.children.add(this);

                    this.parentsUnlocked &= widget.abilityInstance.isUnlocked();
                }
            }
        }
    }

    private static float getMaxWidth(StringSplitter manager, List<FormattedText> text) {
        return (float) text.stream().mapToDouble(manager::stringWidth).max().orElse(0.0);
    }

    private List<FormattedText> findOptimalLines(Component component, int maxWidth) {
        StringSplitter stringSplitter = this.minecraft.font.getSplitter();
        List<FormattedText> list = null;
        float f = Float.MAX_VALUE;

        for (int i : TEST_SPLIT_OFFSETS) {
            List<FormattedText> list2 = stringSplitter.splitLines(component, maxWidth - i, Style.EMPTY);
            float g = Math.abs(getMaxWidth(stringSplitter, list2) - (float) maxWidth);
            if (g <= 10.0F) {
                return list2;
            }

            if (g < f) {
                f = g;
                list = list2;
            }
        }

        return list;
    }

    public void drawDisplayIcon(Minecraft mc, GuiGraphics guiGraphics, int x, int y) {
        if (this.abilityInstance.isUnlocked()) {
            IconRenderer.drawIcon(this.abilityInstance.getAbility().getProperties().getIcon(), mc, guiGraphics, DataContext.forAbility(mc.player, this.abilityInstance), x, y);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, PowersScreen.WIDGETS, x, y, 90, 83, 16, 16, 256, 256);
        }
    }

    public void drawIcon(Minecraft mc, GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, PowersScreen.WIDGETS, x - 13, y - 13, 0, this.abilityInstance.isUnlocked() ? 78 : 104, 26, 26, 256, 256);
        this.drawDisplayIcon(mc, guiGraphics, x - 8, y - 8);
    }

    public int getWidth() {
        return this.width;
    }

    public void drawHover(GuiGraphics guiGraphics, int x, int y, float fade, int width, int height) {
        int i = 9 * this.titleLines.size() + 9 + 8;
        int j = y + this.y + (26 - i) / 2;
        int k = j + i;
        int l = this.description.size() * 9;
        int m = 6 + l;
        boolean bl = width + x + this.x + this.width + 26 >= this.tab.getScreen().width;
        boolean bl2 = k + m >= 113;
        float f = 0.0F;
        int o = Mth.floor(f * (float) this.width);

        AdvancementWidgetType advancementWidgetType;
        AdvancementWidgetType advancementWidgetType2;
        AdvancementWidgetType advancementWidgetType3;

        if (o < 2) {
            o = this.width / 2;
            advancementWidgetType = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        } else if (o > this.width - 2) {
            o = this.width / 2;
            advancementWidgetType = AdvancementWidgetType.OBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.OBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        } else {
            advancementWidgetType = AdvancementWidgetType.OBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        }

        int p = this.width - o;
        int q;
        if (bl) {
            q = x + this.x - this.width + 26 + 6;
        } else {
            q = x + this.x;
        }

        int r = i + m;
        if (!this.description.isEmpty()) {
            if (bl2) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, q, k - r, this.width, r);
            } else {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, q, j, this.width, r);
            }
        }

        if (advancementWidgetType != advancementWidgetType2) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, advancementWidgetType.boxSprite(), 200, i, 0, 0, q, j, o, i);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, advancementWidgetType2.boxSprite(), 200, i, 200 - p, 0, q + o, j, p, i);
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, advancementWidgetType.boxSprite(), q, j, this.width, i);
        }

        int s = q + 5;
        if (bl) {
            this.drawMultilineText(guiGraphics, this.titleLines, s, j + 9, -1);
        } else {
            this.drawMultilineText(guiGraphics, this.titleLines, x + this.x + 32, j + 9, -1);
        }

        if (bl2) {
            this.drawMultilineText(guiGraphics, this.description, s, j - l + 1, -16711936);
        } else {
            this.drawMultilineText(guiGraphics, this.description, s, k, -16711936);
        }

        this.drawIcon(this.minecraft, guiGraphics, x + this.x + 16, y + this.y + 13);
    }

    private void drawMultilineText(GuiGraphics guiGraphics, List<FormattedCharSequence> text, int x, int y, int color) {
        Font font = this.minecraft.font;

        for (int i = 0; i < text.size(); i++) {
            guiGraphics.drawString(font, text.get(i), x, y + i * 9, color);
        }
    }

    public boolean isMouseOver(int x, int y, int mouseX, int mouseY) {
        int i = x + this.x;
        int j = i + 26;
        int k = y + this.y;
        int l = k + 26;
        return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= l;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

}
