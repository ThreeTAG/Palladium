package com.threetag.threecore.abilities.client;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AbilityBarRenderer {

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL && !mc.ingameGUI.getChatGUI().getChatOpen()) {
            List<Ability> abilities = AbilityHelper.getAbilities(mc.player);

            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                ability.getDataManager().get(Ability.ICON).draw(mc, 10, 10 + i * 20);
                mc.ingameGUI.drawString(mc.fontRenderer, ability.getDataManager().get(Ability.TITLE).getFormattedText(), 30, 10 + i * 20 + 7, 0xffffff);
            }
            RenderHelper.disableStandardItemLighting();
        }
    }

}
