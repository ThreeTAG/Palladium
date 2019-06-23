package com.threetag.threecore.abilities.client;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.network.AbilityKeyMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class AbilityKeyHandler {

    public static final String CATEGORY = "ThreeCore";

    public static ArrayList<KeyBindingAbility> KEYS = new ArrayList<KeyBindingAbility>();
    public static final KeyBindingAbility ABILITY_1 = new KeyBindingAbility("key.threecore.ability_1", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 88, CATEGORY, 1);
    public static final KeyBindingAbility ABILITY_2 = new KeyBindingAbility("key.threecore.ability_2", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 67, CATEGORY, 2);
    public static final KeyBindingAbility ABILITY_3 = new KeyBindingAbility("key.threecore.ability_3", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 86, CATEGORY, 3);
    public static final KeyBindingAbility ABILITY_4 = new KeyBindingAbility("key.threecore.ability_4", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 66, CATEGORY, 4);
    public static final KeyBindingAbility ABILITY_5 = new KeyBindingAbility("key.threecore.ability_5", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 78, CATEGORY, 5);

    public static final KeyBinding SCROLL_UP = new KeyBinding("key.threecore.scroll_up", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 73, CATEGORY);
    public static final KeyBinding SCROLL_DOWN = new KeyBinding("key.threecore.scroll_down", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 80, CATEGORY);

    public AbilityKeyHandler() {
        KEYS.add(ABILITY_1);
        KEYS.add(ABILITY_2);
        KEYS.add(ABILITY_3);
        KEYS.add(ABILITY_4);
        KEYS.add(ABILITY_5);

        KEYS.forEach(k -> ClientRegistry.registerKeyBinding(k));
        ClientRegistry.registerKeyBinding(SCROLL_UP);
        ClientRegistry.registerKeyBinding(SCROLL_DOWN);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        boolean pressed = false;

        for (KeyBindingAbility key : KEYS) {
            if (key.isKeyDown() != key.isPressed) {
                key.isPressed = !key.isPressed;
                Ability ability = AbilityBarRenderer.getAbilityFromKey(key.abilityIndex - 1);

                if (ability != null) {
                    if (key.isPressed)
                        pressed = true;
                    ThreeCore.NETWORK_CHANNEL.sendToServer(new AbilityKeyMessage(ability.getContainer().getId(), ability.getId(), key.isPressed));
                }
            }
        }

        if (!pressed) {
            if (SCROLL_UP.isKeyDown()) {
                AbilityBarRenderer.scroll(true);
            } else if (SCROLL_DOWN.isKeyDown()) {
                AbilityBarRenderer.scroll(false);
            }
        }
    }

    @SubscribeEvent
    public void onMouse(GuiScreenEvent.MouseScrollEvent.Pre e) {
        if (Minecraft.getInstance().player.isSneaking() && e.getScrollDelta() != 0F) {
            AbilityBarRenderer.scroll(e.getScrollDelta() > 0);
            e.setCanceled(true);
        }
    }

    public static class KeyBindingAbility extends KeyBinding {

        public final int abilityIndex;
        public boolean isPressed;

        public KeyBindingAbility(String description, IKeyConflictContext keyConflictContext, InputMappings.Type inputType, int keyCode, String category, int abilityIndex) {
            super(description, keyConflictContext, inputType, keyCode, category);
            this.abilityIndex = abilityIndex;
        }
    }

}
