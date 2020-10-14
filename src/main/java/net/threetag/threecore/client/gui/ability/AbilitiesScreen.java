package net.threetag.threecore.client.gui.ability;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.AbilityHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AbilitiesScreen extends Screen {

    public static final ResourceLocation WINDOW = new ResourceLocation(ThreeCore.MODID, "textures/gui/abilities/window.png");
    public static final ResourceLocation TABS = new ResourceLocation(ThreeCore.MODID, "textures/gui/abilities/tabs.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(ThreeCore.MODID, "textures/gui/abilities/widgets.png");

    private final int guiWidth = 252;
    private final int guiHeight = 196;
    private List<AbilityTabGui> tabs = Lists.newLinkedList();
    private AbilityTabGui selectedTab = null;
    public Screen overlayScreen = null;
    private boolean isScrolling;

    public AbilitiesScreen() {
        super(NarratorChatListener.EMPTY);
        this.tabs.clear();
        this.selectedTab = null;
        AtomicInteger index = new AtomicInteger();
        AbilityHelper.getAbilityContainers(Minecraft.getInstance().player).forEach((container) -> {
            if (!container.getAbilities().isEmpty()) {
                this.tabs.add(AbilityTabGui.create(Minecraft.getInstance(), this, index.get(), container));
                index.getAndIncrement();
            }
        });
        if (!this.tabs.isEmpty())
            this.selectedTab = tabs.get(0);
    }

    @Override
    protected void init() {
        super.init();

        if (this.overlayScreen != null)
            this.overlayScreen.init(this.minecraft, this.width, this.height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        if (type == 0) {
            int i = (this.width - guiWidth) / 2;
            int j = (this.height - guiHeight) / 2;

            if (this.isOverOverlayScreen(mouseX, mouseY)) {
                return this.overlayScreen.mouseClicked(mouseX, mouseY, type);
            } else {
                for (AbilityTabGui tab : this.tabs) {
                    if (tab.isMouseOver(i, j, mouseX, mouseY)) {
                        this.selectedTab = tab;
                        break;
                    }
                }

                if (selectedTab != null) {
                    AbilityTabEntry entry = this.selectedTab.getAbilityHoveredOver((int) (mouseX - i - 9), (int) (mouseY - j - 18), i, j);
                    if (entry != null) {
                        this.overlayScreen = entry.getScreen(this);
                        if (this.overlayScreen != null)
                            this.overlayScreen.init(this.minecraft, this.width, this.height);
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, type);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double x, double y) {
        if (p_mouseDragged_5_ != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(x, y);
            }

            return true;
        }
    }

    @Override
    public boolean keyPressed(int type, int scanCode, int modifiers) {
        return this.overlayScreen == null ? super.keyPressed(type, scanCode, modifiers) : this.overlayScreen.keyPressed(type, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        int i = (this.width - guiWidth) / 2;
        int j = (this.height - guiHeight) / 2;
        this.renderBackground(stack);
        this.renderInside(stack, mouseX, mouseY, i, j);
        this.renderWindow(stack, i, j);
        this.renderToolTips(stack, mouseX, mouseY, i, j);

        if (this.overlayScreen != null) {
            RenderSystem.pushMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.translatef(0, 0, 950);
            this.overlayScreen.render(stack, mouseX, mouseY, partialTicks);
            this.selectedTab.fade = MathHelper.clamp(this.selectedTab.fade + 0.02F, 0, 0.5F);
            RenderSystem.popMatrix();
        }
    }

    public void renderWindow(MatrixStack stack, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.getMinecraft().getTextureManager().bindTexture(WINDOW);
        this.blit(stack, x, y, 0, 0, guiWidth, guiHeight);
        if (this.tabs.size() > 0) {
            this.getMinecraft().getTextureManager().bindTexture(TABS);

            for (AbilityTabGui tab : this.tabs) {
                tab.drawTab(stack, x, y, tab == this.selectedTab);
            }

            RenderSystem.enableRescaleNormal();
            RenderSystem.defaultBlendFunc();

            for (AbilityTabGui tab : this.tabs) {
                tab.drawIcon(stack, x, y);
            }

            RenderSystem.disableBlend();
        }
        //Draw String
        this.font.func_243248_b(stack, new TranslationTextComponent("gui.threecore.abilities"), (float) (x + 8), (float) (y + 6), 4210752);
    }

    private void renderInside(MatrixStack stack, int mouseX, int mouseY, int x, int y) {
        AbilityTabGui tab = this.selectedTab;
        if (tab == null) {
            fill(stack, x + 9, y + 18, x + 9 + AbilityTabGui.innerWidth, y + 18 + AbilityTabGui.innerHeight, -16777216);
            ITextComponent s = new TranslationTextComponent("advancements.empty");
            int i = this.font.getStringWidth(s.getString());
            // TODO Draw String
            this.font.func_243248_b(stack, s, (float) (x + 9 + 117 - i / 2), (float) (y + 18 + 56 - 9 / 2), -1);
            this.font.func_243248_b(stack, new StringTextComponent(":("), (float) (x + 9 + 117 - this.font.getStringWidth(":(") / 2), (float) (y + 18 + 113 - 9), -1);
        } else {
            RenderSystem.pushMatrix();
            RenderSystem.translated((double) x + 9, (double) y + 18, 0.0);
            RenderSystem.enableDepthTest();
            tab.drawContents(stack);
            RenderSystem.popMatrix();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    private void renderToolTips(MatrixStack stack, int mouseX, int mouseY, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            RenderSystem.pushMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.translated(x + 9, y + 18, 400.0);
            this.selectedTab.drawToolTips(stack, mouseX - x - 9, mouseY - y - 18, x, y, this, this.overlayScreen != null);
            RenderSystem.disableDepthTest();
            RenderSystem.popMatrix();
        }

        if (this.overlayScreen == null && this.tabs.size() > 0) {
            for (AbilityTabGui tab : this.tabs) {
                if (tab.isMouseOver(x, y, mouseX, mouseY)) {
                    //render tooltip
                    this.renderTooltip(stack, tab.getTitle(), mouseX, mouseY);
                }
            }
        }
    }

    public boolean isOverOverlayScreen(double mouseX, double mouseY) {
        return overlayScreen != null;
    }
}
