package net.threetag.threecore.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import net.threetag.threecore.util.icon.TexturedIcon;

public abstract class AbstractAccessoireScreen extends SettingsScreen {

    public static final TexturedIcon TICK_ICON = new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 160, 16, 16, 16);
    public static float ROTATION = 0F;

    public RotationSlider rotationSlider;

    public AbstractAccessoireScreen(Screen parentScreen, ITextComponent title) {
        super(parentScreen, null, title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new ExtendedButton(30, this.height - 30, 100, 18, new TranslationTextComponent("gui.done"), (button) -> this.closeScreen()));
        this.addButton(this.rotationSlider = new RotationSlider(100 + (this.width - 150) / 2, this.height / 2 + this.height / 3 + 10, 100, 10, 0, 360, 180));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderDirtBackground(160, 0);

        this.renderSidebar(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, this.title, 80, 7, 16777215);

        drawEntityOnScreen(150 + (this.width - 150) / 2, this.height / 2 + this.height / 3, this.height / 3, ROTATION, 0F, 0F, this.minecraft.player);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public abstract void renderSidebar(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

    public void renderDirtBackground(int width, int offset) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, this.height, 0.0D).tex(0.0F, (float) this.height / 32.0F + (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, this.height, 0.0D).tex((float) width / 32.0F, (float) this.height / 32.0F + (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, 0.0D, 0.0D).tex((float) width / 32.0F, (float) offset).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0F, (float) offset).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float rotation, float mouseX, float mouseY, LivingEntity p_228187_5_) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.XN.rotationDegrees(10));
        matrixstack.rotate(Vector3f.YP.rotationDegrees(rotation));
        float f2 = p_228187_5_.renderYawOffset;
        float f3 = p_228187_5_.rotationYaw;
        float f4 = p_228187_5_.rotationPitch;
        float f5 = p_228187_5_.prevRotationYawHead;
        float f6 = p_228187_5_.rotationYawHead;
        p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
        p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
        p_228187_5_.rotationPitch = -f1 * 20.0F;
        p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
        p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.renderYawOffset = f2;
        p_228187_5_.rotationYaw = f3;
        p_228187_5_.rotationPitch = f4;
        p_228187_5_.prevRotationYawHead = f5;
        p_228187_5_.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }

    public static class RotationSlider extends Slider {

        public RotationSlider(int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal) {
            super(xPos, yPos, width, height, StringTextComponent.EMPTY, StringTextComponent.EMPTY, minVal, maxVal, currentVal, false, false, null, slider -> {
                ROTATION = (float) (slider.getValue() - 180F);
            });
        }
    }

}
