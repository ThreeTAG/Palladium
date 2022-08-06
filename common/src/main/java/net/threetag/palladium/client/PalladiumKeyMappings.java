package net.threetag.palladium.client;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.network.AbilityKeyPressedMessage;
import net.threetag.palladium.network.NotifyJumpKeyListenerMessage;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperties;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements ClientRawInputEvent.KeyPressed, ClientRawInputEvent.MouseClicked {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static final KeyMapping SWITCH_ABILITY_LIST = new KeyMapping("key.palladium.switch_ability_list", 88, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    public static void init() {
        KeyMappingRegistry.register(SWITCH_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 44 : -1, CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        ClientRawInputEvent.KEY_PRESSED.register(instance);
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register(instance);
    }

    @Override
    public EventResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        if (client.player != null && client.screen == null) {
            if (SWITCH_ABILITY_LIST.isDown()) {
                AbilityBarRenderer.scroll(true);
            }

            AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
            if (list != null && action != GLFW.GLFW_REPEAT) {
                for (AbilityKeyMapping key : ABILITY_KEYS) {
                    AbilityEntry entry = list.getAbilities()[key.index - 1];

                    if (entry != null) {
                        if (key.matches(keyCode, scanCode) && entry.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.KEY_BIND) {
                            new AbilityKeyPressedMessage(list.getPower().getId(), entry.id, action == GLFW.GLFW_PRESS).sendToServer();
                        } else if (entry.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.SPACE_BAR && client.options.keyJump.matches(keyCode, scanCode)) {
                            new AbilityKeyPressedMessage(list.getPower().getId(), entry.id, action == GLFW.GLFW_PRESS).sendToServer();
                            return EventResult.interruptFalse();
                        }
                    }
                }
            }

            if (PalladiumProperties.JUMP_KEY_DOWN.isRegistered(client.player) && client.options.keyJump.isDown() != PalladiumProperties.JUMP_KEY_DOWN.get(client.player)) {
                new NotifyJumpKeyListenerMessage(client.options.keyJump.isDown()).sendToServer();
            }
        }

        return EventResult.pass();
    }

    @Override
    public EventResult mouseClicked(Minecraft client, int button, int action, int mods) {
        AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
        if (client.screen == null && client.player != null && client.hitResult != null && (action != GLFW.GLFW_PRESS || client.hitResult.getType() == HitResult.Type.MISS) && list != null) {

            if (button == 0 && !client.player.getMainHandItem().isEmpty()) {
                return EventResult.pass();
            } else if(button == 1 && (!client.player.getMainHandItem().isEmpty() || !client.player.getOffhandItem().isEmpty())) {
                return EventResult.pass();
            }

            for (AbilityEntry ability : list.getAbilities()) {
                if (ability != null && ability.isUnlocked()) {
                    AbilityConfiguration.KeyType keyType = ability.getConfiguration().getKeyType();
                    if ((keyType == AbilityConfiguration.KeyType.LEFT_CLICK && button == 0) || (keyType == AbilityConfiguration.KeyType.RIGHT_CLICK && button == 1)) {
                        new AbilityKeyPressedMessage(list.getPower().getId(), ability.id, action == GLFW.GLFW_PRESS).sendToServer();
                    }
                }
            }
        }

        return EventResult.pass();
    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, keyCode, category);
            this.index = index;
        }
    }

}