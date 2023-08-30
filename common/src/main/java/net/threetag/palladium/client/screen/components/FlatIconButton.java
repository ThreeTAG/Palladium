package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.icon.IIcon;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FlatIconButton extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(Palladium.MOD_ID, "textures/gui/widgets.png");

    private final IIcon icon;

    protected FlatIconButton(int x, int y, IIcon icon, OnPress onPress, CreateNarration createNarration) {
        super(x, y, 20, 20, Component.empty(), onPress, createNarration);
        this.icon = icon;
    }

    public static Builder flatIcon(IIcon icon, OnPress onPress) {
        return new Builder(icon, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blit(WIDGETS_LOCATION, this.getX(), this.getY(), 0, this.getTextureY(), 20, this.height);
        this.icon.draw(minecraft, guiGraphics, minecraft.player != null ? DataContext.forEntity(minecraft.player) : DataContext.create(), this.getX() + 2, this.getY() + 2);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return i * 20;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {

        private final IIcon icon;
        private final Button.OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private Button.CreateNarration createNarration;

        public Builder(IIcon icon, Button.OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder createNarration(Button.CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public FlatIconButton build() {
            FlatIconButton button = new FlatIconButton(this.x, this.y, this.icon, this.onPress, this.createNarration);
            button.setTooltip(this.tooltip);
            return button;
        }
    }

}
