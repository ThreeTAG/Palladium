package com.threetag.threecore.abilities.client;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AbilityBarRenderer {

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL && !mc.ingameGUI.getChatGUI().getChatOpen()) {
            List<Ability> abilities = AbilityHelper.getAbilities(mc.player);

            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                mc.ingameGUI.drawString(mc.fontRenderer, ability.getDataManager().get(Ability.TITLE).getFormattedText(), 10, 10 + i * 15, 0xffffff);
            }
        }
    }

}
