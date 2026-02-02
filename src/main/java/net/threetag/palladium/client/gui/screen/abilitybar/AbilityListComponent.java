package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.AbilityKeyBind;
import net.threetag.palladium.power.ability.keybind.JumpKeyBind;
import net.threetag.palladium.power.ability.keybind.KeyBindType;
import net.threetag.palladium.power.ability.keybind.MouseClickKeyBind;

public record AbilityListComponent(AbilityBar.AbilityList abilityList) implements AbilityBarComponent {

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 112;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment) {
        gui.blit(RenderPipelines.GUI_TEXTURED, this.abilityList.getTexture(DataContext.forPower(minecraft.player, this.abilityList.getPowerHolder())), x, y, 0, 56, this.getWidth(), this.getHeight(), 256, 256);

        for (int i = 0; i < AbilityBar.AbilityList.MAX_ABILITIES; i++) {
            var ability = this.abilityList.getAbility(i);
            int abilityX = x + 3;
            int abilityY = y + 3 + (i * 22);
            renderAbility(minecraft, this.abilityList.getTexture(DataContext.forAbility(minecraft.player, ability)), gui, abilityX, abilityY, alignment, ability, i);
        }
    }

    public static void renderAbility(Minecraft minecraft, Identifier texture, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment, AbilityInstance<?> ability, int index) {
        if (ability != null) {
            if (ability.isUnlocked()) {
                if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler
                        && handler.getBehaviour() == KeyBindEnablingHandler.Behaviour.ACTIVATION) {
                    int height = (int) (handler.getActivationPercentage(ability) * 18);
                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 24, 56, 18, 18, 256, 256);
                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y + (18 - height), 42, 56 + (18 - height), 18, height, 256, 256);
                } else {
                    if (ability.isEnabled()) {
                        gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 42, 56, 18, 18, 256, 256);
                    } else {
                        gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 24, 56, 18, 18, 256, 256);
                    }
                }

                // Ability Icon
                IconRenderer.drawIcon(ability.getAbility().getProperties().getIcon(), minecraft, gui, DataContext.forAbility(minecraft.player, ability), x + 1, y + 1);

                // Cooldown
                if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler
                        && handler.displayCooldown(ability)) {
                    float percentage = handler.getCooldownPercentage(ability);

                    gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 60, 74, (int) (18 * percentage), 18, 256, 256);
                }

                // Key Bind (inside)
                if (PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY.get() == AbilityKeyBindDisplay.INSIDE &&
                        ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                    var key = getComponentForKeyBind(handler.getKeyBindType(), ability, texture, true, index);
                    gui.pose().pushMatrix();
                    gui.pose().translate(0, 0);
                    key.render(minecraft, gui, x + 19 - key.getWidth(), y + 17 - key.getHeight(), alignment);
                    gui.pose().popMatrix();
                }

                // Name / Key Bind (outside)
                boolean chatOpen = minecraft.screen instanceof ChatScreen;
                AbilityBarComponent displayedText = null;

                if (PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY.get() == AbilityKeyBindDisplay.OUTSIDE) {
                    if (chatOpen) {
                        displayedText = new TextAbilityBarComponent(ability.getAbility().getDisplayName());
                    } else if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                        displayedText = getComponentForKeyBind(handler.getKeyBindType(), ability, texture, false, index);
                    }
                } else if (chatOpen) {
                    displayedText = new TextAbilityBarComponent(ability.getAbility().getDisplayName());
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
                    displayedText.render(minecraft, gui, x + (alignment.isLeft() ? 26 : -width - 8), y + 3 + ((12 - displayedText.getHeight()) / 2), alignment);
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

    public static AbilityBarComponent getComponentForKeyBind(KeyBindType type, AbilityInstance<?> abilityInstance, Identifier texture, boolean inside, int index) {
        return switch (type) {
            case JumpKeyBind jump -> new BlitAbilityBarComponent(texture, 39, 92, 10, 5, 256, 256);
            case AbilityKeyBind ability ->
                    new TextAbilityBarComponent(PalladiumKeyMappings.ABILITY_KEYS[index].getTranslatedKeyMessage(), inside);
            case MouseClickKeyBind mouse -> new BlitAbilityBarComponent(texture,
                    mouse.clickType == MouseClickKeyBind.ClickType.LEFT_CLICK ? 24 :
                            (mouse.clickType == MouseClickKeyBind.ClickType.RIGHT_CLICK ? 29 : 34),
                    92, 5, 7, 256, 256);
            default ->
                    throw new IllegalStateException(String.format("No UiComponent known for key bind type %s", type.getClass().getSimpleName()));
        };
    }
}
