package net.threetag.palladium.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.network.AbilityKeyPressedMessage;
import net.threetag.palladium.network.NotifyMovementKeyListenerMessage;
import net.threetag.palladium.network.ToggleOpenableEquipmentMessage;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.event.ClientTickEvents;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.InputEvents;
import net.threetag.palladiumcore.registry.client.KeyMappingRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements InputEvents.KeyPressed, ClientTickEvents.ClientTick, InputEvents.MouseScrolling {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static final KeyMapping OPEN_EQUIPMENT = new KeyMapping("key.palladium.open_equipment", GLFW.GLFW_KEY_SLASH, "key.categories.gameplay");
    public static final KeyMapping SWITCH_ABILITY_LIST = new KeyMapping("key.palladium.switch_ability_list", 88, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];
    public static AbilityEntry LEFT_CLICKED_ABILITY = null;
    public static AbilityEntry RIGHT_CLICKED_ABILITY = null;

    public static void init() {
        KeyMappingRegistry.register(OPEN_EQUIPMENT);
        KeyMappingRegistry.register(SWITCH_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? GLFW.GLFW_KEY_V : i == 2 ? GLFW.GLFW_KEY_B : i == 3 ? GLFW.GLFW_KEY_N : i == 4 ? GLFW.GLFW_KEY_M : i == 5 ? GLFW.GLFW_KEY_COMMA : -1, CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        InputEvents.KEY_PRESSED.register(instance);
        InputEvents.MOUSE_SCROLLING.register(instance);
        ClientTickEvents.CLIENT_POST.register(instance);
    }

    @Override
    public void keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        if (client.player != null && client.screen == null && !client.player.isSpectator()) {

            // Open Equipment
            if(OPEN_EQUIPMENT.isDown()) {
                new ToggleOpenableEquipmentMessage().send();
                return;
            }

            // Scroll ability list
            if (SWITCH_ABILITY_LIST.isDown()) {
                AbilityBarRenderer.scroll(!client.player.isCrouching());
                return;
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
    public EventResult mouseScrolling(Minecraft client, double scrollDelta, boolean leftDown, boolean middleDown, boolean rightDown, double mouseX, double mouseY) {
        var player = Objects.requireNonNull(client.player);

        // Disable active toggle abilities
        List<AbilityEntry> activeToggles = AbilityUtil.getEntries(player).stream()
                .filter(e -> e.getConfiguration().getKeyPressType() == AbilityConfiguration.KeyPressType.TOGGLE
                        && e.getConfiguration().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll")
                        && e.isEnabled())
                .toList();

        for (AbilityEntry active : activeToggles) {
            if ((active.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.SCROLL_UP && scrollDelta < 0D)
                    || (active.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.SCROLL_DOWN && scrollDelta > 0D)
                    || (active.getConfiguration().getKeyType() == AbilityConfiguration.KeyType.SCROLL_EITHER && scrollDelta != 0D)) {
                new AbilityKeyPressedMessage(active.getReference(), true).send();
                return EventResult.cancel();
            }
        }

        AbilityEntry entry = null;

        if (scrollDelta > 0D) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConfiguration.KeyType.SCROLL_UP);
        } else if (scrollDelta < 0D) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConfiguration.KeyType.SCROLL_DOWN);
        }

        if (entry == null) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConfiguration.KeyType.SCROLL_EITHER);
        }

        if (entry != null && entry.isUnlocked() && (!entry.getConfiguration().needsEmptyHand() || player.getMainHandItem().isEmpty())) {
            var pressType = entry.getConfiguration().getKeyPressType();

            if (pressType == AbilityConfiguration.KeyPressType.ACTION) {
                if (!entry.isOnCooldown()) {
                    new AbilityKeyPressedMessage(entry.getReference(), true).send();
                    return EventResult.cancel();
                }
            } else if (pressType == AbilityConfiguration.KeyPressType.ACTIVATION) {
                if (!entry.isOnCooldown() && !entry.isEnabled()) {
                    new AbilityKeyPressedMessage(entry.getReference(), true).send();
                    return EventResult.cancel();
                }
            } else if (pressType == AbilityConfiguration.KeyPressType.TOGGLE) {
                new AbilityKeyPressedMessage(entry.getReference(), true).send();
                return EventResult.cancel();
            }
        }

        return EventResult.pass();
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
                        if(!ability.getConfiguration().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll") || (!ability.getConfiguration().allowScrollWhenCrouching() || !Objects.requireNonNull(Minecraft.getInstance().player).isCrouching())) {
                            return ability;
                        }
                    }
                }
            }
        }

        for (AbilityEntry entry : AbilityUtil.getEntries(Minecraft.getInstance().player)) {
            if (entry != null && entry.isUnlocked()) {
                if (entry.getConfiguration().getKeyType() == keyType) {
                    if(!entry.getConfiguration().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll") || (!entry.getConfiguration().allowScrollWhenCrouching() || !Objects.requireNonNull(Minecraft.getInstance().player).isCrouching())) {
                        return entry;
                    }
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