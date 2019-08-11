package com.threetag.threecore.abilities.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AbilityTabGui extends AbstractGui {

    public static final int innerWidth = 234;
    public static final int innerHeight = 169;
    public static final int gridSize = 50;

    private final IAbilityContainer container;
    private final AbilityTabType type;
    private final int index;
    private List<AbilityTabEntry> abilities;
    private List<Connection> connections;
    private boolean centered;
    private double scrollX;
    private double scrollY;
    public float fade;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public AbilityTabGui(IAbilityContainer container, AbilityTabType type, int index) {
        this.container = container;
        this.type = type;
        this.index = index;
        this.abilities = new LinkedList<>();
        this.connections = new LinkedList<>();
        this.init();
    }

    public void init() {
        List<AbilityTabEntry> root = new LinkedList<>();

        // Create entry for each ability
        for (Ability ability : container.getAbilities()) {
            abilities.add(new AbilityTabEntry(ability));
        }

        // Find parents and children for each
        for (AbilityTabEntry entry : abilities) {
            entry.updateRelatives(abilities);
        }


        // Locate and set first row
        int y = 0;
        for (AbilityTabEntry entry : abilities) {
            if (entry.parents.size() <= 0) {
                entry.updatePosition(0, y, this);
                root.add(entry);
                y++;
            }
        }

        int longest = longestRow();

        // Set position for children
        for (int j = 0; j < root.size(); j++) {
            for (AbilityTabEntry entry : root) {
                for (AbilityTabEntry children : entry.children) {
                    if (entry.x == children.x) {
                        children.x++;
                        children.y = getFreeYPos(children.x, entry.y);
                    }
                }
            }
        }

        // Last Adjustments
        for (int x = 0; x < 100; x++) {
            List<AbilityTabEntry> entries = getEntriesAtX(x);
            for (int n = 0; n < entries.size(); n++) {
                AbilityTabEntry entry = entries.get(n);
                entry.y = (longest / 2D) - (entries.size() / 2D) + n;
            }
        }

        // Fixing min & max size; make lines
        for (AbilityTabEntry entry : this.abilities) {
            this.minX = (int) Math.min(entry.x * gridSize, this.minX);
            this.minY = (int) Math.min(entry.y * gridSize, this.minY);
            this.maxX = (int) Math.max(entry.x * gridSize, this.maxX);
            this.maxY = (int) Math.max(entry.y * gridSize, this.maxY);

            for (AbilityTabEntry child : entry.children) {
                Connection connection = new Connection();
                if (entry.children.size() == 1) {
                    int startX = toCoord(entry.x);
                    int startY = toCoord(entry.y, 1D / (entry.children.size() + 1) * (entry.children.indexOf(child) + 1));
                    int endX = toCoord(child.x);
                    int endY = toCoord(child.y, 1D / (child.parents.size() + 1) * (child.parents.indexOf(entry) + 1));

                    connection.addLine(new ConnectionLine(startX, startY, endX, startY));
                    connection.addLine(new ConnectionLine(endX, startY, endX, endY));
                } else {
                    int startX = toCoord(entry.x);
                    int startY = toCoord(entry.y, 1D / (entry.children.size() + 1) * (entry.children.indexOf(child) + 1));
                    int endX = toCoord(child.x);
                    int endY = toCoord(child.y, 1D / (child.parents.size() + 1) * (child.parents.indexOf(entry) + 1));
                    int midX = (startX + endX) / 2;
                    connection.addLine(new ConnectionLine(startX, startY, midX, startY));
                    connection.addLine(new ConnectionLine(midX, startY, midX, endY));
                    connection.addLine(new ConnectionLine(midX, endY, endX, endY));
                }
                Random random = new Random();
                connection.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
                this.connections.add(connection);
            }
        }
    }

    private int toCoord(double d) {
        return toCoord(d, 0.5D);
    }

    private int toCoord(double d, double height) {
        return (int) ((d - 0.5D) * gridSize + (gridSize - 16) / 2D + (16 * height));
    }

    private int longestRow() {
        int l = 0;
        for (int i = 0; i < 100; i++) {
            int k = getEntriesAtX(i).size();
            l = Math.max(l, k);
        }
        return l;
    }

    public List<AbilityTabEntry> getEntriesAtX(double x) {
        List<AbilityTabEntry> list = new LinkedList<>();
        for (AbilityTabEntry entry : this.abilities) {
            if (entry.x == x) {
                list.add(entry);
            }
        }

        return list;
    }

    public AbilityTabEntry getEntry(double x, double y) {
        for (AbilityTabEntry entry : this.abilities) {
            if (entry.x == x && entry.y == y) {
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

    public ITextComponent getTitle() {
        return this.container.getTitle();
    }

    public void drawTab(int x, int y, boolean selected) {
        this.type.draw(this, x, y, selected, this.index);
    }

    public void drawIcon(int x, int y) {
        this.type.drawIcon(x, y, this.index, this.container.getIcon());
    }

    public void drawContents() {
        if (!this.centered) {
            this.scrollX = (double) (innerWidth / 2 - (this.maxX + this.minX) / 2);
            this.scrollY = (double) (innerHeight / 2 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }

        GlStateManager.depthFunc(518);
        fill(0, 0, innerWidth, innerHeight, -16777216);
        GlStateManager.depthFunc(515);

        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/block/red_wool.png"));
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 10; ++j1) {
                blit(k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        for (Connection connection : this.connections) {
            connection.draw(this, i, j);
        }

        for (AbilityTabEntry entry : this.abilities) {
            entry.drawIcon(mc, i + (int) (entry.x * gridSize), j + (int) (entry.y * gridSize));
        }
    }

    public void drawToolTips(int mouseX, int mouseY, int x, int y, AbilityScreen screen, boolean overlayActive) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0.0F, 0.0F, 200.0F);
        fill(0, 0, innerWidth, innerHeight, MathHelper.floor(this.fade * 255.0F) << 24);
        boolean flag = false;

        if(!overlayActive) {
            int i = MathHelper.floor(this.scrollX);
            int j = MathHelper.floor(this.scrollY);
            if (mouseX > 0 && mouseX < innerWidth && mouseY > 0 && mouseY < innerHeight) {
                for (AbilityTabEntry entry : this.abilities) {
                    if (entry.isMouseOver(i, j, mouseX, mouseY)) {
                        flag = true;
                        entry.drawHover(i, j, this.fade, x, y, screen);
                        break;
                    }
                }
            }
        }

        GlStateManager.popMatrix();
        if(!overlayActive) {
            if (flag) {
                this.fade = MathHelper.clamp(this.fade + 0.02F, 0.0F, 0.3F);
            } else {
                this.fade = MathHelper.clamp(this.fade - 0.04F, 0.0F, 1.0F);
            }
        }
    }

    public AbilityTabEntry getAbilityHoveredOver(int mouseX, int mouseY, int x, int y) {
        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        if (mouseX > 0 && mouseX < innerWidth && mouseY > 0 && mouseY < innerHeight) {
            for (AbilityTabEntry entry : this.abilities) {
                if (entry.isMouseOver(i, j, mouseX, mouseY)) {
                    return entry;
                }
            }
        }
        return null;
    }

    public boolean isMouseOver(int mouseX, int mouseY, double p_195627_3_, double p_195627_5_) {
        return this.type.isMouseOver(mouseX, mouseY, this.index, p_195627_3_, p_195627_5_);
    }

    public void scroll(double x, double y) {
        if (this.maxX - this.minX > innerWidth) {
            this.scrollX = MathHelper.clamp(this.scrollX + x, (double) (-(this.maxX - innerWidth)), 0.0D);
        }

        if (this.maxY - this.minY > innerHeight) {
            this.scrollY = MathHelper.clamp(this.scrollY + y, (double) (-(this.maxY - innerHeight)), 0.0D);
        }
    }

    public static AbilityTabGui create(Minecraft minecraft, AbilityScreen screen, int index, IAbilityContainer container) {
        for (AbilityTabType tabType : AbilityTabType.values()) {
            if ((index % AbilityTabType.MAX_TABS) < tabType.getMax()) {
                return new AbilityTabGui(container, tabType, index % AbilityTabType.MAX_TABS);
            }

            index -= tabType.getMax();
        }

        return null;
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

        public void draw(AbilityTabGui gui, int x, int y) {
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, x, y, true, Color.BLACK);
            }
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, x, y, false, this.color);
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

        public void draw(AbilityTabGui gui, int x, int y, boolean outline, Color color) {
            // AARRGGBB
            int colorCode = color.getRGB();
            if (outline) {
                if (this.startY == endY) {
                    gui.hLine(x + startX - 2, x + endX + 1, y + startY - 2, colorCode);
                    gui.hLine(x + startX - 2, x + endX + 1, y + startY + 1, colorCode);
                } else if (this.startX == endX) {
                    gui.vLine(x + startX - 2, y + startY - 2, y + endY + 1, colorCode);
                    gui.vLine(x + startX + 1, y + startY - 2, y + endY + 1, colorCode);
                }
            } else {
                if (this.startY == endY) {
                    gui.hLine(x + startX - 1, x + endX, y + startY - 1, colorCode);
                    gui.hLine(x + startX - 1, x + endX, y + startY, colorCode);
                } else if (this.startX == endX) {
                    gui.vLine(x + startX - 1, y + startY - 1, y + endY, colorCode);
                    gui.vLine(x + startX, y + startY - 1, y + endY, colorCode);
                }
            }
        }

    }

}