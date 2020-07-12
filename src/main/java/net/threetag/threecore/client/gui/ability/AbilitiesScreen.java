package net.threetag.threecore.client.gui.ability;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.IAbilityContainer;

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
        AbilityHelper.getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(Minecraft.getInstance().player);
            if (container != null && !container.getAbilities().isEmpty()) {
                this.tabs.add(AbilityTabGui.create(Minecraft.getInstance(), this, index.get(), container));
                index.getAndIncrement();
            }
        });
        if(!this.tabs.isEmpty())
            this.selectedTab = tabs.get(0);
    }

    //Init
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        //Init Overlay Screen
        if (this.overlayScreen != null)
            this.overlayScreen.func_231158_b_(this.field_230706_i_, this.field_230708_k_, this.field_230709_l_); //minecraft, width, height
    }

    //Mouse Clicked
    @Override
    public boolean func_231044_a_(double mouseX, double mouseY, int type) {
        if (type == 0) {
            int i = (this.field_230708_k_ - guiWidth) / 2;
            int j = (this.field_230709_l_ - guiHeight) / 2;

            if (this.isOverOverlayScreen(mouseX, mouseY)) {
                return this.overlayScreen.func_231044_a_(mouseX, mouseY, type);
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
                            this.overlayScreen.func_231158_b_(this.field_230706_i_, this.field_230708_k_, this.field_230709_l_);
                    }
                }
            }
        }
        return super.func_231044_a_(mouseX, mouseY, type);
    }

    //Mouse Dragged
    @Override
    public boolean func_231045_a_(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double x, double y) {
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

    //KeyPressed
    @Override
    public boolean func_231046_a_(int type, int scanCode, int p_keyPressed_3_) {
        return this.overlayScreen == null ? super.func_231046_a_(type, scanCode, p_keyPressed_3_) : this.overlayScreen.func_231046_a_(type, scanCode, p_keyPressed_3_);
    }

    //Render
    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);

        int i = (this.field_230708_k_ - guiWidth) / 2;
        int j = (this.field_230709_l_ - guiHeight) / 2;
        this.func_230446_a_(stack); //renderBackground
        this.renderInside(stack, mouseX, mouseY, i, j);
        this.renderWindow(stack, i, j);
        this.renderToolTips(stack, mouseX, mouseY, i, j);

        if (this.overlayScreen != null) {
            stack.push();
            RenderSystem.enableDepthTest();
            stack.translate(0, 0, 950);
            this.overlayScreen.func_230430_a_(stack, mouseX, mouseY, partialTicks);
            this.selectedTab.fade = MathHelper.clamp(this.selectedTab.fade + 0.02F, 0, 0.5F);
            stack.pop();
        }
    }

    public void renderWindow(MatrixStack stack, int x, int y) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.field_230706_i_.getTextureManager().bindTexture(WINDOW);
        //blit
        this.func_238474_b_(stack, x, y, 0, 0, guiWidth, guiHeight);
        if (this.tabs.size() > 0) {
            this.field_230706_i_.getTextureManager().bindTexture(TABS);

            for (AbilityTabGui tab : this.tabs) {
                tab.drawTab(stack, x, y, tab == this.selectedTab);
            }

//            RenderSystem.enableRescaleNormal();
            RenderSystem.defaultBlendFunc();

            for (AbilityTabGui tab : this.tabs) {
                tab.drawIcon(stack, x, y);
            }

            RenderSystem.disableBlend();
        }
        //Draw String
        this.field_230712_o_.func_238421_b_(stack, I18n.format("gui.threecore.abilities"), (float) (x + 8), (float) (y + 6), 4210752);
    }

    private void renderInside(MatrixStack stack, int mouseX, int mouseY, int x, int y) {
        AbilityTabGui tab = this.selectedTab;
        if (tab == null) {
            func_238467_a_(stack, x + 9, y + 18, x + 9 + AbilityTabGui.innerWidth, y + 18 + AbilityTabGui.innerHeight, -16777216);
            String s = I18n.format("advancements.empty");
            int i = this.field_230712_o_.getStringWidth(s);
            //Draw String
            this.field_230712_o_.func_238421_b_(stack, s, (float) (x + 9 + 117 - i / 2), (float) (y + 18 + 56 - 9 / 2), -1);
            this.field_230712_o_.func_238421_b_(stack, ":(", (float) (x + 9 + 117 - this.field_230712_o_.getStringWidth(":(") / 2), (float) (y + 18 + 113 - 9), -1);
        } else {
            stack.push();
            stack.translate((double) x + 9, (double) y + 18, 0.0);
            RenderSystem.enableDepthTest();
            tab.drawContents(stack);
            stack.pop();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    private void renderToolTips(MatrixStack stack, int mouseX, int mouseY, int x, int y) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            stack.push();
            RenderSystem.enableDepthTest();
            stack.translate(x + 9, y + 18, 400.0);
            this.selectedTab.drawToolTips(stack, mouseX - x - 9, mouseY - y - 18, x, y, this, this.overlayScreen != null);
            RenderSystem.disableDepthTest();
            stack.pop();
        }

        if (this.overlayScreen == null && this.tabs.size() > 0) {
            for (AbilityTabGui tab : this.tabs) {
                if (tab.isMouseOver(x, y, mouseX, mouseY)) {
                    //render tooltip
                    this.func_238652_a_(stack, tab.getTitle(), mouseX, mouseY);
                }
            }
        }
    }

    public boolean isOverOverlayScreen(double mouseX, double mouseY) {
        return overlayScreen != null;
    }
}
