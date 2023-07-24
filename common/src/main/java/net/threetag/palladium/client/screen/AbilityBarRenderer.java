package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.netty.util.collection.IntObjectHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityColor;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladiumcore.event.ClientTickEvents;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

import java.util.ArrayList;
import java.util.List;

public class AbilityBarRenderer implements OverlayRegistry.IIngameOverlay {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Palladium.MOD_ID, "textures/gui/ability_bar.png");
    public static List<AbilityList> ABILITY_LISTS = new ArrayList<>();
    public static int SELECTED = 0;

    public AbilityBarRenderer() {
        ClientTickEvents.CLIENT_POST.register(instance -> updateCurrentLists());
    }

    public static AbilityList getSelectedList() {
        if (ABILITY_LISTS.isEmpty()) {
            return null;
        } else {
            if (SELECTED >= ABILITY_LISTS.size() || SELECTED < 0) {
                SELECTED = 0;
            }
            return ABILITY_LISTS.get(SELECTED);
        }
    }

    @Override
    public void render(Minecraft mc, Gui gui, PoseStack poseStack, float partialTicks, int width, int height) {
        if (ABILITY_LISTS.isEmpty()) {
            return;
        }

        Position position = PalladiumConfig.Client.ABILITY_BAR_POSITION.get();
        AbilityList list = getSelectedList();

        if (position == Position.HIDDEN || list == null) {
            return;
        }

        if (position.top && mc.options.renderDebug) {
            return;
        }

        if (!position.top && mc.screen instanceof ChatScreen) {
            position = position.left ? Position.TOP_LEFT : Position.TOP_RIGHT;
        }

        boolean simple = list.simple && ABILITY_LISTS.size() <= 1;
        if (mc.player != null) {
            var texture = list.texture != null ? list.texture : TEXTURE;
            int indicatorWidth = 52;
            int indicatorHeight = 28;

            if (!simple) {
                poseStack.pushPose();
                translateIndicatorBackground(poseStack, mc.getWindow(), position, indicatorWidth, indicatorHeight);
                renderIndicator(list, mc, poseStack, position, texture, ABILITY_LISTS.size() > 1);
                poseStack.popPose();
            }

            poseStack.pushPose();
            translateAbilitiesBackground(poseStack, mc.getWindow(), position, indicatorHeight, 24, 112, simple);
            renderAbilitiesBackground(mc, poseStack, position, list, texture, simple);
            renderAbilitiesOverlay(mc, poseStack, position, list, texture, simple);
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

    private static void renderIndicator(AbilityList list, Minecraft minecraft, PoseStack poseStack, Position position, ResourceLocation texture, boolean showKey) {
        // Background
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.gui.blit(poseStack, 0, 0, position.left ? 52 : 0, position.top ? 28 : 0, 52, 28);

        // Icon
        list.power.getIcon().draw(minecraft, poseStack, showKey ? (position.left ? 30 : 6) : (position.left ? 17 : 19), position.top ? 5 : 7);

        // Button
        if (showKey) {
            FormattedText properties = minecraft.font.substrByWidth(PalladiumKeyMappings.SWITCH_ABILITY_LIST.getTranslatedKeyMessage(), 10);
            int length = minecraft.font.width(properties) + 10;
            minecraft.font.draw(poseStack, Component.literal(properties.getString()), (position.left ? 15 : 37) - length / 2F + 10, position.top ? 10 : 12, 0xffffffff);

            RenderSystem.setShaderTexture(0, texture);
            minecraft.gui.blit(poseStack, (position.left ? 15 : 37) - length / 2, position.top ? 9 : 11, 78, minecraft.player.isCrouching() ? 64 : 56, 8, 8);
        }
    }

    private static void translateAbilitiesBackground(PoseStack poseStack, Window window, Position position, int indicatorHeight, int abilitiesWidth, int abilitiesHeight, boolean simple) {
        if (!simple) {
            if (position.top) {
                poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, indicatorHeight - 1, 0);
            } else {
                poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, window.getGuiScaledHeight() - indicatorHeight - abilitiesHeight + 1, 0);
            }
        } else {
            if (position.top) {
                poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, 0, 0);
            } else {
                poseStack.translate(!position.left ? window.getGuiScaledWidth() - abilitiesWidth : 0, window.getGuiScaledHeight() - 24, 0);
            }
        }
    }

    private static void renderAbilitiesBackground(Minecraft minecraft, PoseStack poseStack, Position position, AbilityList list, ResourceLocation texture, boolean simple) {
        boolean showName = minecraft.screen instanceof ChatScreen;

        for (int i = 0; i < 5; i++) {
            if (simple && i > 0) {
                break;
            }
            Lighting.setupFor3DItems();
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            minecraft.gui.blit(poseStack, 3, i * 22 + 3, 60, 56, 18, 18);

            if (list != null) {
                AbilityEntry entry = list.getDisplayedAbilities()[i];

                if (entry != null) {
                    if (entry.isEnabled() && entry.activationTimer != 0 && entry.maxActivationTimer != 0) {
                        int height = (int) ((float) entry.activationTimer / (float) entry.maxActivationTimer * 18);
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3, 24, 56, 18, 18);
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3 + (18 - height), 42, 74 - height, 18, height);
                    } else {
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3, entry.isEnabled() ? 42 : 24, entry.isUnlocked() ? 56 : 74, 18, 18);
                    }

                    if (entry.cooldown > 0) {
                        int width = (int) ((float) entry.cooldown / (float) entry.maxCooldown * 18);
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3, 60, 74, width, 18);
                    }

                    if (!entry.isUnlocked()) {
                        minecraft.gui.blit(poseStack, 3, i * 22 + 3, 42, 74, 18, 18);
                    } else {
                        entry.getProperty(Ability.ICON).draw(minecraft, poseStack, 4, 4 + i * 22);
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
                } else {
                    minecraft.gui.blit(poseStack, 3, i * 22 + 3, 60, 56, 18, 18);
                }
            }
        }

        // Overlay
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderAbilitiesOverlay(Minecraft minecraft, PoseStack poseStack, Position position, AbilityList list, ResourceLocation texture, boolean simple) {
        // Overlay
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (!simple) {
            minecraft.gui.blit(poseStack, 0, 0, 0, 56, 24, 112);
        } else {
            minecraft.gui.blit(poseStack, 0, 0, 0, 168, 24, 24);
        }

        // Colored Frames + Keys
        for (int i = 0; i < AbilityList.SIZE; i++) {
            AbilityEntry ability = list.getDisplayedAbilities()[i];

            if (ability != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                Lighting.setupFor3DItems();
                RenderSystem.enableBlend();

                if (!ability.isUnlocked()) {
                    minecraft.gui.blit(poseStack, 3, i * 22 + 3, 42, 74, 18, 18);
                }

                AbilityColor color = ability.getProperty(Ability.COLOR);
                minecraft.gui.blit(poseStack, 0, i * 22, color.getX(), color.getY(), 24, 24);

                if (ability.getConfiguration().needsKey() && ability.isUnlocked()) {
                    AbilityConfiguration.KeyType keyType = ability.getConfiguration().getKeyType();
                    poseStack.pushPose();
                    poseStack.translate(0, 0, minecraft.getItemRenderer().blitOffset + 200);
                    if (keyType == AbilityConfiguration.KeyType.KEY_BIND) {
                        Component key = PalladiumKeyMappings.ABILITY_KEYS[i].getTranslatedKeyMessage();
                        GuiComponent.drawString(poseStack, minecraft.font, key, 5 + 19 - 2 - minecraft.font.width(key), 5 + i * 22 + 7, 0xffffff);
                    } else if (keyType == AbilityConfiguration.KeyType.LEFT_CLICK) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 8, 5 + i * 22 + 8, 24, 92, 5, 7);
                    } else if (keyType == AbilityConfiguration.KeyType.RIGHT_CLICK) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 8, 5 + i * 22 + 8, 29, 92, 5, 7);
                    } else if (keyType == AbilityConfiguration.KeyType.SPACE_BAR) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 13, 5 + i * 22 + 10, 34, 92, 10, 5);
                    } else if (keyType == AbilityConfiguration.KeyType.SCROLL_UP) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 8, 5 + i * 22 + 4, 24, 99, 5, 11);
                    } else if (keyType == AbilityConfiguration.KeyType.SCROLL_DOWN) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 8, 5 + i * 22 + 4, 29, 99, 5, 11);
                    } else if (keyType == AbilityConfiguration.KeyType.SCROLL_EITHER) {
                        minecraft.gui.blit(poseStack, 5 + 19 - 8, 5 + i * 22 + 2, 34, 99, 5, 13);
                    }

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

    public static void updateCurrentLists() {
        if (Minecraft.getInstance() != null && Minecraft.getInstance().player != null) {
            ABILITY_LISTS = getAbilityLists();

            if (SELECTED >= ABILITY_LISTS.size()) {
                SELECTED = ABILITY_LISTS.size() - 1;
            }
        }
    }

    public static void scroll(boolean up) {
        if (up) SELECTED++;
        else SELECTED--;

        if (SELECTED >= ABILITY_LISTS.size()) {
            SELECTED = 0;
        } else if (SELECTED < 0) {
            SELECTED = ABILITY_LISTS.size() - 1;
        }
    }

    public static List<AbilityList> getAbilityLists() {
        List<AbilityList> lists = new ArrayList<>();
        IPowerHandler handler = PowerManager.getPowerHandler(Minecraft.getInstance().player).orElse(null);

        if (handler == null) {
            return lists;
        }

        for (IPowerHolder holder : handler.getPowerHolders().values()) {
            List<AbilityList> containerList = new ArrayList<>();
            List<AbilityList> remainingLists = new ArrayList<>();
            List<AbilityEntry> remaining = new ArrayList<>();
            for (AbilityEntry abilityEntry : holder.getAbilities().values()) {
                int i = abilityEntry.getProperty(Ability.LIST_INDEX);

                if (abilityEntry.getConfiguration().needsKey() && !abilityEntry.getProperty(Ability.HIDDEN_IN_BAR)) {
                    if (i >= 0) {
                        int listIndex = Math.floorDiv(i, 5);
                        int index = i % 5;

                        while (!(containerList.size() - 1 >= listIndex)) {
                            containerList.add(new AbilityList(holder.getPower()));
                        }

                        AbilityList abilityList = containerList.get(listIndex);
                        abilityList.addAbility(index, abilityEntry);
                    } else {
                        remaining.add(abilityEntry);
                    }
                }
            }

            for (int i = 0; i < remaining.size(); i++) {
                AbilityEntry abilityEntry = remaining.get(i);
                int listIndex = Math.floorDiv(i, 5);
                int index = i % 5;

                while (!(remainingLists.size() - 1 >= listIndex)) {
                    remainingLists.add(new AbilityList(holder.getPower()));
                }

                AbilityList abilityList = remainingLists.get(listIndex);
                abilityList.addAbility(index, abilityEntry);
            }

            for (AbilityList list : containerList) {
                if (!list.isEmpty() && !list.isFullyLocked()) {
                    lists.add(list);
                }
            }

            for (AbilityList list : remainingLists) {
                if (!list.isEmpty() && !list.isFullyLocked()) {
                    lists.add(list);
                }
            }
        }

        if (lists.size() <= 1) {
            lists.forEach(AbilityList::simplify);
        }

        return lists;
    }

    public static class AbilityList {

        private static final int SIZE = 5;
        private final Power power;
        private final IntObjectHashMap<List<AbilityEntry>> abilities = new IntObjectHashMap<>();
        private final ResourceLocation texture;
        public boolean simple = false;

        public AbilityList(Power power) {
            this.power = power;
            this.texture = power.getAbilityBarTexture().getTexture(Minecraft.getInstance().player);
        }

        public Power getPower() {
            return power;
        }

        public AbilityList addAbility(int index, AbilityEntry ability) {
            this.abilities.computeIfAbsent(index, integer -> new ArrayList<>()).add(ability);
            return this;
        }

        public boolean addAbility(AbilityEntry ability) {
            for (int i = 0; i < SIZE; i++) {
                if (this.abilities.get(i) == null || this.abilities.get(i).isEmpty()) {
                    this.abilities.computeIfAbsent(i, integer -> new ArrayList<>()).add(ability);
                    return true;
                }
            }
            return false;
        }

        public boolean isEmpty() {
            for (int i = 0; i < SIZE; i++) {
                if (this.abilities.get(i) != null && !this.abilities.get(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public boolean isFullyLocked() {
            for (AbilityEntry entry : this.getDisplayedAbilities()) {
                if (entry != null && entry.isUnlocked()) {
                    return false;
                }
            }

            return true;
        }

        public AbilityEntry[] getDisplayedAbilities() {
            AbilityEntry[] entries = new AbilityEntry[SIZE];

            for (int i = 0; i < SIZE; i++) {
                if (this.abilities.get(i) != null) {
                    for (AbilityEntry entry : this.abilities.get(i)) {
                        var current = entries[i];

                        if (current == null) {
                            entries[i] = entry;
                        } else if (!current.isUnlocked() && entry.isUnlocked()) {
                            entries[i] = entry;
                        }
                    }
                }
            }

            return entries;
        }

        public void simplify() {
            int abilities = 0;
            AbilityEntry entry = null;

            for (AbilityEntry ability : this.getDisplayedAbilities()) {
                if (ability != null && ability.isUnlocked()) {
                    abilities++;
                    entry = ability;
                }
            }

            this.simple = abilities == 1;

            if (this.simple) {
                this.abilities.clear();
                this.addAbility(0, entry);
            }
        }
    }

    public enum Position {

        TOP_LEFT(true, true), TOP_RIGHT(false, true), BOTTOM_LEFT(true, false), BOTTOM_RIGHT(false, false), HIDDEN(false, false);

        private final boolean left, top;

        Position(boolean left, boolean top) {
            this.left = left;
            this.top = top;
        }
    }


}
