package net.threetag.threecore.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityClientEventHandler;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.EnumAbilityColor;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityBarRenderer {

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

    public static int getKeyFromAbility(Ability ability, int pos) {
        if (ability.getDataManager().get(Ability.KEYBIND) > -1) {
            return ability.getDataManager().get(Ability.KEYBIND);
        } else if (pos < AbilityClientEventHandler.ABILITY_KEYS.size()) {
            return AbilityClientEventHandler.ABILITY_KEYS.get(pos).getKey().getKeyCode();
        }
        return -1;
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
            RenderSystem.pushMatrix();
            Tessellator tes = Tessellator.getInstance();
            BufferBuilder bb = tes.getBuffer();
            List<Ability> abilities = getCurrentDisplayedAbilities(AbilityHelper.getAbilities(mc.player));
            boolean showName = mc.currentScreen instanceof ChatScreen;

            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                EnumAbilityColor color = ability.getColor();
                ITextComponent name = showName ? ability.getDataManager().get(Ability.TITLE) : InputMappings.getInputByCode(getKeyFromAbility(ability, i), 0).func_237520_d_();
                int nameLength = mc.fontRenderer.getStringPropertyWidth(name);

                RenderSystem.color4f(1, 1, 1, 1);
                mc.textureManager.bindTexture(TEXTURE);
                mc.ingameGUI.blit(e.getMatrixStack(), 7, 7 + i * 22, color.getX(), color.getY(), 22, 22);

                if (ability.getConditionManager().isEnabled())
                    mc.ingameGUI.blit(e.getMatrixStack(), 7, 7 + i * 22, color.getX(), color.getY() + 44, 22, 22);

                ability.drawIcon(mc, e.getMatrixStack(), mc.ingameGUI, 10, 10 + i * 22);

                if (ability.getConditionManager().needsKey()) {
                    RenderSystem.disableTexture();
                    RenderSystem.enableBlend();
                    RenderSystem.color4f(0, 0, 0, 0.5F);
                    bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
                    bb.pos(7 + 22, 10 + i * 22 + 1, 0).endVertex();
                    bb.pos(7 + 22 + nameLength + 8, 10 + i * 22 + 1, 0).endVertex();
                    bb.pos(7 + 22 + nameLength + 8, 10 + i * 22 + 15, 0).endVertex();
                    bb.pos(7 + 22, 10 + i * 22 + 15, 0).endVertex();
                    tes.draw();
                    RenderSystem.enableTexture();
                    RenderSystem.disableBlend();
                    mc.ingameGUI.drawString(e.getMatrixStack(), mc.fontRenderer, name, 34, 10 + i * 22 + 4, 0xffffff);
                }
            }
            RenderSystem.color4f(1, 1, 1, 1F);
            RenderSystem.popMatrix();
        }
    }

}
