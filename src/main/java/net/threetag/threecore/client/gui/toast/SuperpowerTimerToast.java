package net.threetag.threecore.client.gui.toast;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.DefaultAbilityContainer;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.client.renderer.AbilityBarRenderer;

@OnlyIn(Dist.CLIENT)
public class SuperpowerTimerToast implements IToast {

    private final ResourceLocation containerId;

    public SuperpowerTimerToast(ResourceLocation containerId) {
        this.containerId = containerId;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui guiToast, long l) {
        IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(Minecraft.getInstance().player, this.containerId);

        if (!(container instanceof DefaultAbilityContainer) || container.isObsolete() || ((DefaultAbilityContainer) container).getMaxLifetime() < 0) {
            return Visibility.HIDE;
        }

        guiToast.getMinecraft().getTextureManager().bindTexture(AbilityBarRenderer.TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        guiToast.blit(matrixStack, 0, 0, 0, 224, 160, 32);

        guiToast.getMinecraft().fontRenderer.func_243248_b(matrixStack, container.getTitle(), 30.0F, 7F, -16777216);
        guiToast.getMinecraft().fontRenderer.func_243248_b(matrixStack, container.getSubtitle(), 30.0F, 18.0F, TextFormatting.GRAY.getColor());

        container.getIcon().draw(guiToast.getMinecraft(), matrixStack, 8, 8);

        return Visibility.SHOW;
    }

    public static void add(ResourceLocation containerId) {
        ToastGui toastGui = Minecraft.getInstance().getToastGui();
        if (toastGui != null) {
            for (ToastGui.ToastInstance<?> instance : toastGui.visible) {
                if (instance != null && instance.toast instanceof SuperpowerTimerToast && ((SuperpowerTimerToast) instance.toast).containerId == containerId) {
                    return;
                }
            }
            toastGui.add(new SuperpowerTimerToast(containerId));
        }
    }
}
