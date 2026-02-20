package net.threetag.palladium.client.gui.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.customization.CustomizationPreviewComponent;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.logic.context.DataContext;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class CustomizationToast implements Toast {

    private static final Identifier BACKGROUND_SPRITE = Palladium.id("toast/customization");
    public static final String TITLE_TEXT = "customization.toast.title";
    private static final long DISPLAY_TIME = 5000L;

    private Toast.Visibility wantedVisibility;
    private final Customization customization;
    private final SuitStandRenderState suitStandPreview;

    public CustomizationToast(Customization customization) {
        this.wantedVisibility = Visibility.HIDE;
        this.customization = customization;
        this.suitStandPreview = new SuitStandRenderState();
        this.suitStandPreview.entityType = PalladiumEntityTypes.SUIT_STAND.get();
        this.suitStandPreview.showBasePlate = false;
        this.suitStandPreview.showArms = true;
        this.suitStandPreview.color = DyeColor.WHITE;
        this.suitStandPreview.xRot = 0F;
        this.suitStandPreview.bodyRot = 0F;
        this.suitStandPreview.boundingBoxHeight = 1.8F;
        this.suitStandPreview.lightCoords = 15728880;
        this.suitStandPreview.shadowPieces.clear();
        this.suitStandPreview.outlineColor = 0;
        CustomizationPreviewComponent.updateRenderStateForCustomization(this.suitStandPreview, customization, DataContext.create());
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long l) {
        this.wantedVisibility = l >= DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long l) {
        var registryAccess = Objects.requireNonNull(Minecraft.getInstance().level).registryAccess();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        RenderUtil.flashStringColorOverride(-11534256);
        RenderUtil.flashStringShadowOverride(false);
        guiGraphics.drawScrollingString(guiGraphics.textRenderer(), font, Component.translatable(TITLE_TEXT), 33, 33 + 122,7);
        guiGraphics.drawString(font, this.customization.getTitle(registryAccess), 30, 18, -16777216, false);

        ScreenRectangle rectangle = new ScreenRectangle(5, 4, 25, 25);
        rectangle = rectangle.transformMaxBounds(new Matrix3x2f(guiGraphics.pose()));
        var preview = this.customization.getCategory(registryAccess).value().preview();
        var translation = new Vector3f(0, this.suitStandPreview.boundingBoxHeight / 2F, 0).add(preview.translation().toVector3f().div(2));
        var rotation = preview.rotation();
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(180F));
        quaternionf.mul(new Quaternionf()
                .rotationXYZ((float) (rotation.x * (Math.PI / 180.0)), (float) (rotation.y * (Math.PI / 180.0)), (float) (rotation.z * (Math.PI / 180.0))));
        guiGraphics.submitEntityRenderState(this.suitStandPreview, 10, translation, quaternionf, null, rectangle.left(), rectangle.top(), rectangle.right(), rectangle.bottom());
    }
}
