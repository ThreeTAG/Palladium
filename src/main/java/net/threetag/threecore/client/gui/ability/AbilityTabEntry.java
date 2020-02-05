package net.threetag.threecore.client.gui.ability;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.advancements.AdvancementState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.TexturedIcon;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AbilityTabEntry extends AbstractGui {

    public static final TexturedIcon LOCKED_ICON = new TexturedIcon(AbilitiesScreen.WIDGETS, 90, 133, 16, 16);

    protected final Ability ability;
    protected double x, y;
    private boolean unlocked;
    protected String title;
    private List<String> description;
    private final int width;
    List<AbilityTabEntry> parents = new LinkedList<>();
    List<AbilityTabEntry> children = new LinkedList<>();

    public AbilityTabEntry(Ability ability) {
        this.ability = ability;
        this.unlocked = ability.getConditionManager().isUnlocked();
        this.title = ability.getDataManager().get(Ability.TITLE).getFormattedText();
        this.description = getDescription();

        int i = getProgress() != null ? getProgress()[1] : 0;
        int j = String.valueOf(i).length();
        int k = i > 1 ? Minecraft.getInstance().fontRenderer.getStringWidth("  ") + Minecraft.getInstance().fontRenderer.getStringWidth("0") * j * 2 + Minecraft.getInstance().fontRenderer.getStringWidth("/") : 0;
        int l = 29 + Minecraft.getInstance().fontRenderer.getStringWidth(this.title) + k;

        for (String s1 : this.description) {
            l = Math.max(l, Minecraft.getInstance().fontRenderer.getStringWidth(s1));
        }

        this.width = l + 3 + 5;
    }

    public IIcon getDisplayIcon() {
        return this.unlocked ? this.ability.getDataManager().get(Ability.ICON) : LOCKED_ICON;
    }

    public AbilityTabEntry updatePosition(double x, double y, AbilityTabGui gui) {
        this.x = x;
        this.y = gui.getFreeYPos(x, y);

        for (AbilityTabEntry child : this.children) {
            child.updatePosition(this.x + 1, y, gui);
        }

        return this;
    }

    public AbilityTabEntry updateRelatives(Collection<AbilityTabEntry> list) {
        this.parents.clear();
        this.children.clear();

        for (AbilityTabEntry entry : list) {
            List<Ability> parents = AbilityHelper.findParentAbilities(Minecraft.getInstance().player, this.ability, this.ability.container);
            List<Ability> children = AbilityHelper.findChildrenAbilities(Minecraft.getInstance().player, this.ability, this.ability.container);

            if (!parents.isEmpty()) {
                if (parents.contains(entry.ability)) {
                    this.parents.add(entry);
                }
            }

            if (!children.isEmpty()) {
                if (children.contains(entry.ability)) {
                    this.children.add(entry);
                }
            }
        }

        return this;
    }

    public void drawIcon(Minecraft mc, int x, int y) {
        GlStateManager.color4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(AbilitiesScreen.WIDGETS);
        this.blit(x - 13, y - 13, 0, this.unlocked ? 128 : 154, 26, 26);
        getDisplayIcon().draw(mc, x - 8, y - 8);
    }

    public void drawHover(int x, int y, float fade, int p_191821_4_, int p_191821_5_, AbilitiesScreen gui) {
        Minecraft minecraft = Minecraft.getInstance();
        int posX = (int) (this.x * AbilityTabGui.gridSize) - 16;
        int posY = (int) (this.y * AbilityTabGui.gridSize) - 13;
        boolean flag = p_191821_4_ + x + posX + this.width + 26 >= gui.width;
        int[] progress = getProgress();
        String s = progress != null ? progress[0] + "/" + progress[1] : null;
        int i = s == null ? 0 : minecraft.fontRenderer.getStringWidth(s);
        boolean flag1 = AbilityTabGui.innerHeight - y - posY - 26 <= 6 + this.description.size() * 9;
        float f = progress == null ? (this.unlocked ? 1F : 0F) : (float) progress[0] / (float) progress[1];
        int j = MathHelper.floor(f * (float) this.width);
        AdvancementState advancementstate;
        AdvancementState advancementstate1;
        AdvancementState advancementstate2;
        if (f >= 1.0F) {
            j = this.width / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
        } else if (j < 2) {
            j = this.width / 2;
            advancementstate = AdvancementState.UNOBTAINED;
            advancementstate1 = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        } else if (j > this.width - 2) {
            j = this.width / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        } else {
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        }

        int k = this.width - j;
        minecraft.getTextureManager().bindTexture(AbilitiesScreen.WIDGETS);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        int l = y + posY;
        int i1;
        if (flag) {
            i1 = x + posX - this.width + 26 + 6;
        } else {
            i1 = x + posX;
        }

        int j1 = 32 + this.description.size() * 9;
        if (!this.description.isEmpty()) {
            if (flag1) {
                this.render9Sprite(i1, l + 26 - j1, this.width, j1, 10, 200, 26, 0, 52);
            } else {
                this.render9Sprite(i1, l, this.width, j1, 10, 200, 26, 0, 52);
            }
        }

        this.blit(i1, l, 0, advancementstate.getId() * 26, j, 26);
        this.blit(i1 + j, l, 200 - k, advancementstate1.getId() * 26, k, 26);
        this.blit(x + posX + 3, y + posY, 0, 128 + advancementstate2.getId() * 26, 26, 26);
        if (flag) {
            minecraft.fontRenderer.drawStringWithShadow(this.title, (float) (i1 + 5), (float) (y + posY + 9), -1);
            if (s != null) {
                minecraft.fontRenderer.drawStringWithShadow(s, (float) (x + posX - i), (float) (y + posY + 9), -1);
            }
        } else {
            minecraft.fontRenderer.drawStringWithShadow(this.title, (float) (x + posX + 32), (float) (y + posY + 9), -1);
            if (s != null) {
                minecraft.fontRenderer.drawStringWithShadow(s, (float) (x + posX + this.width - i - 5), (float) (y + posY + 9), -1);
            }
        }

        if (flag1) {
            for (int k1 = 0; k1 < this.description.size(); ++k1) {
                minecraft.fontRenderer.drawString(this.description.get(k1), (float) (i1 + 5), (float) (l + 26 - j1 + 7 + k1 * 9), -5592406);
            }
        } else {
            for (int l1 = 0; l1 < this.description.size(); ++l1) {
                minecraft.fontRenderer.drawString(this.description.get(l1), (float) (i1 + 5), (float) (y + posY + 9 + 17 + l1 * 9), -5592406);
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
        getDisplayIcon().draw(minecraft, x + posX + 8, y + posY + 5);
    }

    protected void render9Sprite(int p_192994_1_, int p_192994_2_, int p_192994_3_, int p_192994_4_, int p_192994_5_, int p_192994_6_, int p_192994_7_, int p_192994_8_, int p_192994_9_) {
        this.blit(p_192994_1_, p_192994_2_, p_192994_8_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.blit(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.blit(p_192994_1_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.blit(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
    }

    protected void renderRepeating(int p_192993_1_, int p_192993_2_, int p_192993_3_, int p_192993_4_, int p_192993_5_, int p_192993_6_, int p_192993_7_, int p_192993_8_) {
        for (int i = 0; i < p_192993_3_; i += p_192993_7_) {
            int j = p_192993_1_ + i;
            int k = Math.min(p_192993_7_, p_192993_3_ - i);

            for (int l = 0; l < p_192993_4_; l += p_192993_8_) {
                int i1 = p_192993_2_ + l;
                int j1 = Math.min(p_192993_8_, p_192993_4_ - l);
                this.blit(j, i1, p_192993_5_, p_192993_6_, k, j1);
            }
        }
    }

    public int[] getProgress() {
        List<Condition> conditions = this.ability.getConditionManager().getFilteredConditions(false);
        int i = conditions.size();
        if (i <= 1) {
            return null;
        } else {
            int j = this.ability.getConditionManager().getFilteredConditions(false, true).size();
            return new int[]{j, i};
        }
    }

    public List<String> getDescription() {
        List<String> list = new LinkedList<>();
        List<Condition> conditions = this.ability.getConditionManager().getFilteredConditions(false);

        for (Condition condition : conditions) {
            boolean active = this.ability.getConditionManager().isActive(condition);
            list.add(condition.getDisplayName().shallowCopy().setStyle(new Style().setColor(active ? TextFormatting.GREEN : TextFormatting.RED)).getFormattedText());
        }

        return list;
    }

    public boolean isMouseOver(int scrollX, int scrollY, int mouseX, int mouseY) {
        int i = (int) (scrollX + this.x * AbilityTabGui.gridSize) - 13;
        int j = i + 26;
        int k = (int) (scrollY + this.y * AbilityTabGui.gridSize) - 13;
        int l = k + 26;
        return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= l;
    }

    public Screen getScreen(AbilitiesScreen screen) {
        return this.ability.getScreen(screen);
    }

}
