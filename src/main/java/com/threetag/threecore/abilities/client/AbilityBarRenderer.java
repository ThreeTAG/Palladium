package com.threetag.threecore.abilities.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityBarRenderer {

    // TODO new ability key binding

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/ability_bar.png");
    public static int INDEX = 0;
    public static final int ENTRY_SHOW_AMOUNT = 5;

    public static List<Ability> getCurrentDisplayedAbilities(List<Ability> abilities) {
        abilities = abilities.stream().filter(a -> a.getConditionManager().isUnlocked() && (a.getConditionManager().needsKey() || a.getDataManager().get(Ability.SHOW_IN_BAR)) && !a.getDataManager().get(Ability.HIDDEN)).collect(Collectors.toList());
        List<Ability> list = new ArrayList<>();

        if (abilities.isEmpty())
            return list;

        if (INDEX >= abilities.size())
            INDEX = 0;
        else if (INDEX < 0)
            INDEX = abilities.size() - 1;

        list.add(abilities.get(INDEX));

        int i = INDEX + 1;
        int added = 1;
        while (list.size() < ENTRY_SHOW_AMOUNT && added < abilities.size()) {
            if (i >= abilities.size())
                i = 0;
            list.add(abilities.get(i));
            i++;
            added++;
        }

        return list;
    }

    public static Ability getAbilityFromKey(int key) {
        if (key > ENTRY_SHOW_AMOUNT)
            return null;

        List<Ability> list = getCurrentDisplayedAbilities(AbilityHelper.getAbilities(Minecraft.getInstance().player));

        if (key < 0 || key >= list.size())
            return null;

        return list.get(key);
    }

    public static void scroll(boolean up) {
        if (up)
            INDEX++;
        else
            INDEX--;
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Tessellator tes = Tessellator.getInstance();
            BufferBuilder bb = tes.getBuffer();
            List<Ability> abilities = getCurrentDisplayedAbilities(AbilityHelper.getAbilities(mc.player));
            boolean showName = mc.ingameGUI.getChatGUI().getChatOpen();

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                EnumAbilityColor color = ability.getColor();
                //color = EnumAbilityColor.values()[new Random(ability.getId().hashCode() + 21321357).nextInt(EnumAbilityColor.values().length)];
                String name = showName ? ability.getDataManager().get(Ability.TITLE).getFormattedText() : AbilityKeyHandler.KEYS.get(i).getLocalizedName();
                int nameLength = mc.fontRenderer.getStringWidth(name);

                GlStateManager.color4f(1, 1, 1, 1);
                mc.textureManager.bindTexture(TEXTURE);
                mc.ingameGUI.blit(7, 7 + i * 22, color.getX(), color.getY(), 22, 22);

                if (ability.getConditionManager().isEnabled())
                    mc.ingameGUI.blit(7, 7 + i * 22, color.getX(), color.getY() + 44, 22, 22);

                ability.drawIcon(mc, mc.ingameGUI, 10, 10 + i * 22);

                if (ability.getConditionManager().needsKey()) {
                    GlStateManager.disableTexture();
                    GlStateManager.disableCull();
                    GlStateManager.color4f(0, 0, 0, 0.5F);
                    bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
                    bb.pos(7 + 22, 10 + i * 22 + 1, 0).endVertex();
                    bb.pos(7 + 22 + nameLength + 8, 10 + i * 22 + 1, 0).endVertex();
                    bb.pos(7 + 22 + nameLength + 8, 10 + i * 22 + 15, 0).endVertex();
                    bb.pos(7 + 22, 10 + i * 22 + 15, 0).endVertex();
                    tes.draw();
                    GlStateManager.enableTexture();
                    mc.ingameGUI.drawString(mc.fontRenderer, name, 34, 10 + i * 22 + 4, 0xffffff);
                }
            }
            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();
        }
    }

}
