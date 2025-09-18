package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.gui.component.TextUiComponent;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.component.UiComponent;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;

public class AbilityListComponent implements UiComponent {

    private final AbilityBar.AbilityList abilityList;

    public AbilityListComponent(AbilityBar.AbilityList abilityList) {
        this.abilityList = abilityList;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 112;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        gui.blit(RenderPipelines.GUI_TEXTURED, this.abilityList.getTexture(DataContext.forPower(minecraft.player, this.abilityList.getPowerHolder())), x, y, 0, 56, this.getWidth(), this.getHeight(), 256, 256);

        for (int i = 0; i < AbilityBar.AbilityList.MAX_ABILITIES; i++) {
            var ability = this.abilityList.getAbility(i);
            int abilityX = x + 3;
            int abilityY = y + 3 + (i * 22);
            renderAbility(minecraft, this.abilityList.getTexture(DataContext.forAbility(minecraft.player, ability)), gui, deltaTracker, abilityX, abilityY, alignment, ability, i);
        }
    }

    public static void renderAbility(Minecraft minecraft, ResourceLocation texture, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment, AbilityInstance<?> ability, int index) {
        if (ability != null) {
            if (ability.isUnlocked()) {
                if (ability.isEnabled()) {
                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 42, 56, 18, 18, 256, 256);
                } else {
                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 24, 56, 18, 18, 256, 256);
                }

                // Ability Icon
                ability.getAbility().getProperties().getIcon().draw(minecraft, gui, DataContext.forAbility(minecraft.player, ability), x + 1, y + 1);

                // Cooldown
                if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler
                        && handler.displayCooldown(ability)) {
                    float percentage = handler.getCooldownPercentage(ability);

                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 60, 74, (int) (18 * percentage), 18, 256, 256);
                }

                // Key Bind (inside)
                if (PalladiumConfig.ABILITY_BAR_KEY_BIND_DISPLAY == AbilityKeyBindDisplay.INSIDE &&
                        ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                    var key = handler.getKeyBindType().getDisplayedKey(ability, texture, true, index);
                    gui.pose().pushMatrix();
                    gui.pose().translate(0, 0);
                    key.render(minecraft, gui, deltaTracker, x + 19 - key.getWidth(), y + 17 - key.getHeight(), alignment);
                    gui.pose().popMatrix();
                }

                // Name / Key Bind (outside)
                boolean chatOpen = minecraft.screen instanceof ChatScreen;
                UiComponent displayedText = null;

                if (PalladiumConfig.ABILITY_BAR_KEY_BIND_DISPLAY == AbilityKeyBindDisplay.OUTSIDE) {
                    if (chatOpen) {
                        displayedText = new TextUiComponent(ability.getAbility().getDisplayName());
                    } else if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                        displayedText = handler.getKeyBindType().getDisplayedKey(ability, texture, false, index);
                    }
                } else if (chatOpen) {
                    displayedText = new TextUiComponent(ability.getAbility().getDisplayName());
                }

                if (displayedText != null) {
                    int width = displayedText.getWidth();
                    gui.fill(
                            x + (alignment.isLeft() ? 21 : -width - 10),
                            y + 3,
                            x + (alignment.isLeft() ? 28 : -width - 3) + width,
                            y + 5 + 10,
                            0x80000000
                    );
                    displayedText.render(minecraft, gui, deltaTracker, x + (alignment.isLeft() ? 26 : -width - 8), y + 3 + ((12 - displayedText.getHeight()) / 2), alignment);
                }
            } else {
                gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 24, 74, 18, 18, 256, 256);
                gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 42, 74, 18, 18, 256, 256);
            }

            // Ability Color
            var color = ability.getAbility().getProperties().getColor();
            gui.blit(RenderPipelines.GUI_TEXTURED, texture, x - 3, y - 3, color.getU(), color.getV(), 24, 24, 256, 256);
        } else {
            gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 60, 56, 18, 18, 256, 256);
        }
    }
}
