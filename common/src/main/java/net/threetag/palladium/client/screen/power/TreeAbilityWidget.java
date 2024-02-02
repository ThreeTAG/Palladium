package net.threetag.palladium.client.screen.power;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.context.DataContext;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
    private final IPowerHolder holder;
    public final AbilityEntry abilityEntry;
    private final FormattedCharSequence title;
    private final int width;
    private final List<FormattedCharSequence> description;
    private final Minecraft minecraft;
    List<TreeAbilityWidget> parents = new LinkedList<>();
    List<TreeAbilityWidget> children = new LinkedList<>();
    private int x;
    private int y;
    public double gridX, gridY;
    public boolean fixedPosition = false;

    public TreeAbilityWidget(TreePowerTab tab, Minecraft mc, IPowerHolder holder, AbilityEntry abilityEntry) {
        this.tab = tab;
        this.holder = holder;
        this.abilityEntry = abilityEntry;
        this.minecraft = mc;
        this.title = Language.getInstance().getVisualOrder(mc.font.substrByWidth(abilityEntry.getConfiguration().getDisplayName(), 163));
        int l = 29 + mc.font.width(this.title);
        var description = abilityEntry.getProperty(Ability.DESCRIPTION);
        this.description = Language.getInstance()
                .getVisualOrder(
                        this.findOptimalLines(ComponentUtils.mergeStyles(description != null ? description.get(this.abilityEntry.isUnlocked()).copy() : Component.empty(), Style.EMPTY.withColor(ChatFormatting.WHITE)), l)
                );

        for (FormattedCharSequence formattedCharSequence : this.description) {
            l = Math.max(l, minecraft.font.width(formattedCharSequence));
        }

        this.width = l + 3 + 5;
    }

    public TreeAbilityWidget updatePosition(double x, double y, TreePowerTab tab) {
        this.gridX = x;
        this.gridY = y;
        this.x = (int) (x * TreePowerTab.GRID_SIZE) - 16;
        this.y = (int) (tab.getFreeYPos(x, y) * TreePowerTab.GRID_SIZE) - 13;

        for (TreeAbilityWidget child : this.children) {
            child.updatePosition(this.gridX + 1, y, tab);
        }

        return this;
    }

    public TreeAbilityWidget setPosition(double x, double y) {
        this.gridX = x;
        this.gridY = y;
        this.x = (int) (x * TreePowerTab.GRID_SIZE) - 16;
        this.y = (int) (y * TreePowerTab.GRID_SIZE) - 13;
        return this;
    }

    public TreeAbilityWidget setPositionFixed(double x, double y) {
        this.fixedPosition = true;
        return this.setPosition(x, y);
    }

    public TreeAbilityWidget updateRelatives(Collection<TreeAbilityWidget> list) {
        this.parents.clear();
        this.children.clear();
        List<AbilityEntry> parents = Ability.findParentsWithinHolder(this.abilityEntry.getConfiguration(), this.holder);
        List<AbilityEntry> children = Ability.findChildrenWithinHolder(this.abilityEntry.getConfiguration(), this.holder);

        for (TreeAbilityWidget widget : list) {
            if (!parents.isEmpty()) {
                if (parents.contains(widget.abilityEntry)) {
                    this.parents.add(widget);
                }
            }

            if (!children.isEmpty()) {
                if (children.contains(widget.abilityEntry)) {
                    this.children.add(widget);
                }
            }
        }

        return this;
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
        if (this.abilityEntry.isUnlocked()) {
            this.abilityEntry.getProperty(Ability.ICON).draw(mc, guiGraphics, DataContext.forAbility(mc.player, this.abilityEntry), x, y);
        } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.blit(PowersScreen.WIDGETS, x, y, 90, 83, 16, 16);
        }
    }

    public void drawIcon(Minecraft mc, GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(PowersScreen.WIDGETS, x - 13, y - 13, 0, this.abilityEntry.isUnlocked() ? 78 : 104, 26, 26);
        this.drawDisplayIcon(mc, guiGraphics, x - 8, y - 8);
    }

    public int getWidth() {
        return this.width;
    }

    public void drawHover(GuiGraphics guiGraphics, int x, int y, float fade, int width, int height) {
        boolean bl = width + x + this.x + this.width + 26 >= this.tab.getScreen().width;
        String string = null;
        int i = string == null ? 0 : this.minecraft.font.width(string);
        boolean bl2 = 113 - y - this.y - 26 <= 6 + this.description.size() * 9;
        float f = 0.0F;
        int j = Mth.floor(f * (float) this.width);
        AdvancementWidgetType advancementWidgetType;
        AdvancementWidgetType advancementWidgetType2;
        AdvancementWidgetType advancementWidgetType3;
        if (f >= 1.0F) {
            j = this.width / 2;
            advancementWidgetType = AdvancementWidgetType.OBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.OBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.OBTAINED;
        } else if (j < 2) {
            j = this.width / 2;
            advancementWidgetType = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        } else if (j > this.width - 2) {
            j = this.width / 2;
            advancementWidgetType = AdvancementWidgetType.OBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.OBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        } else {
            advancementWidgetType = AdvancementWidgetType.OBTAINED;
            advancementWidgetType2 = AdvancementWidgetType.UNOBTAINED;
            advancementWidgetType3 = AdvancementWidgetType.UNOBTAINED;
        }

        int k = this.width - j;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        int l = y + this.y;
        int m;
        if (bl) {
            m = x + this.x - this.width + 26 + 6;
        } else {
            m = x + this.x;
        }

        int n = 32 + this.description.size() * 9;
        if (!this.description.isEmpty()) {
            if (bl2) {
                guiGraphics.blitNineSliced(PowersScreen.WIDGETS, m, l + 26 - n, this.width, n, 10, 200, 26, 0, 52);
            } else {
                guiGraphics.blitNineSliced(PowersScreen.WIDGETS, m, l, this.width, n, 10, 200, 26, 0, 52);
            }
        }

        guiGraphics.blit(PowersScreen.WIDGETS, m, l, 0, advancementWidgetType.getIndex() * 26, j, 26);
        guiGraphics.blit(PowersScreen.WIDGETS, m + j, l, 200 - k, advancementWidgetType2.getIndex() * 26, k, 26);
        guiGraphics.blit(PowersScreen.WIDGETS, x + this.x + 3, y + this.y, 0, 78 + advancementWidgetType3.getIndex() * 26, 26, 26);
        if (bl) {
            guiGraphics.drawString(this.minecraft.font, this.title, m + 5, y + this.y + 9, -1);
            if (string != null) {
                guiGraphics.drawString(this.minecraft.font, string, x + this.x - i, y + this.y + 9, -1);
            }
        } else {
            guiGraphics.drawString(this.minecraft.font, this.title, x + this.x + 32, y + this.y + 9, -1);
            if (string != null) {
                guiGraphics.drawString(this.minecraft.font, string, x + this.x + this.width - i - 5, y + this.y + 9, -1);
            }
        }

        if (bl2) {
            for (int o = 0; o < this.description.size(); ++o) {
                guiGraphics.drawString(this.minecraft.font, this.description.get(o), m + 5, l + 26 - n + 7 + o * 9, -5592406);
            }
        } else {
            for (int o = 0; o < this.description.size(); ++o) {
                guiGraphics.drawString(this.minecraft.font, this.description.get(o), m + 5, y + this.y + 9 + 17 + o * 9, -5592406);
            }
        }

        this.drawDisplayIcon(this.minecraft, guiGraphics, x + this.x + 8, y + this.y + 5);
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
