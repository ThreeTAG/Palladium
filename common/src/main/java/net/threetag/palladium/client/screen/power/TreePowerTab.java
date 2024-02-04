package net.threetag.palladium.client.screen.power;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.network.RequestAbilityBuyScreenMessage;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TreePowerTab extends PowerTab {

    public static final int GRID_SIZE = 50;
    private final List<TreeAbilityWidget> entries = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private double scrollX;
    private double scrollY;
    private int minX = 2147483647;
    private int minY = 2147483647;
    private int maxX = -2147483648;
    private int maxY = -2147483648;
    private boolean centered;

    public TreePowerTab(Minecraft minecraft, PowersScreen powersScreen, PowerTabType tabType, int tabIndex, IPowerHolder powerHolder) {
        super(minecraft, powersScreen, tabType, tabIndex, powerHolder);
        this.populate();
    }

    @Override
    public void populate() {
        this.entries.clear();
        this.connections.clear();
        List<TreeAbilityWidget> root = new LinkedList<>();

        // Create entry for each ability
        for (AbilityEntry ability : this.powerHolder.getAbilities().values()) {
            if (!ability.getProperty(Ability.HIDDEN_IN_GUI)) {
                var widget = new TreeAbilityWidget(this, this.minecraft, this.powerHolder, ability).setPosition(0, 0);
                this.entries.add(widget);

                var pos = ability.getProperty(Ability.GUI_POSITION);
                if (pos != null) {
                    widget.setPositionFixed(pos.x, pos.y);
                }
            }
        }

        // Find parents and children for each
        for (TreeAbilityWidget entry : this.entries) {
            entry.updateRelatives(this.entries);
        }


        // Locate and set first row
        int y = 0;
        for (TreeAbilityWidget entry : this.entries) {
            if (entry.parents.isEmpty()) {
                if (!entry.fixedPosition) {
                    entry.updatePosition(0, y, this);
                    y++;
                }
                root.add(entry);
            }
        }

        int longest = longestRow();

        // Set position for children
        for (int j = 0; j < root.size(); j++) {
            for (TreeAbilityWidget entry : root) {
                for (TreeAbilityWidget child : entry.children) {
                    if (!child.fixedPosition && entry.gridX == child.gridX) {
                        child.setPosition(child.gridX + 1, getFreeYPos(child.gridX + 1, entry.gridY));
                    }
                }
            }
        }

        // Last Adjustments
        for (int x = 0; x < 100; x++) {
            List<TreeAbilityWidget> entries = getEntriesAtX(x);
            for (int n = 0; n < entries.size(); n++) {
                TreeAbilityWidget entry = entries.get(n);
                if (!entry.fixedPosition) {
                    entry.setPosition(entry.gridX, (longest / 2D) - (entries.size() / 2D) + n);
                }
            }
        }

        // Fixing min & max size; make lines
        for (TreeAbilityWidget entry : this.entries) {
            this.minX = (int) Math.min((entry.gridX - 1) * GRID_SIZE, this.minX);
            this.minY = (int) Math.min((entry.gridY - 1) * GRID_SIZE, this.minY);
            this.maxX = (int) Math.max((entry.gridX + 1) * GRID_SIZE, this.maxX);
            this.maxY = (int) Math.max((entry.gridY + 1) * GRID_SIZE, this.maxY);

            for (TreeAbilityWidget child : entry.children) {
                Connection connection = new Connection();
                int startX = toCoord(entry.gridX);
                int startY = toCoord(entry.gridY, 1D / (entry.children.size() + 1) * (entry.children.indexOf(child) + 1));
                int endX = toCoord(child.gridX);
                int endY = toCoord(child.gridY, 1D / (child.parents.size() + 1) * (child.parents.indexOf(entry) + 1));
                connection.addLine(new ConnectionLine(startX, startY, endX, startY));
                connection.addLine(new ConnectionLine(endX, startY, endX, endY));
                connection.color = child.abilityEntry.isUnlocked() ? this.powerHolder.getPower().getPrimaryColor() : this.powerHolder.getPower().getSecondaryColor();
                this.connections.add(connection);
            }
        }
    }

    private int toCoord(double d) {
        return toCoord(d, 0.5D);
    }

    private int toCoord(double d, double height) {
        return (int) ((d - 0.5D) * GRID_SIZE + (GRID_SIZE - 16) / 2D + (16 * height));
    }

    private int longestRow() {
        int l = 0;
        for (int i = 0; i < 100; i++) {
            int k = getEntriesAtX(i).size();
            l = Math.max(l, k);
        }
        return l;
    }

    public List<TreeAbilityWidget> getEntriesAtX(double x) {
        List<TreeAbilityWidget> list = new LinkedList<>();
        for (TreeAbilityWidget entry : this.entries) {
            if (entry.gridX == x) {
                list.add(entry);
            }
        }

        return list;
    }

    public TreeAbilityWidget getEntry(double x, double y) {
        for (TreeAbilityWidget entry : this.entries) {
            if (entry.gridX == x && entry.gridY == y) {
                return entry;
            }
        }

        return null;
    }

    public double getFreeYPos(double x, double y) {
        for (int i = (int) y; i < 100; i++) {
            if (getEntry(x, i) == null) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public void drawContents(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        if (!this.centered) {
            this.scrollX = 117 - (this.maxX + this.minX) / 2D;
            this.scrollY = 56 - (this.maxY + this.minY) / 2D;
            this.centered = true;
        }

        guiGraphics.enableScissor(x, y, x + PowersScreen.WINDOW_INSIDE_WIDTH, y + PowersScreen.WINDOW_INSIDE_HEIGHT);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) x, (float) y, 0.0F);
        TextureReference backgroundTexture = this.powerHolder.getPower().getBackground();
        var texture = backgroundTexture != null ? backgroundTexture.getTexture(DataContext.forPower(minecraft.player, this.powerHolder)) : new ResourceLocation("textures/block/red_wool.png");

        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int m = -1; m <= 15; ++m) {
            for (int n = -1; n <= 11; ++n) {
                guiGraphics.blit(texture, k + 16 * m, l + 16 * n, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        for (Connection connection : this.connections) {
            connection.drawOutlines(this, guiGraphics, i, j);
        }

        for (Connection connection : this.connections) {
            connection.draw(this, guiGraphics, i, j);
        }

        for (TreeAbilityWidget widget : this.entries) {
            widget.drawIcon(this.minecraft, guiGraphics, i + widget.getX() + 16, j + widget.getY() + 13);
        }

        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();
    }

    @Override
    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height, float partialTick, boolean overlayActive) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, -200.0F);
        guiGraphics.fill(0, 0, PowersScreen.WINDOW_INSIDE_WIDTH, PowersScreen.WINDOW_INSIDE_HEIGHT, Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;

        if (!overlayActive) {
            int i = Mth.floor(this.scrollX);
            int j = Mth.floor(this.scrollY);
            if (mouseX > 0 && mouseX < PowersScreen.WINDOW_INSIDE_WIDTH && mouseY > 0 && mouseY < PowersScreen.WINDOW_INSIDE_HEIGHT) {

                for (TreeAbilityWidget widget : this.entries) {
                    if (widget.isMouseOver(i, j, mouseX, mouseY)) {
                        flag = true;
                        widget.drawHover(guiGraphics, i, j, this.fade, width, height);
                        break;
                    }
                }
            }
        }

        guiGraphics.pose().popPose();

        if (!overlayActive) {
            if (flag) {
                this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
            } else {
                this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
            }
        }
    }

    public TreeAbilityWidget getAbilityHoveredOver(int mouseX, int mouseY, int x, int y) {
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < PowersScreen.WINDOW_INSIDE_WIDTH && mouseY > 0 && mouseY < PowersScreen.WINDOW_INSIDE_HEIGHT) {
            for (TreeAbilityWidget entry : this.entries) {
                if (entry.isMouseOver(i, j, mouseX, mouseY)) {
                    return entry;
                }
            }
        }
        return null;
    }

    @Nullable
    public static TreePowerTab create(Minecraft minecraft, PowersScreen screen, int tabIndex, IPowerHolder powerHolder) {
        PowerTabType[] tabTypes = PowerTabType.values();

        for (PowerTabType tabType : tabTypes) {
            if (tabIndex < tabType.getMax()) {
                return new TreePowerTab(minecraft, screen, tabType, tabIndex, powerHolder);
            }

            tabIndex -= tabType.getMax();
        }

        return null;
    }

    public static boolean canBeTree(IPowerHolder holder) {
        return holder.getAbilities().values().stream().filter(entry -> !entry.getProperty(Ability.HIDDEN_IN_GUI)).anyMatch(entry -> {
            List<AbilityEntry> parents = Ability.findParentsWithinHolder(entry.getConfiguration(), holder);
            List<AbilityEntry> children = Ability.findChildrenWithinHolder(entry.getConfiguration(), holder);

            return !parents.isEmpty() || !children.isEmpty();
        });
    }

    public void scroll(double dragX, double dragY) {
        if (this.maxX - this.minX > PowersScreen.WINDOW_INSIDE_WIDTH) {
            this.scrollX = Mth.clamp(this.scrollX + dragX, -(this.maxX - PowersScreen.WINDOW_INSIDE_WIDTH), -this.minX);
        }

        if (this.maxY - this.minY > PowersScreen.WINDOW_INSIDE_HEIGHT) {
            this.scrollY = Mth.clamp(this.scrollY + dragY, -(this.maxY - PowersScreen.WINDOW_INSIDE_HEIGHT), -this.minY);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int i = (this.screen.width - PowersScreen.WINDOW_WIDTH) / 2;
        int j = (this.screen.height - PowersScreen.WINDOW_HEIGHT) / 2;
        TreeAbilityWidget entry = this.getAbilityHoveredOver((int) (mouseX - i - 9), (int) (mouseY - j - 18), i, j);
        if (entry != null && entry.abilityEntry.getConfiguration().isBuyable()) {
            new RequestAbilityBuyScreenMessage(entry.abilityEntry.getReference()).send();
        }
    }

    public static class Connection {

        public Color color = Color.WHITE;
        public List<ConnectionLine> lines = new LinkedList<>();

        public Connection(List<ConnectionLine> lines) {
            this.lines = lines;
        }

        public Connection() {

        }

        public Connection addLine(ConnectionLine line) {
            this.lines.add(line);
            return this;
        }

        public void drawOutlines(TreePowerTab gui, GuiGraphics guiGraphics, int x, int y) {
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, guiGraphics, x, y, true, Color.BLACK);
            }
        }

        public void draw(TreePowerTab gui, GuiGraphics guiGraphics, int x, int y) {
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, guiGraphics, x, y, false, this.color);
            }
        }

    }

    public static class ConnectionLine {

        public int startX, startY, endX, endY;

        public ConnectionLine(int startX, int startY, int endX, int endY) {
            this.startX = Math.min(startX, endX);
            this.startY = Math.min(startY, endY);
            this.endX = Math.max(startX, endX);
            this.endY = Math.max(startY, endY);
        }

        public void draw(TreePowerTab gui, GuiGraphics guiGraphics, int x, int y, boolean outline, Color color) {
            // AARRGGBB
            int colorCode = color.getRGB();
            if (outline) {
                if (this.startY == endY) {
                    //hLine
                    guiGraphics.hLine(x + startX - 2, x + endX + 1, y + startY - 2, colorCode);
                    guiGraphics.hLine(x + startX - 2, x + endX + 1, y + startY + 1, colorCode);
                } else if (this.startX == endX) {
                    //vLine
                    guiGraphics.vLine(x + startX - 2, y + startY - 2, y + endY + 1, colorCode);
                    guiGraphics.vLine(x + startX + 1, y + startY - 2, y + endY + 1, colorCode);
                }
            } else {
                if (this.startY == endY) {
                    guiGraphics.hLine(x + startX - 1, x + endX, y + startY - 1, colorCode);
                    guiGraphics.hLine(x + startX - 1, x + endX, y + startY, colorCode);
                } else if (this.startX == endX) {
                    guiGraphics.vLine(x + startX - 1, y + startY - 1, y + endY, colorCode);
                    guiGraphics.vLine(x + startX, y + startY - 1, y + endY, colorCode);
                }
            }
        }

    }
}
