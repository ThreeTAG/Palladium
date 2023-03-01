package net.threetag.palladium.client.screen.power;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.components.IconButton;
import net.threetag.palladium.network.RequestAbilityBuyScreenMessage;
import net.threetag.palladium.power.PowerHandler;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladiumcore.event.ScreenEvents;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PowersScreen extends Screen {

    public static final ResourceLocation WINDOW = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/window.png");
    public static final ResourceLocation TABS = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/tabs.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(Palladium.MOD_ID, "textures/gui/powers/widgets.png");

    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 196;
    private static final int WINDOW_INSIDE_X = 9;
    private static final int WINDOW_INSIDE_Y = 18;
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
    private PowerTab selectedTab;
    private boolean isScrolling;
    private static int tabPage;
    private static int maxPages;
    public Screen overlayScreen = null;

    public PowersScreen() {
        super(Component.empty());
    }

    @SuppressWarnings("rawtypes")
    public static void register() {
        ScreenEvents.INIT_POST.register((screen) -> {

            int abilityButtonXPos = -1;
            int abilityButtonYPos = -1;

            if (screen instanceof InventoryScreen || screen.getClass().toString().equals("class top.theillusivec4.curios.client.gui.CuriosScreen")) {
                abilityButtonXPos = ((AbstractContainerScreen) screen).leftPos + 134;
                abilityButtonYPos = screen.height / 2 - 23;
            } else if (screen instanceof CreativeModeInventoryScreen) {
                abilityButtonXPos = ((AbstractContainerScreen) screen).leftPos + 148;
                abilityButtonYPos = screen.height / 2 - 50;
            }

            if (abilityButtonXPos > 0 && abilityButtonYPos > 0) {
                int finalAbilityButtonXPos = abilityButtonXPos;
                int finalAbilityButtonYPos = abilityButtonYPos;
                screen.addRenderableWidget(new IconButton(finalAbilityButtonXPos, finalAbilityButtonYPos, new ItemIcon(ItemStack.EMPTY), b -> Minecraft.getInstance().setScreen(new PowersScreen()), (button, matrixStack, mouseX, mouseY) -> screen.renderTooltip(matrixStack, TITLE, mouseX, mouseY)) {
                    @Override
                    public IIcon getIcon() {
                        List<IIcon> icons = Lists.newArrayList();
                        Minecraft mc = Minecraft.getInstance();
                        PowerManager.getPowerHandler(mc.player).ifPresent(handler -> handler.getPowerHolders().values().stream().filter(holder -> !holder.getPower().isHidden() && holder.getAbilities().values().stream().anyMatch(en -> !en.getProperty(Ability.HIDDEN))).forEach(holder -> icons.add(holder.getPower().getIcon())));
                        if (icons.size() <= 0) {
                            icons.add(new ItemIcon(Blocks.BARRIER));
                        }
                        int i = (mc.player.tickCount / 20) % icons.size();
                        return icons.get(i);
                    }

                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                        this.setPosition(finalAbilityButtonXPos, finalAbilityButtonYPos);
                        this.visible = !(screen instanceof CreativeModeInventoryScreen) || ((CreativeModeInventoryScreen) screen).getSelectedTab() == CreativeModeTab.TAB_INVENTORY.getId();
                        this.active = this.visible && PowerManager.getPowerHandler(Minecraft.getInstance().player).orElse(new PowerHandler(null)).getPowerHolders().size() > 0;
                        super.render(matrixStack, mouseX, mouseY, partialTicks);
                    }
                });
            }
        });
    }

    @Override
    protected void init() {
        this.tabs.clear();
        this.selectedTab = null;

        AtomicInteger i = new AtomicInteger();
        PowerManager.getPowerHandler(this.minecraft.player).ifPresent(handler -> handler.getPowerHolders().values().forEach(holder -> {
            if (!holder.getPower().isHidden() && holder.getAbilities().values().stream().anyMatch(en -> !en.getProperty(Ability.HIDDEN))) {
                this.tabs.add(PowerTab.create(this.minecraft, this, i.getAndIncrement(), holder));
            }
        }));

        if (this.tabs.size() > PowerTabType.MAX_TABS) {
            int guiLeft = (this.width - WINDOW_WIDTH) / 2;
            int guiTop = (this.height - WINDOW_HEIGHT) / 2;
            this.addRenderableWidget(new Button(guiLeft, guiTop - 50, 20, 20, Component.literal("<"), (b) -> {
                tabPage = Math.max(tabPage - 1, 0);
            }));
            this.addRenderableWidget(new Button(guiLeft + WINDOW_WIDTH - 20, guiTop - 50, 20, 20, Component.literal(">"), (b) -> {
                tabPage = Math.min(tabPage + 1, maxPages);
            }));
            maxPages = this.tabs.size() / PowerTabType.MAX_TABS;
        }

        if (!this.tabs.isEmpty()) {
            this.selectedTab = tabs.get(0);
        }

        if (this.overlayScreen != null) {
            this.overlayScreen.init(this.minecraft, this.width, this.height);
        }
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
                        this.selectedTab = powerTab;
                        break;
                    }
                }

                if (selectedTab != null) {
                    AbilityWidget entry = this.selectedTab.getAbilityHoveredOver((int) (mouseX - i - 9), (int) (mouseY - j - 18), i, j);
                    if (entry != null && entry.abilityEntry.getConfiguration().isBuyable()) {
                        new RequestAbilityBuyScreenMessage(entry.abilityEntry.getReference()).send();
                    }
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - WINDOW_WIDTH) / 2;
        int j = (this.height - WINDOW_HEIGHT) / 2;
        this.renderBackground(poseStack);
        this.renderInside(poseStack, mouseX, mouseY, i, j);
        this.renderWindow(poseStack, i, j);
        this.renderTooltips(poseStack, mouseX, mouseY, i, j);

        if (this.selectedTab != null && this.overlayScreen != null) {
            this.overlayScreen.render(poseStack, mouseX, mouseY, partialTick);
            this.selectedTab.fade = Mth.clamp(this.selectedTab.fade + 0.02F, 0, 0.5F);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(dragX, dragY);
            }

            return true;
        }
    }

    private void renderInside(PoseStack poseStack, int mouseX, int mouseY, int offsetX, int offsetY) {
        PowerTab tab = this.selectedTab;
        if (tab == null) {
            fill(poseStack, offsetX + WINDOW_INSIDE_X, offsetY + WINDOW_INSIDE_Y, offsetX + WINDOW_INSIDE_X + WINDOW_INSIDE_WIDTH, offsetY + WINDOW_INSIDE_Y + WINDOW_INSIDE_HEIGHT, -16777216);
            int i = offsetX + WINDOW_INSIDE_X + 117;
            Font var10001 = this.font;
            Component var10002 = NO_ADVANCEMENTS_LABEL;
            int var10004 = offsetY + WINDOW_INSIDE_Y + 56;
            Objects.requireNonNull(this.font);
            drawCenteredString(poseStack, var10001, var10002, i, var10004 - 9 / 2, -1);
            var10001 = this.font;
            var10002 = VERY_SAD_LABEL;
            var10004 = offsetY + 18 + WINDOW_INSIDE_HEIGHT;
            Objects.requireNonNull(this.font);
            drawCenteredString(poseStack, var10001, var10002, i, var10004 - 9, -1);
        } else {
            PoseStack poseStack2 = RenderSystem.getModelViewStack();
            poseStack2.pushPose();
            poseStack2.translate(offsetX + WINDOW_INSIDE_X, offsetY + WINDOW_INSIDE_Y, 0.0D);
            RenderSystem.applyModelViewMatrix();
            tab.drawContents(poseStack);
            poseStack2.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.disableDepthTest();
        }
    }

    public void renderWindow(PoseStack poseStack, int offsetX, int offsetY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW);
        this.blit(poseStack, offsetX, offsetY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (this.tabs.size() > 1) {
            RenderSystem.setShaderTexture(0, TABS);

            for (PowerTab tab : this.tabs) {
                tab.drawTab(poseStack, offsetX, offsetY, tab == this.selectedTab);
            }

            RenderSystem.defaultBlendFunc();

            for (PowerTab tab : this.tabs) {
                tab.drawIcon(poseStack, offsetX, offsetY);
            }

            RenderSystem.disableBlend();
        }

        this.font.draw(poseStack, TITLE, (float) (offsetX + 8), (float) (offsetY + 6), 4210752);
    }

    private void renderTooltips(PoseStack poseStack, int mouseX, int mouseY, int offsetX, int offsetY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            poseStack.pushPose();
            poseStack.translate(offsetX + WINDOW_INSIDE_X, offsetY + WINDOW_INSIDE_Y, 400.0D);
            this.selectedTab.drawTooltips(poseStack, mouseX - offsetX - WINDOW_INSIDE_X, mouseY - offsetY - WINDOW_INSIDE_Y, offsetX, offsetY, this.overlayScreen != null);
            poseStack.popPose();
        }

        if (this.tabs.size() > 1) {
            for (PowerTab tab : this.tabs) {
                if (tab.isMouseOver(offsetX, offsetY, mouseX, mouseY)) {
                    this.renderTooltip(poseStack, tab.getTitle(), mouseX, mouseY);
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
        this.overlayScreen.init(this.minecraft, this.width, this.height);
    }

    public boolean isOverOverlayScreen(double mouseX, double mouseY) {
        return overlayScreen != null;
    }

}
