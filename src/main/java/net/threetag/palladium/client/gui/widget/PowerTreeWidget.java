package net.threetag.palladium.client.gui.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.EasingType;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.DelayedRenderCallReceiver;
import net.threetag.palladium.client.gui.screen.ModalScreen;
import net.threetag.palladium.client.gui.ui.background.UiBackground;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.client.util.GuiUtil;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.network.BuyAbilityPacket;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.unlocking.BuyableUnlockingHandler;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PowerTreeWidget extends AbstractWidget implements TickableWidget {

    public static final String TRANS_TITLE = "gui.palladium.powers";
    public static final String TRANS_CUSTOMIZE = "gui.palladium.powers.customize";
    public static final String TRANS_KEY_UNLOCK = "gui.palladium.powers.unlock";
    public static final String TRANS_KEY_VERY_SAD_LABEL = "gui.palladium.powers.sad_label";
    public static final String TRANS_KEY_NO_ABILITIES_LABEL = "gui.palladium.powers.empty";
    public static final Identifier SPRITE_ABILITY_FRAME_UNLOCKED = Palladium.id("powers/ability_frame_unlocked");
    public static final Identifier SPRITE_ABILITY_FRAME_LOCKED = Palladium.id("powers/ability_frame_locked");
    public static final Identifier SPRITE_TITLE_BOX = Palladium.id("powers/title_box");
    public static final Identifier SPRITE_VIGNETTE = Palladium.id("powers/vignette");
    public static final Identifier SPRITE_LOCK = Palladium.id("powers/lock");
    public static final int GRID_SIZE = 50;

    private final Screen parent;
    private final UiBackground background;
    private int innerWidth, innerHeight;
    private boolean isDragging = false;
    private int offsetX = 0, offsetY = 0;
    private int fade, fade0;
    private boolean enableFade = false;
    public final List<AbilityElement> abilities = new ArrayList<>();

    public PowerTreeWidget(Screen parent, PowerInstance powerInstance, UiBackground background,
                           int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.parent = parent;
        this.background = background;
        this.innerWidth = Math.max(500, this.getWidth());
        this.innerHeight = Math.max(500, this.getHeight());
        this.populate(powerInstance);
    }

    public void populate(PowerInstance powerInstance) {
        this.abilities.clear();
        if (powerInstance != null) {
            PowerTreePopulator.generateTree(powerInstance, this.abilities);
            this.innerWidth = (int) Math.max(PowerTreePopulator.getHighestXGridValue(this.abilities) * GRID_SIZE + GRID_SIZE,
                    this.getWidth());
            this.innerHeight = (int) Math.max(PowerTreePopulator.getHighestYGridValue(this.abilities) * GRID_SIZE + GRID_SIZE,
                    this.getHeight());

            PowerTreePopulator.center(this.abilities, this.innerWidth, this.innerHeight);
            PowerTreePopulator.generateConnections(this.abilities);
        }
    }

    @Override
    public void tick() {
        this.fade0 = this.fade;

        if (this.enableFade && this.fade < 10) {
            this.fade++;
        } else if (!this.enableFade && this.fade > 0) {
            this.fade--;
        }

        this.enableFade = false;

        if (!this.abilities.isEmpty() && this.abilities.stream().anyMatch(AbilityElement::needsRefresh)) {
            this.populate(this.abilities.getFirst().abilityInstance.getPowerInstance());
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.abilities.isEmpty()) {
            int x = this.getX() + this.getWidth() / 2;
            int y = this.getY() + this.getHeight() / 2;
            guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), RenderUtil.FULL_BLACK);
            GuiUtil.drawCenteredWordWrap(guiGraphics, Component.translatable(TRANS_KEY_NO_ABILITIES_LABEL), x, y - 20, this.getWidth() - 15, -1);
            GuiUtil.drawCenteredWordWrap(guiGraphics, Component.translatable(TRANS_KEY_VERY_SAD_LABEL), x, y + 20, this.getWidth() - 15, -1);
            return;
        }

        guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
        this.renderInner(guiGraphics, this.getX() - this.offsetX, this.getY() - this.offsetY, this.innerWidth,
                this.innerHeight, mouseX, mouseY);
        guiGraphics.disableScissor();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_VIGNETTE, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        float fade = EasingType.IN_OUT_CUBIC.apply(Mth.lerp(partialTick, this.fade0, this.fade) / 10F) / 3F;
        if (fade > 0F) {
            guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), ARGB.colorFromFloat(fade, 0, 0, 0));
        }
    }

    private void renderInner(GuiGraphics guiGraphics, int x, int y, int width, int height, int mouseX, int mouseY) {
        this.background.render(guiGraphics, x, y, width, height);

        for (AbilityElement ability : this.abilities) {
            for (AbilityConnection connection : ability.connections) {
                connection.draw(guiGraphics, x, y, true, Color.BLACK);
                connection.draw(guiGraphics, x, y, false, ability.unlocked ? Color.CYAN : Color.WHITE);
            }
        }

        for (AbilityElement ability : this.abilities) {
            ability.render(guiGraphics, x + ability.getX(), y + ability.getY());

            if (this.parent instanceof DelayedRenderCallReceiver receiver && this.isMouseOverAbility(ability, mouseX, mouseY)) {
                receiver.renderDelayed(gui -> ability.renderHovered(gui, x + ability.getX(), y + ability.getY()));
                this.enableFade = true;
            }
        }
    }

    private boolean isMouseOverAbility(AbilityElement abilityElement, int mouseX, int mouseY) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        int x = this.getX() - this.offsetX + abilityElement.getX();
        int y = this.getY() - this.offsetY + abilityElement.getY();

        return mouseX >= x - 13 && mouseX <= x + 13 && mouseY >= y - 13 && mouseY <= y + 13;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (this.isActive()) {
            if (this.isValidClickButton(event.buttonInfo())) {
                if (this.isMouseOver(event.x(), event.y())) {
                    for (AbilityElement ability : this.abilities) {
                        if (this.isMouseOverAbility(ability, (int) event.x(), (int) event.y())) {
                            this.playDownSound(Minecraft.getInstance().getSoundManager());

                            if (ability.parentsUnlocked && !ability.unlocked) {
                                ability.abilityInstance.getAbility().getStateManager().getUnlockingHandler().onClicked(this.parent.getMinecraft().player, ability.abilityInstance);
                            } else {
                                ability.openModal(this.parent);
                            }

                            return true;
                        }
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (event.button() != InputConstants.MOUSE_BUTTON_LEFT) {
            this.isDragging = false;
            return false;
        } else {
            if (!this.isDragging) {
                this.isDragging = true;
            } else {
                this.drag(mouseX, mouseY);
            }

            return true;
        }
    }

    public void drag(double dragX, double dragY) {
        this.offsetX = (int) Mth.clamp(this.offsetX - dragX, 0, this.innerWidth - this.getWidth());
        this.offsetY = (int) Mth.clamp(this.offsetY - dragY, 0, this.innerHeight - this.getHeight());
    }

    public static class AbilityElement {

        private final Minecraft minecraft = Minecraft.getInstance();
        private final AbilityInstance<?> abilityInstance;
        private final Component title;
        private final Component description;
        private final Icon icon;
        private final boolean unlocked;
        float gridX = 0, gridY = 0;
        boolean fixedPosition = false;
        List<AbilityElement> parents = new LinkedList<>();
        List<AbilityElement> children = new LinkedList<>();
        List<AbilityConnection> connections = new LinkedList<>();
        public boolean parentsUnlocked = true;

        public AbilityElement(AbilityInstance<?> abilityInstance) {
            this.abilityInstance = abilityInstance;
            this.title = abilityInstance.getAbility().getDisplayName();
            this.icon = abilityInstance.getAbility().getProperties().getIcon();
            this.unlocked = abilityInstance.isUnlocked();
            var desc = abilityInstance.getAbility().getProperties().getDescription();
            this.description = desc == null ? Component.empty() : desc.get(this.unlocked);
        }

        public void openModal(Screen parent) {
            this.openModal(parent, null, false);
        }

        public void openModal(Screen parent, @Nullable BuyableUnlockingHandler.Display display, boolean available) {
            if (display == null && this.description.getString().isBlank()) {
                return;
            }

            var modal = new ModalScreen(this.description)
                    .setHeader(this.title)
                    .disableBackgroundRendering();

            if (display != null) {
                var button = TextWithIconButton.textWithIconBuilder(Component.translatable(TRANS_KEY_UNLOCK).append(": " + display.amount() + "x "), display.icon(), s -> {
                    ClientPacketDistributor.sendToServer(new BuyAbilityPacket(this.getReference()));
                    modal.onClose();
                    Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).playSound(SoundEvents.PLAYER_LEVELUP, 1F, 1F);
                }).bounds(0, 0, 20, 20).tooltip(Tooltip.create(display.description())).build();
                button.active = available;
                modal.addButton(button);
            }

            ClientHooks.pushGuiLayer(parent.getMinecraft(), modal);
        }

        public boolean needsRefresh() {
            return this.unlocked != this.abilityInstance.isUnlocked();
        }

        public AbilityElement setGridPos(float x, float y) {
            this.gridX = x;
            this.gridY = y;
            return this;
        }

        public AbilityElement setGridPosFixed(float x, float y) {
            this.fixedPosition = true;
            return this.setGridPos(x, y);
        }

        public void updatePosition(float x, float y) {
            if (!this.fixedPosition) {
                this.gridX = x;
                this.gridY = y;
            }

            for (AbilityElement child : this.children) {
                child.updatePosition(this.gridX + 1, y);
            }
        }

        public void render(GuiGraphics guiGraphics, int x, int y) {
            if (this.unlocked) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_ABILITY_FRAME_UNLOCKED, x - 13, y - 13, 26, 26);
                IconRenderer.drawIcon(this.icon, this.minecraft, guiGraphics, DataContext.forEntity(this.minecraft.player),
                        x - 8, y - 8);
            } else {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_ABILITY_FRAME_LOCKED, x - 13, y - 13, 26, 26);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_LOCK, x - 13, y - 13, 26, 26);
            }
        }

        public void renderHovered(GuiGraphics guiGraphics, int x, int y) {
            var font = this.minecraft.font;
            int textWidth = font.width(this.title);
            int padding = 3;

            if (PalladiumClientConfig.DEV_MODE.getAsBoolean()) {
                Component abilityRef = Component.literal(this.abilityInstance.getReference().toString());
                int idWidth = font.width(abilityRef);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_TITLE_BOX, x, y - 13,
                        Math.max(idWidth, textWidth) + 13 + (padding * 3), 26 + 10);
                guiGraphics.drawString(font, this.title, x + 13 + padding, y - 4, RenderUtil.FULL_WHITE);
                guiGraphics.drawString(font, abilityRef, x + 13 + padding, y - 4 + 11, RenderUtil.DEFAULT_GRAY, false);
            } else {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_TITLE_BOX, x, y - 13,
                        textWidth + 13 + (padding * 3), 26);
                guiGraphics.drawString(font, this.title, x + 13 + padding, y - 4, RenderUtil.FULL_WHITE);
            }

            this.render(guiGraphics, x, y);
        }

        public AbilityReference getReference() {
            return this.abilityInstance.getReference();
        }

        public int getX() {
            return Math.round(this.gridX * GRID_SIZE + (GRID_SIZE / 2F));
        }

        public int getY() {
            return Math.round(this.gridY * GRID_SIZE + (GRID_SIZE / 2F));
        }

        public void updateRelatives(Collection<AbilityElement> list) {
            List<? extends AbilityInstance<?>> parents = this.abilityInstance.getAbility().getStateManager()
                    .getUnlockingHandler().getParentAbilities().stream()
                    .map(reference -> reference.getInstance(this.minecraft.player,
                            this.abilityInstance.getPowerInstance()))
                    .toList();

            if (!parents.isEmpty()) {
                for (AbilityElement element : list) {
                    if (parents.contains(element.abilityInstance)) {
                        this.parents.add(element);
                        element.children.add(this);

                        this.parentsUnlocked &= element.abilityInstance.isUnlocked();
                    }
                }
            }
        }
    }

    public static class AbilityConnection {

        public AbilityElement parent;
        public List<AbilityElement> children;
        public java.util.Map<AbilityElement, Integer> childInputIndices;

        public AbilityConnection(AbilityElement parent, List<AbilityElement> children,
                                 java.util.Map<AbilityElement, Integer> childInputIndices) {
            this.parent = parent;
            this.children = children;
            this.childInputIndices = childInputIndices;
        }

        public void draw(GuiGraphics guiGraphics, int x, int y, boolean outline, Color color) {
            if (this.children.isEmpty()) {
                return;
            }

            int lineSize = 3;
            int lineDistance = 4;
            int colorCode = color.getRGB();

            int startX = this.parent.getX();
            int startY = this.parent.getY();

            int endX = this.children.getFirst().getX();
            int busX = startX + (endX - startX) / 2;

            int minY = startY;
            int maxY = startY;

            for (AbilityElement child : this.children) {
                int totalDockingHeight = ((child.parents.size() - 1) * lineDistance) + lineSize;
                int childY = child.getY() - (totalDockingHeight / 2) + (child.parents.indexOf(parent) * lineDistance) + 1;
                if (childY < minY)
                    minY = childY;
                if (childY > maxY)
                    maxY = childY;
            }

            if (outline) {
                guiGraphics.fill(x + busX - 2, y + minY - 2, x + busX + 1, y + maxY + 1, colorCode);
                guiGraphics.fill(x + startX - 2, y + startY - 2, x + busX + 1, y + startY + 1, colorCode);
            } else {
                guiGraphics.fill(x + busX - 1, y + minY - 1, x + busX, y + maxY, colorCode);
                guiGraphics.fill(x + startX - 1, y + startY - 1, x + busX, y + startY, colorCode);
            }

            for (AbilityElement child : this.children) {
                int childX = child.getX();
                int totalDockingHeight = ((child.parents.size() - 1) * lineDistance) + lineSize;
                int childY = child.getY() - (totalDockingHeight / 2) + (child.parents.indexOf(parent) * lineDistance) + 1;

                if (outline) {
                    guiGraphics.fill(x + busX - 2, y + childY - 2, x + childX + 1, y + childY + 1, colorCode);
                } else {
                    guiGraphics.fill(x + busX - 1, y + childY - 1, x + childX, y + childY, colorCode);
                }
            }
        }
    }

}
