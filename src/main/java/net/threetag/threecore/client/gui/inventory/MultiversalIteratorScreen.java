package net.threetag.threecore.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.container.MultiversalIteratorContainer;
import net.threetag.threecore.item.MultiversalExtrapolatorItem;
import net.threetag.threecore.sound.TCSounds;

import java.util.List;
import java.util.Random;

public class MultiversalIteratorScreen extends ContainerScreen<MultiversalIteratorContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/container/multiversal_iterator.png");
    private float sliderProgress;
    private boolean clickedOnSroll;
    private int recipeIndexOffset;
    private boolean hasItemsInInputSlot;

    public MultiversalIteratorScreen(MultiversalIteratorContainer containerIn, PlayerInventory playerInv, ITextComponent titleIn) {
        super(containerIn, playerInv, titleIn);
        containerIn.setInventoryUpdateListener(this::onInventoryUpdate);
        this.ySize = 198;
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();

        if (!MultiversalExtrapolatorItem.hasValidUniverse(this.container.getSlot(0).getStack()) && this.field_230706_i_ != null && this.field_230706_i_.player != null && this.field_230706_i_.player.ticksExisted % 2 == 0) {
            this.field_230706_i_.player.playSound(TCSounds.MULTIVERSE_SEARCH.get(), 0.5F, 1F);
        }
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        this.field_230712_o_.func_238407_a_(stack, this.func_231171_q_(), 8.0F, 6.0F, 4210752);
        this.field_230712_o_.func_238407_a_(stack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 94), 4210752);
        Random random = new Random(this.field_230706_i_.player.ticksExisted / 2);
        String s;
        if (MultiversalExtrapolatorItem.hasValidUniverse(this.container.getSlot(0).getStack())) {
            s = TextFormatting.GOLD + I18n.format("universe." + this.container.getSlot(0).getStack().getOrCreateTag().getString("Universe"));
        } else {
            s = I18n.format("universe.earth_search", random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10));
        }
        this.field_230712_o_.func_238421_b_(stack, s, this.xSize / 2F - this.field_230712_o_.getStringWidth(s) / 2F, 25, 0xffffff);
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        this.func_230446_a_(stack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.func_238474_b_(stack, i, j, 0, 0, this.xSize, this.ySize);
        int k = (int) (41.0F * this.sliderProgress);
        this.func_238474_b_(stack, i + 119, j + 47 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        int l = this.guiLeft + 52;
        int i1 = this.guiTop + 46;
        int j1 = this.recipeIndexOffset + 12;
        this.drawRecipesBackground(stack, mouseX, mouseY, l, i1, j1);
        this.drawRecipesItems(l, i1, j1);
    }

    private void drawRecipesBackground(MatrixStack stack, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            int j1 = this.ySize;
            if (i == this.container.getSelectedRecipe()) {
                j1 += 18;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36;
            }

            this.func_238474_b_(stack, k, i1 - 1, 0, j1, 16, 18);
        }
    }

    private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
        List<ItemStack> list = this.container.getItemList();

        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            this.field_230706_i_.getItemRenderer().renderItemAndEffectIntoGUI(list.get(i), k, i1);
        }

    }

    @Override
    public boolean func_231044_a_(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        this.clickedOnSroll = false;
        if (this.hasItemsInInputSlot) {
            int i = this.guiLeft + 52;
            int j = this.guiTop + 14;
            int k = this.recipeIndexOffset + 12;

            for (int l = this.recipeIndexOffset; l < k; ++l) {
                int i1 = l - this.recipeIndexOffset;
                double d0 = p_mouseClicked_1_ - (double) (i + i1 % 4 * 16);
                double d1 = p_mouseClicked_3_ - (double) (j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.container.enchantItem(this.field_230706_i_.player, l)) {
                    Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(TCSounds.MULTIVERSE_SEARCH.get(), 1.0F));
                    this.field_230706_i_.playerController.sendEnchantPacket((this.container).windowId, l);
                    return true;
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (p_mouseClicked_1_ >= (double) i && p_mouseClicked_1_ < (double) (i + 12) && p_mouseClicked_3_ >= (double) j && p_mouseClicked_3_ < (double) (j + 54)) {
                this.clickedOnSroll = true;
            }
        }

        return super.func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public boolean func_231045_a_(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.clickedOnSroll && this.canScroll()) {
            int i = this.guiTop + 14;
            int j = i + 54;
            this.sliderProgress = ((float) p_mouseDragged_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.func_231045_a_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        }
    }

    @Override
    public boolean func_231043_a_(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float) ((double) this.sliderProgress - p_mouseScrolled_5_ / (double) i);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
        }

        return true;
    }

    private boolean canScroll() {
        return this.hasItemsInInputSlot && this.container.getRecipeListSize() > 12;
    }

    protected int getHiddenRows() {
        return (this.container.getRecipeListSize() + 4 - 1) / 4 - 3;
    }

    private void onInventoryUpdate() {
        this.hasItemsInInputSlot = this.container.hasItemsInInputSlot();
        if (!this.hasItemsInInputSlot) {
            this.sliderProgress = 0.0F;
            this.recipeIndexOffset = 0;
        }
    }

}
