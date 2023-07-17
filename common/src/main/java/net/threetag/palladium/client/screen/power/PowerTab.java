package net.threetag.palladium.client.screen.power;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.icon.IIcon;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PowerTab extends GuiComponent {

    public static final int GRID_SIZE = 50;
    private final Minecraft minecraft;
    private final PowersScreen screen;
    private final PowerTabType type;
    private final int index;
    public final IPowerHolder powerHolder;
    private final IIcon icon;
    private final Component title;
    private final List<AbilityWidget> entries = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private double scrollX;
    private double scrollY;
    private int minX = 2147483647;
    private int minY = 2147483647;
    private int maxX = -2147483648;
    private int maxY = -2147483648;
    public float fade;
    private boolean centered;

    public PowerTab(Minecraft minecraft, PowersScreen powersScreen, PowerTabType tabType, int i, IPowerHolder powerHolder) {
        this.minecraft = minecraft;
        this.screen = powersScreen;
        this.type = tabType;
        this.index = i;
        this.powerHolder = powerHolder;
        this.icon = powerHolder.getPower().getIcon();
        this.title = powerHolder.getPower().getName();
        this.populate();
    }

    public void populate() {
        this.entries.clear();
        this.connections.clear();
        List<AbilityWidget> root = new LinkedList<>();

        // Create entry for each ability
        for (AbilityEntry ability : this.powerHolder.getAbilities().values()) {
            if (!ability.getProperty(Ability.HIDDEN_IN_GUI)) {
                var widget = new AbilityWidget(this, this.minecraft, this.powerHolder, ability).setPosition(0, 0);
                this.entries.add(widget);

                var pos = ability.getProperty(Ability.GUI_POSITION);
                if (pos != null) {
                    widget.setPositionFixed(pos.x, pos.y);
                }
            }
        }

        // Find parents and children for each
        for (AbilityWidget entry : this.entries) {
            entry.updateRelatives(this.entries);
        }


        // Locate and set first row
        int y = 0;
        for (AbilityWidget entry : this.entries) {
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
            for (AbilityWidget entry : root) {
                for (AbilityWidget child : entry.children) {
                    if (!child.fixedPosition && entry.gridX == child.gridX) {
                        child.setPosition(child.gridX + 1, getFreeYPos(child.gridX + 1, entry.gridY));
                    }
                }
            }
        }

        // Last Adjustments
        for (int x = 0; x < 100; x++) {
            List<AbilityWidget> entries = getEntriesAtX(x);
            for (int n = 0; n < entries.size(); n++) {
                AbilityWidget entry = entries.get(n);
                if (!entry.fixedPosition) {
                    entry.setPosition(entry.gridX, (longest / 2D) - (entries.size() / 2D) + n);
                }
            }
        }

        // Fixing min & max size; make lines
        for (AbilityWidget entry : this.entries) {
            this.minX = (int) Math.min((entry.gridX - 1) * GRID_SIZE, this.minX);
            this.minY = (int) Math.min((entry.gridY - 1) * GRID_SIZE, this.minY);
            this.maxX = (int) Math.max((entry.gridX + 1) * GRID_SIZE, this.maxX);
            this.maxY = (int) Math.max((entry.gridY + 1) * GRID_SIZE, this.maxY);

            for (AbilityWidget child : entry.children) {
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

    public List<AbilityWidget> getEntriesAtX(double x) {
        List<AbilityWidget> list = new LinkedList<>();
        for (AbilityWidget entry : this.entries) {
            if (entry.gridX == x) {
                list.add(entry);
            }
        }

        return list;
    }

    public AbilityWidget getEntry(double x, double y) {
        for (AbilityWidget entry : this.entries) {
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

    public PowerTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public Component getTitle() {
        return this.title;
    }

    public void drawTab(PoseStack poseStack, int offsetX, int offsetY, boolean isSelected) {
        this.type.draw(poseStack, this, offsetX, offsetY, isSelected, this.index);
    }

    public void drawIcon(PoseStack poseStack, int offsetX, int offsetY) {
        this.type.drawIcon(poseStack, offsetX, offsetY, this.index, this.icon);
    }

    public void drawContents(PoseStack poseStack) {
        if (!this.centered) {
            this.scrollX = 117 - (this.maxX + this.minX) / 2D;
            this.scrollY = 56 - (this.maxY + this.minY) / 2D;
            this.centered = true;
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 950.0D);
        RenderSystem.enableDepthTest();
        RenderSystem.colorMask(false, false, false, false);
        fill(poseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        poseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        fill(poseStack, PowersScreen.WINDOW_INSIDE_WIDTH, PowersScreen.WINDOW_INSIDE_HEIGHT, 0, 0, -16777216);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        ResourceLocation resourceLocation = this.powerHolder.getPower().getBackground();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        if (resourceLocation != null) {
            RenderSystem.setShaderTexture(0, resourceLocation);
        } else {
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/block/red_wool.png"));
        }

        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int m = -1; m <= 15; ++m) {
            for (int n = -1; n <= 11; ++n) {
                blit(poseStack, k + 16 * m, l + 16 * n, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        for (Connection connection : this.connections) {
            connection.drawOutlines(this, poseStack, i, j);
        }

        for (Connection connection : this.connections) {
            connection.draw(this, poseStack, i, j);
        }

        for (AbilityWidget widget : this.entries) {
            widget.drawIcon(this.minecraft, poseStack, i + widget.getX() + 16, j + widget.getY() + 13);
        }

        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        poseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.colorMask(false, false, false, false);
        fill(poseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        poseStack.popPose();
    }

    public void drawTooltips(PoseStack poseStack, int mouseX, int mouseY, int width, int height, boolean overlayActive) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, -200.0D);
        fill(poseStack, 0, 0, PowersScreen.WINDOW_INSIDE_WIDTH, PowersScreen.WINDOW_INSIDE_HEIGHT, Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;

        if (!overlayActive) {
            int i = Mth.floor(this.scrollX);
            int j = Mth.floor(this.scrollY);
            if (mouseX > 0 && mouseX < PowersScreen.WINDOW_INSIDE_WIDTH && mouseY > 0 && mouseY < PowersScreen.WINDOW_INSIDE_HEIGHT) {

                for (AbilityWidget widget : this.entries) {
                    if (widget.isMouseOver(i, j, mouseX, mouseY)) {
                        flag = true;
                        widget.drawHover(poseStack, i, j, this.fade, width, height);
                        break;
                    }
                }
            }
        }

        poseStack.popPose();

        if (!overlayActive) {
            if (flag) {
                this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
            } else {
                this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
            }
        }
    }

    public AbilityWidget getAbilityHoveredOver(int mouseX, int mouseY, int x, int y) {
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < PowersScreen.WINDOW_INSIDE_WIDTH && mouseY > 0 && mouseY < PowersScreen.WINDOW_INSIDE_HEIGHT) {
            for (AbilityWidget entry : this.entries) {
                if (entry.isMouseOver(i, j, mouseX, mouseY)) {
                    return entry;
                }
            }
        }
        return null;
    }

    public boolean isMouseOver(int offsetX, int offsetY, double mouseX, double mouseY) {
        return this.type.isMouseOver(offsetX, offsetY, this.index, mouseX, mouseY);
    }

    @Nullable
    public static PowerTab create(Minecraft minecraft, PowersScreen screen, int tabIndex, IPowerHolder powerHolder) {
        PowerTabType[] var4 = PowerTabType.values();

        for (PowerTabType tabType : var4) {
            if (tabIndex < tabType.getMax()) {
                return new PowerTab(minecraft, screen, tabType, tabIndex, powerHolder);
            }

            tabIndex -= tabType.getMax();
        }

        return null;
    }

    public void scroll(double dragX, double dragY) {
        if (this.maxX - this.minX > PowersScreen.WINDOW_INSIDE_WIDTH) {
            this.scrollX = Mth.clamp(this.scrollX + dragX, -(this.maxX - PowersScreen.WINDOW_INSIDE_WIDTH), -this.minX);
        }

        if (this.maxY - this.minY > PowersScreen.WINDOW_INSIDE_HEIGHT) {
            this.scrollY = Mth.clamp(this.scrollY + dragY, -(this.maxY - PowersScreen.WINDOW_INSIDE_HEIGHT), -this.minY);
        }

    }

    public PowersScreen getScreen() {
        return this.screen;
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

        public void drawOutlines(PowerTab gui, PoseStack stack, int x, int y) {
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, stack, x, y, true, Color.BLACK);
            }
        }

        public void draw(PowerTab gui, PoseStack stack, int x, int y) {
            for (ConnectionLine lines : this.lines) {
                lines.draw(gui, stack, x, y, false, this.color);
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

        public void draw(PowerTab gui, PoseStack stack, int x, int y, boolean outline, Color color) {
            // AARRGGBB
            int colorCode = color.getRGB();
            if (outline) {
                if (this.startY == endY) {
                    //hLine
                    gui.hLine(stack, x + startX - 2, x + endX + 1, y + startY - 2, colorCode);
                    gui.hLine(stack, x + startX - 2, x + endX + 1, y + startY + 1, colorCode);
                } else if (this.startX == endX) {
                    //vLine
                    gui.vLine(stack, x + startX - 2, y + startY - 2, y + endY + 1, colorCode);
                    gui.vLine(stack, x + startX + 1, y + startY - 2, y + endY + 1, colorCode);
                }
            } else {
                if (this.startY == endY) {
                    gui.hLine(stack, x + startX - 1, x + endX, y + startY - 1, colorCode);
                    gui.hLine(stack, x + startX - 1, x + endX, y + startY, colorCode);
                } else if (this.startX == endX) {
                    gui.vLine(stack, x + startX - 1, y + startY - 1, y + endY, colorCode);
                    gui.vLine(stack, x + startX, y + startY - 1, y + endY, colorCode);
                }
            }
        }

    }
}
