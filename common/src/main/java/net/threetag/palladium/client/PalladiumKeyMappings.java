package net.threetag.palladium.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.network.AbilityKeyPressedMessage;
import net.threetag.palladium.network.NotifyMovementKeyListenerMessage;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.event.ClientTickEvents;
import net.threetag.palladiumcore.event.InputEvents;
import net.threetag.palladiumcore.registry.client.KeyMappingRegistry;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements InputEvents.KeyPressed, ClientTickEvents.ClientTick {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static final KeyMapping SWITCH_ABILITY_LIST = new KeyMapping("key.palladium.switch_ability_list", 88, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];
    public static AbilityEntry LEFT_CLICKED_ABILITY = null;
    public static AbilityEntry RIGHT_CLICKED_ABILITY = null;

    public static void init() {
        KeyMappingRegistry.register(SWITCH_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 44 : -1, CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        InputEvents.KEY_PRESSED.register(instance);
        ClientTickEvents.CLIENT_POST.register(instance);
    }

    @Override
    public void keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        if (client.player != null && client.screen == null && !client.player.isSpectator()) {

            // Scroll ability list
            if (SWITCH_ABILITY_LIST.isDown()) {
                AbilityBarRenderer.scroll(!client.player.isCrouching());
            }

            // Ability keys
            AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
            if (list != null && action != GLFW.GLFW_REPEAT) {
                for (AbilityKeyMapping key : ABILITY_KEYS) {
                    AbilityEntry entry = list.getDisplayedAbilities()[key.index - 1];

                    if (entry != null && (action != GLFW.GLFW_PRESS || (!entry.getConfiguration().needsEmptyHand() || client.player.getMainHandItem().isEmpty()))) {
                        if (key.matches(keyCode, scanCode) && entry.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.KEY_BIND) {
                            new AbilityKeyPressedMessage(entry.getReference(), action == GLFW.GLFW_PRESS).send();
                        } else if (entry.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.SPACE_BAR && client.options.keyJump.matches(keyCode, scanCode)) {
                            new AbilityKeyPressedMessage(entry.getReference(), action == GLFW.GLFW_PRESS).send();
                            return;
                        }
                    }
                }
            }

            // Sync jump key
            if (PalladiumProperties.JUMP_KEY_DOWN.isRegistered(client.player) && client.options.keyJump.isDown() != PalladiumProperties.JUMP_KEY_DOWN.get(client.player)) {
                new NotifyMovementKeyListenerMessage(0, client.options.keyJump.isDown()).send();
            }

            if (PalladiumProperties.LEFT_KEY_DOWN.isRegistered(client.player) && client.options.keyLeft.isDown() != PalladiumProperties.LEFT_KEY_DOWN.get(client.player)) {
                new NotifyMovementKeyListenerMessage(1, client.options.keyLeft.isDown()).send();
            }

            if (PalladiumProperties.RIGHT_KEY_DOWN.isRegistered(client.player) && client.options.keyRight.isDown() != PalladiumProperties.RIGHT_KEY_DOWN.get(client.player)) {
                new NotifyMovementKeyListenerMessage(2, client.options.keyRight.isDown()).send();
            }

            if (PalladiumProperties.FORWARD_KEY_DOWN.isRegistered(client.player) && client.options.keyUp.isDown() != PalladiumProperties.FORWARD_KEY_DOWN.get(client.player)) {
                new NotifyMovementKeyListenerMessage(3, client.options.keyUp.isDown()).send();
            }

            if (PalladiumProperties.BACKWARDS_KEY_DOWN.isRegistered(client.player) && client.options.keyDown.isDown() != PalladiumProperties.BACKWARDS_KEY_DOWN.get(client.player)) {
                new NotifyMovementKeyListenerMessage(4, client.options.keyDown.isDown()).send();
            }
        }
    }

    @Override
    public void clientTick(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.screen == null && !minecraft.player.isSpectator()) {
            // Stop left-clicked ability
            if (LEFT_CLICKED_ABILITY != null && !minecraft.options.keyAttack.isDown()) {
                new AbilityKeyPressedMessage(LEFT_CLICKED_ABILITY.getReference(), false).send();
                LEFT_CLICKED_ABILITY = null;
            }

            // Stop right-clicked ability
            if (RIGHT_CLICKED_ABILITY != null && !minecraft.options.keyUse.isDown()) {
                new AbilityKeyPressedMessage(RIGHT_CLICKED_ABILITY.getReference(), false).send();
                RIGHT_CLICKED_ABILITY = null;
            }
        }
    }

    public static AbilityEntry getPrioritisedKeyedAbility(AbilityConfiguration.KeyType keyType) {
        var list = AbilityBarRenderer.getSelectedList();

        if (list != null) {
            for (AbilityEntry ability : list.getDisplayedAbilities()) {
                if (ability != null && ability.isUnlocked()) {
                    if (ability.getConfiguration().getKeyType() == keyType) {
                        return ability;
                    }
                }
            }
        }

        for (AbilityEntry entry : AbilityUtil.getEntries(Minecraft.getInstance().player)) {
            if (entry != null && entry.isUnlocked()) {
                if (entry.getConfiguration().getKeyType() == keyType) {
                    return entry;
                }
            }
        }

        return null;
    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, keyCode, category);
            this.index = index;
        }
    }

}