package net.threetag.palladium.client;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.network.messages.AbilityKeyPressedMessage;
import net.threetag.palladium.network.messages.NotifyJumpKeyListenerMessage;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperties;
import org.lwjgl.glfw.GLFW;

public class PalladiumKeyMappings {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static final KeyMapping SWITCH_ABILITY_LIST = new KeyMapping("key.palladium.switch_ability_list", 88, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    public static void init() {
        KeyMappingRegistry.register(SWITCH_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 44 : -1, CATEGORY, i));
        }

        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            if(client.player != null && client.screen == null) {
                if (SWITCH_ABILITY_LIST.isDown()) {
                    AbilityBarRenderer.scroll(true);
                }

                AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
                if (list != null && action != GLFW.GLFW_REPEAT) {
                    for (AbilityKeyMapping key : ABILITY_KEYS) {
                        AbilityEntry entry = list.getAbilities()[key.index - 1];

                        if (key.matches(keyCode, scanCode) && entry != null) {
                            new AbilityKeyPressedMessage(list.getPower().getId(), entry.id, action == GLFW.GLFW_PRESS).sendToServer();
                        }
                    }
                }

                if (PalladiumProperties.JUMP_KEY_DOWN.isRegistered(client.player) && client.options.keyJump.isDown() != PalladiumProperties.JUMP_KEY_DOWN.get(client.player)) {
                    new NotifyJumpKeyListenerMessage(client.options.keyJump.isDown()).sendToServer();
                }
            }

            return EventResult.pass();
        });
    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, keyCode, category);
            this.index = index;
        }
    }

}
