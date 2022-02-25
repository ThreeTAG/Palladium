package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityColor;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Map;

public class AbilityBarRenderer implements IIngameOverlay {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Palladium.MOD_ID, "textures/gui/ability_bar.png");
    public static AbilityList ABILITY_LIST = null;

    public AbilityBarRenderer() {
        ClientTickEvent.CLIENT_POST.register(instance -> updateCurrentList());
    }

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        Position position = PalladiumConfig.getAbilityBarPosition();

        if (position == Position.HIDDEN || ABILITY_LIST == null) {
            return;
        }

        if (position.top && mc.options.renderDebug) {
            return;
        }

        if (!position.top && mc.screen instanceof ChatScreen) {
            position = position.left ? Position.TOP_LEFT : Position.TOP_RIGHT;
        }

        if (mc.player != null) {
            int indicatorWidth = 52;
            int indicatorHeight = 28;

            poseStack.pushPose();
            translateIndicatorBackground(poseStack, mc.getWindow(), position, indicatorWidth, indicatorHeight);
            renderIndicatorBackground(mc, poseStack, position, TEXTURE, true);
            poseStack.popPose();
            renderIndicatorIcon(mc.getWindow(), position, ABILITY_LIST, indicatorWidth, indicatorHeight, false);


            poseStack.pushPose();
            translateAbilitiesBackground(poseStack, mc.getWindow(), position, indicatorHeight, 24, 112);
            renderAbilitiesBackground(mc, poseStack, position, ABILITY_LIST, TEXTURE);
            poseStack.popPose();
            renderAbilityIcons(mc.getWindow(), position, ABILITY_LIST, indicatorHeight, 24, 112);
            poseStack.pushPose();
            translateAbilitiesBackground(poseStack, mc.getWindow(), position, indicatorHeight, 24, 112);
            renderAbilitiesOverlay(mc, poseStack, position, ABILITY_LIST, TEXTURE);
            poseStack.popPose();
        }
    }

    private static void translateIndicatorBackground(PoseStack poseStack, Window window, Position position, int width, int height) {
        if (!position.top) {
            poseStack.translate(0, window.getGuiScaledHeight() - height, 0);
        }

        if (!position.left) {
            poseStack.translate(window.getGuiScaledWidth() - width, 0, 0);
        }
    }

    private static void renderIndicatorBackground(Minecraft minecraft, PoseStack poseStack, Position position, ResourceLocation texture, boolean showKey) {
        // Background
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.gui.blit(poseStack, 0, 0, position.left ? 52 : 0, position.top ? 28 : 0, 52, 28);
    }

    private static void renderIndicatorIcon(Window window, Position position, AbilityList list, int width, int height, boolean showSwitchKey) {
        int x = 0, y = 5;

        if (!position.top) {
            y += window.getGuiScaledHeight() - height + 2;
        }

        if (!position.left) {
            x = window.getGuiScaledWidth() - width + (showSwitchKey ? 6 : 19);
        } else {
            x = showSwitchKey ? 30 : 17;
        }

        list.power.getIcon().draw(Minecraft.getInstance(), RenderSystem.getModelViewStack(), x, y);
    }

    private static void translateAbilitiesBackground(PoseStack poseStack, Window window, Position position, int indicatorHeight, int abilitiesWidth, int abilitiesHeight) {
        if (position.top) {
            poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, indicatorHeight - 1, 0);
        } else {
            poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, window.getGuiScaledHeight() - indicatorHeight - abilitiesHeight + 1, 0);
        }
    }

    private static void renderAbilitiesBackground(Minecraft minecraft, PoseStack poseStack, Position position, AbilityList list, ResourceLocation texture) {
        boolean showName = minecraft.screen instanceof ChatScreen;

        for (int i = 0; i < 5; i++) {
            Lighting.setupFor3DItems();
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            minecraft.gui.blit(poseStack, 3, i * 22 + 3, 60, 56, 18, 18);

            if (list != null) {
                AbilityEntry entry = list.getAbilities()[i];

                if (entry != null) {
                    minecraft.gui.blit(poseStack, 3, i * 22 + 3, entry.isEnabled() ? 42 : 24, entry.isUnlocked() ? 56 : 74, 18, 18);

                    if (!entry.isUnlocked()) {
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3, 42, 74, 18, 18);
                    }

                    // Ability Name
                    if (showName) {
                        Tesselator tes = Tesselator.getInstance();
                        BufferBuilder bb = tes.getBuilder();
                        Component name = entry.getConfiguration().getDisplayName();
                        int width = minecraft.font.width(name);
                        renderBlackBox(bb, tes, poseStack, minecraft.screen, position.left ? 24 : -width - 10, i * 22 + 5, 10 + width, 14, 0.5F);
                        minecraft.font.draw(poseStack, name, position.left ? 29 : -width - 5, i * 22 + 8, 0xffffffff);
                    }
                }
            }
        }

        // Overlay
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.gui.blit(poseStack, 0, 0, 0, 56, 24, 112);
    }

    private static void renderAbilityIcons(Window window, Position position, AbilityList list, int indicatorHeight, int abilitiesWidth, int abilitiesHeight) {
        int startX, startY;

        if (position.top) {
            startX = !position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0;
            startY = indicatorHeight - 1;
        } else {
            startX = !position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0;
            startY = window.getGuiScaledHeight() - indicatorHeight - abilitiesHeight + 1;
        }

        for (int i = 0; i < list.abilities.length; i++) {
            AbilityEntry ability = list.abilities[i];

            if (ability != null) {
                if (ability.isUnlocked()) {
                    ability.getConfiguration().get(Ability.ICON).draw(Minecraft.getInstance(), RenderSystem.getModelViewStack(), startX + 4, startY + 4 + i * 22);
                } else {
                    // TODO draw lock icon
                }
            }
        }
    }

    private static void renderAbilitiesOverlay(Minecraft minecraft, PoseStack poseStack, Position position, AbilityList list, ResourceLocation texture) {
        // Overlay
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.gui.blit(poseStack, 0, 0, 0, 56, 24, 112);

        // Colored Frames + Keys
        for (int i = 0; i < list.abilities.length; i++) {
            AbilityEntry ability = list.abilities[i];

            if (ability != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                Lighting.setupFor3DItems();
                RenderSystem.enableBlend();

                if(!ability.isUnlocked()) {
                    minecraft.gui.blit(poseStack, 3, i * 22 + 3, 42, 74, 18, 18);
                }

                AbilityColor color = ability.getProperty(Ability.COLOR);
                minecraft.gui.blit(poseStack, 0, i * 22, color.getX(), color.getY(), 24, 24);

                if (ability.getConfiguration().needsKey() && ability.isUnlocked()) {
                    Component key = PalladiumKeyMappings.ABILITY_KEYS[i].getTranslatedKeyMessage();
                    poseStack.pushPose();
                    poseStack.translate(0, 0, minecraft.getItemRenderer().blitOffset + 200);
                    GuiComponent.drawString(poseStack, minecraft.font, key, 5 + 19 - 2 - minecraft.font.width(key), 5 + i * 22 + 6 + 3, 0xffffff);
                    poseStack.popPose();
                }
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderBlackBox(BufferBuilder bb, Tesselator tesselator, PoseStack matrixStack, GuiComponent gui, int x, int y, int width, int height, float opacity) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bb.vertex(matrixStack.last().pose(), x + width, y, gui.getBlitOffset()).color(0F, 0F, 0F, opacity).endVertex();
        bb.vertex(matrixStack.last().pose(), x, y, gui.getBlitOffset()).color(0F, 0F, 0F, opacity).endVertex();
        bb.vertex(matrixStack.last().pose(), x, y + height, gui.getBlitOffset()).color(0F, 0F, 0F, opacity).endVertex();
        bb.vertex(matrixStack.last().pose(), x + width, y + height, gui.getBlitOffset()).color(0F, 0F, 0F, opacity).endVertex();
        tesselator.end();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void updateCurrentList() {
        if (Minecraft.getInstance() != null && Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;
            IPowerHandler handler = PowerManager.getPowerHandler(player);

            for (Map.Entry<ResourceLocation, IPowerHolder> entry : handler.getPowerHolders().entrySet()) {
                AbilityList list = new AbilityList(entry.getKey(), entry.getValue().getPower());

                for (AbilityEntry abilityEntry : entry.getValue().getAbilities().values()) {
                    list.addAbility(abilityEntry);
                }

                ABILITY_LIST = list;
                return;
            }
        }

        ABILITY_LIST = null;
    }

    public static class AbilityList {

        private final ResourceLocation provider;
        private final Power power;
        private final AbilityEntry[] abilities = new AbilityEntry[5];
        private ResourceLocation texture;

        public AbilityList(ResourceLocation provider, Power power) {
            this.provider = provider;
            this.power = power;
        }

        public AbilityList addAbility(int index, AbilityEntry ability) {
            this.abilities[index] = ability;
            return this;
        }

        public boolean addAbility(AbilityEntry ability) {
            for (int i = 0; i < this.abilities.length; i++) {
                if (this.abilities[i] == null) {
                    this.abilities[i] = ability;
                    return true;
                }
            }
            return false;
        }

        public AbilityList setTexture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public boolean isEmpty() {
            for (AbilityEntry ability : this.abilities) {
                if (ability != null) {
                    return false;
                }
            }
            return true;
        }

        public AbilityEntry[] getAbilities() {
            return abilities;
        }

        public ResourceLocation getProvider() {
            return provider;
        }
    }

    public enum Position {

        TOP_LEFT(true, true),
        TOP_RIGHT(false, true),
        BOTTOM_LEFT(true, false),
        BOTTOM_RIGHT(false, false),
        HIDDEN(false, false);

        private final boolean left, top;

        Position(boolean left, boolean top) {
            this.left = left;
            this.top = top;
        }
    }


}
