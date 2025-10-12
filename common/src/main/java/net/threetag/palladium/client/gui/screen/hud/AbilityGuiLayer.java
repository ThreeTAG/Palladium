package net.threetag.palladium.client.gui.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.core.registry.GuiLayerRegistry;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.GuiOverlayAbility;

import java.util.Collection;

public class AbilityGuiLayer implements GuiLayerRegistry.GuiLayer {

    public static final AbilityGuiLayer INSTANCE = new AbilityGuiLayer();

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        var width = minecraft.getWindow().getGuiScaledWidth();
        var height = minecraft.getWindow().getGuiScaledHeight();

        Collection<AbilityInstance<GuiOverlayAbility>> abilityInstances = AbilityUtil.getEnabledInstances(minecraft.player, AbilitySerializers.GUI_OVERLAY.get());
        for (AbilityInstance<GuiOverlayAbility> instance : abilityInstances) {
            var texture = instance.getAbility().texture.getTexture(DataContext.forAbility(minecraft.player, instance));

            var textureWidth = instance.getAbility().textureWidth;
            var textureHeight = instance.getAbility().textureHeight;
            var alignment = instance.getAbility().alignment;
            var translate = instance.getAbility().translate;
            var rotate = instance.getAbility().rotate;
            var scale = instance.getAbility().scale;

            guiGraphics.pose().pushMatrix();

            if (alignment.isStretched()) {
                scale = new Vec2(scale.x * (width / (float) textureWidth), scale.y * (height / (float) textureHeight));
            }

            guiGraphics.pose().translate(translate.x + (textureWidth * scale.x) / 2F, translate.y + (textureWidth * scale.y) / 2F);

            if (!alignment.isStretched()) {
                var horizontal = alignment.getHorizontal();
                var vertical = alignment.getVertical();

                if (horizontal > 0) {
                    guiGraphics.pose().translate(horizontal == 1 ? (width - textureWidth) / 2F : width - textureWidth, 0);
                }

                if (vertical > 0) {
                    guiGraphics.pose().translate(0, vertical == 1 ? (height - textureHeight) / 2F : height - textureHeight);
                }
            }

            if (rotate != 0D) {
                guiGraphics.pose().rotate((float) Math.toRadians(rotate));
            }

            renderImage(guiGraphics, texture, scale, textureWidth, textureHeight);
            guiGraphics.pose().popMatrix();
        }
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation texture, Vec2 scale, int textureWidth, int textureHeight) {
        guiGraphics.pose().scale(scale.x, scale.y);
        guiGraphics.blit(texture, -textureWidth / 2, -textureHeight / 2, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }

}
