package net.threetag.palladium.power.ability.keybind;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class KeybindEventHandler {

    @SubscribeEvent
    static void onKeyInput(InputEvent.InteractionKeyMappingTriggered e) {
        var minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {
            if (e.getKeyMapping() == minecraft.options.keyAttack) {
                handleInput(e, minecraft, MouseClickKeyBind.ClickType.LEFT_CLICK);
            } else if (e.getKeyMapping() == minecraft.options.keyUse) {
                handleInput(e, minecraft, MouseClickKeyBind.ClickType.RIGHT_CLICK);
            } else if (e.getKeyMapping() == minecraft.options.keyPickItem) {
                handleInput(e, minecraft, MouseClickKeyBind.ClickType.MIDDLE_CLICK);
            }
        }
    }

    private static void handleInput(InputEvent.InteractionKeyMappingTriggered e, Minecraft minecraft, MouseClickKeyBind.ClickType clickType) {
        for (AbilityInstance<Ability> ability : AbilityUtil.getInstances(minecraft.player)) {
            if (ability != null && ability.isUnlocked() && ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                if (handler.getKeyBindType() instanceof MouseClickKeyBind mouseClick) {
                    if (minecraft.screen == null && mouseClick.clickType == clickType) {
                        handler.onKeyPressed(minecraft.player, ability);

                        if (mouseClick.cancelInteraction) {
                            e.setSwingHand(false);
                            e.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

}
