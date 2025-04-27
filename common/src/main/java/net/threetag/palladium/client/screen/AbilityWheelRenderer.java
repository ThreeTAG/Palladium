package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Objects;

public class AbilityWheelRenderer implements OverlayRegistry.IngameOverlay {

    private static final float SPACING_BETWEEN = 5F;

    public static Wheel CURRENT_WHEEL = null;

    @Override
    public void render(Minecraft minecraft, Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
        if (CURRENT_WHEEL != null) {
            int abilityAmount = CURRENT_WHEEL.abilities.size();
            float outerRadius = 100;
            float innerRadius = 50;
            float centerRadius = (outerRadius + innerRadius) / 2F;
            int steps = 90 / abilityAmount;
            var texture = CURRENT_WHEEL.getTexture();

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(minecraft.getWindow().getGuiScaledWidth() / 2F, minecraft.getWindow().getGuiScaledHeight() / 2F, 0);

            if (texture != null) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(200 / 256F, 200 / 256F, 1);
                guiGraphics.blit(Objects.requireNonNull(texture.getTexture(
                                DataContext.forEntity(minecraft.player)
                                        .with(DataContextType.ABILITY_WHEEL_SELECTION, CURRENT_WHEEL.selected + 1))),
                        -128, -128, 0, 0, 256, 256, 256, 256);
                guiGraphics.pose().popPose();
            }

            for (int i = 0; i < abilityAmount; i++) {
                AbilityInstance ability = CURRENT_WHEEL.abilities.get(i);
                double startAngle = (CURRENT_WHEEL.degreesPerSegment * i + SPACING_BETWEEN / 2.0) - 90 - (CURRENT_WHEEL.degreesPerSegment / 2.0);
                double endAngle = (CURRENT_WHEEL.degreesPerSegment * (i + 1) - SPACING_BETWEEN / 2.0) - 90 - (CURRENT_WHEEL.degreesPerSegment / 2.0);
                boolean selected = i == CURRENT_WHEEL.selected;

                if (texture == null) {
                    float outerRadius_ = selected ? outerRadius + 15 : outerRadius;

                    for (int j = 0; j < steps; j++) {
                        double angle1 = Math.toRadians(startAngle + (endAngle - startAngle) * j / steps);
                        double angle2 = Math.toRadians(startAngle + (endAngle - startAngle) * (j + 1) / steps);

                        Vec2 outer1 = new Vec2((float) Math.cos(angle1) * outerRadius_, (float) Math.sin(angle1) * outerRadius_);
                        Vec2 outer2 = new Vec2((float) Math.cos(angle2) * outerRadius_, (float) Math.sin(angle2) * outerRadius_);
                        Vec2 inner1 = new Vec2((float) Math.cos(angle1) * innerRadius, (float) Math.sin(angle1) * innerRadius);
                        Vec2 inner2 = new Vec2((float) Math.cos(angle2) * innerRadius, (float) Math.sin(angle2) * innerRadius);

                        float r = selected ? (ability.isUnlocked() ? 1F : 0.5F) : 0F;
                        float g = selected ? (ability.isUnlocked() ? 1F : 0F) : 0F;
                        float b = selected ? (ability.isUnlocked() ? 1F : 0F) : 0F;
                        renderQuad(minecraft, guiGraphics, inner1, inner2, outer2, outer1, r, g, b);
                        guiGraphics.fill(-1000, -1000, -999, -999, 0xFF000000);
                    }
                }

                double centerAngle = Math.toRadians((endAngle + startAngle) / 2D);
                Vec2 center = new Vec2((float) Math.cos(centerAngle) * centerRadius, (float) Math.sin(centerAngle) * centerRadius);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((int) center.x, (int) center.y, 0);
                float scale = selected && texture == null ? 1.5F : 1F;
                guiGraphics.pose().scale(scale, scale, 1);
                if (!ability.isUnlocked()) {
                    guiGraphics.blit(AbilityBarRenderer.TEXTURE, -8, -8, 42, 74, 18, 18);
                } else {
                    ability.getProperty(Ability.ICON).draw(minecraft, guiGraphics,
                            DataContext.forAbility(minecraft.player, ability).with(DataContextType.ABILITY_WHEEL_HOVERED, selected),
                            -8, -8);
                }
                guiGraphics.pose().popPose();

                if (selected) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(2, 2, 1);
                    if (!ability.isUnlocked()) {
                        guiGraphics.blit(AbilityBarRenderer.TEXTURE, -8, -8, 42, 74, 18, 18);
                    } else {
                        ability.getProperty(Ability.ICON).draw(minecraft, guiGraphics,
                                DataContext.forAbility(minecraft.player, ability).with(DataContextType.ABILITY_WHEEL_DISPLAYED, true),
                                -8, -8);
                    }
                    guiGraphics.pose().popPose();

                    var text = ability.getConfiguration().getDisplayName();
                    int length = minecraft.font.width(text);
                    guiGraphics.drawString(minecraft.font, ability.getConfiguration().getDisplayName(), -(length / 2), (int) outerRadius + 25, 0xFFFFFF);
                }
            }

            guiGraphics.pose().popPose();
        }
    }

    private static void renderQuad(Minecraft minecraft, GuiGraphics guiGraphics, Vec2 v1, Vec2 v2, Vec2 v3, Vec2 v4,
                                   float r, float g, float b) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        VertexConsumer consumer = minecraft.renderBuffers().bufferSource().getBuffer(RenderType.gui());

        float opacity = 0.5F;
        consumer.vertex(matrix, v1.x, v1.y, 0).color(r, g, b, opacity).endVertex();
        consumer.vertex(matrix, v2.x, v2.y, 0).color(r, g, b, opacity).endVertex();
        consumer.vertex(matrix, v3.x, v3.y, 0).color(r, g, b, opacity).endVertex();
        consumer.vertex(matrix, v4.x, v4.y, 0).color(r, g, b, opacity).endVertex();
    }

    public static void setWheel(Wheel wheel) {
        CURRENT_WHEEL = wheel;
    }

    public static final class Wheel {

        private final List<AbilityInstance> abilities;
        private final TextureReference texture;
        private final boolean disableMouseScrolling;
        private final float degreesPerSegment;
        private int selected = -1;
        private AbilityInstance selectedAbility = null;
        private double posX = 0, posY = 0;

        public Wheel(List<AbilityInstance> abilities, TextureReference texture, boolean disableMouseScrolling) {
            this.abilities = abilities;
            this.degreesPerSegment = 360F / abilities.size();
            this.texture = texture;
            this.disableMouseScrolling = disableMouseScrolling;
        }

        public List<AbilityInstance> abilities() {
            return this.abilities;
        }

        public TextureReference getTexture() {
            return this.texture;
        }

        public boolean disablesMouseScrolling() {
            return this.disableMouseScrolling;
        }

        public AbilityInstance getSelectedAbility() {
            return this.selectedAbility;
        }

        public void setFromMouseInput(double dx, double dy) {
            this.posX = Mth.clamp(this.posX + dx, -500, 500);
            this.posY = Mth.clamp(this.posY + dy, -500, 500);

            double angle = Math.atan2(this.posY, this.posX);
            angle = Math.toDegrees(angle);

            while (angle >= 360) {
                angle -= 360;
            }
            while (angle < 0) {
                angle += 360;
            }

            double adjustedAngle = angle + (SPACING_BETWEEN / 2.0) - 180 - (this.degreesPerSegment / 2.0);

            while (adjustedAngle >= 360) {
                adjustedAngle -= 360;
            }
            while (adjustedAngle < 0) {
                adjustedAngle += 360;
            }

            this.selected = Mth.clamp((int) (adjustedAngle / this.degreesPerSegment), 0, this.abilities.size() - 1);
            this.selectedAbility = this.abilities.get(this.selected);
        }

        public void scroll(boolean up) {
            this.selected += up ? 1 : -1;

            if (this.selected < 0) {
                this.selected = this.abilities.size() - 1;
            } else if (this.selected >= this.abilities.size()) {
                this.selected = 0;
            }

            this.selectedAbility = this.abilities.get(Mth.clamp(this.selected, 0, this.abilities.size() - 1));
        }

    }

}
