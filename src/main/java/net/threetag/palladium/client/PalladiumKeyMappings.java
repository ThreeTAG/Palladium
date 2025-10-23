package net.threetag.palladium.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.AbilityKeyBind;
import net.threetag.palladium.power.ability.keybind.MouseClickKeyBind;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumKeyMappings {

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Palladium.id("powers"));
    public static final KeyMapping OPEN_EQUIPMENT = new KeyMapping("key.palladium.open_equipment", GLFW.GLFW_KEY_BACKSLASH, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping SHOW_POWERS = new KeyMapping("key.palladium.show_powers", InputConstants.UNKNOWN.getValue(), CATEGORY);
    public static final KeyMapping ROTATE_ABILITY_LIST = new KeyMapping("key.palladium.rotate_ability_list", GLFW.GLFW_KEY_Y, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    private static boolean LEFT_CLICK_DOWN = false;
    private static boolean RIGHT_CLICK_DOWN = false;
    private static boolean MIDDLE_CLICK_DOWN = false;

    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent e) {
        e.registerCategory(CATEGORY);
        e.register(OPEN_EQUIPMENT);
        e.register(SHOW_POWERS);
        e.register(ROTATE_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            e.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, getKeyForIndex(i), CATEGORY, i));
        }
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

    @SubscribeEvent
    static void clientInput(InputEvent.Key e) {
        var abilityBar = AbilityBar.INSTANCE.getCurrentList();
        var client = Minecraft.getInstance();

        if (client.player != null) {
            if (abilityBar != null) {
                for (AbilityKeyMapping key : ABILITY_KEYS) {
                    if (key.matches(e.getKeyEvent())) {
                        AbilityInstance<?> ability = abilityBar.getAbility(key.index - 1);

                        if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            if (handler.getKeyBindType() instanceof AbilityKeyBind) {
                                if (e.getAction() == GLFW.GLFW_PRESS) {
                                    if (client.screen == null) {
                                        handler.onKeyPressed(client.player, ability);
                                    }
                                } else if (e.getAction() == GLFW.GLFW_RELEASE) {
                                    handler.onKeyReleased(client.player, ability);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post e) {
        var client = Minecraft.getInstance();

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

        public AbilityKeyMapping(String description, int keyCode, KeyMapping.Category category, int index) {
            super(description, InputConstants.Type.KEYSYM, keyCode, category);
            this.index = index;
        }
    }

}