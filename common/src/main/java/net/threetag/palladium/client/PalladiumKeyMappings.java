package net.threetag.palladium.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.AbilityKeyBind;
import net.threetag.palladium.power.ability.keybind.MouseClickKeyBind;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements ClientRawInputEvent.KeyPressed, ClientTickEvent.Client {

    public static final String CATEGORY = "key.palladium.categories.powers";
    public static final KeyMapping OPEN_EQUIPMENT = new KeyMapping("key.palladium.open_equipment", GLFW.GLFW_KEY_BACKSLASH, "key.categories.gameplay");
    public static final KeyMapping SHOW_POWERS = new KeyMapping("key.palladium.show_powers", InputConstants.UNKNOWN.getValue(), CATEGORY);
    public static final KeyMapping ROTATE_ABILITY_LIST = new KeyMapping("key.palladium.rotate_ability_list", GLFW.GLFW_KEY_X, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    private static boolean LEFT_CLICK_DOWN = false;
    private static boolean RIGHT_CLICK_DOWN = false;
    private static boolean MIDDLE_CLICK_DOWN = false;

    public static void init() {
        KeyMappingRegistry.register(OPEN_EQUIPMENT);
        KeyMappingRegistry.register(SHOW_POWERS);
        KeyMappingRegistry.register(ROTATE_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, getKeyForIndex(i), CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        ClientRawInputEvent.KEY_PRESSED.register(instance);
//        InputEvents.MOUSE_SCROLLING.register(instance);
        ClientTickEvent.CLIENT_POST.register(instance);
    }

    private static int getKeyForIndex(int index) {
        return switch (index) {
            case 1 -> GLFW.GLFW_KEY_V;
            case 2 -> GLFW.GLFW_KEY_B;
            case 3 -> GLFW.GLFW_KEY_N;
            case 4 -> GLFW.GLFW_KEY_M;
            case 5 -> GLFW.GLFW_KEY_K;
            default -> -1;
        };
    }

    @Override
    public EventResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        var abilityBar = AbilityBar.INSTANCE.getCurrentList();

        if (client.player != null) {
            if (abilityBar != null) {
                for (AbilityKeyMapping key : ABILITY_KEYS) {
                    if (key.matches(keyCode, 0)) {
                        AbilityInstance<?> ability = abilityBar.getAbility(key.index - 1);

                        if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            if (handler.getKeyBindType() instanceof AbilityKeyBind) {
                                if (action == GLFW.GLFW_PRESS) {
                                    if (client.screen == null) {
                                        handler.onKeyPressed(client.player, ability);
                                    }
                                } else if (action == GLFW.GLFW_RELEASE) {
                                    handler.onKeyReleased(client.player, ability);
                                }
                            }
                        }
                    }
                }
            }
        }

        return EventResult.pass();
    }

    @Override
    public void tick(Minecraft client) {
        if (client.player != null) {
            if (client.screen == null) {
                var abilityBar = AbilityBar.INSTANCE.getCurrentList();

                if (abilityBar != null) {
                    while (ROTATE_ABILITY_LIST.consumeClick()) {
                        AbilityBar.INSTANCE.rotateList(!client.player.isCrouching());
                    }
                }

                while (SHOW_POWERS.consumeClick()) {
                    client.setScreen(new PowersScreen());
                }
            }

            var leftKeyDown = client.options.keyAttack.isDown();
            var rightKeyDown = client.options.keyUse.isDown();
            var middleClickDown = client.options.keyPickItem.isDown();

            if (leftKeyDown != LEFT_CLICK_DOWN) {
                if (!leftKeyDown) {
                    for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(client.player)) {
                        if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                                if (mouseClick.clickType == MouseClickKeyBind.ClickType.LEFT_CLICK) {
                                    handler.onKeyReleased(client.player, ability);
                                }
                            }
                        }
                    }
                }

                LEFT_CLICK_DOWN = leftKeyDown;
            }

            if (rightKeyDown != RIGHT_CLICK_DOWN) {
                if (!rightKeyDown) {
                    for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(client.player)) {
                        if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                                if (mouseClick.clickType == MouseClickKeyBind.ClickType.RIGHT_CLICK) {
                                    handler.onKeyReleased(client.player, ability);
                                }
                            }
                        }
                    }
                }

                RIGHT_CLICK_DOWN = rightKeyDown;
            }

            if (middleClickDown != MIDDLE_CLICK_DOWN) {
                if (!middleClickDown) {
                    for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(client.player)) {
                        if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                                if (mouseClick.clickType == MouseClickKeyBind.ClickType.MIDDLE_CLICK) {
                                    handler.onKeyReleased(client.player, ability);
                                }
                            }
                        }
                    }
                }

                MIDDLE_CLICK_DOWN = middleClickDown;
            }
        }
    }


    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, InputConstants.Type.KEYSYM, keyCode, category);
            this.index = index;
        }
    }

}