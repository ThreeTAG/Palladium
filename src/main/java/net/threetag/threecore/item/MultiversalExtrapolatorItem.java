package net.threetag.threecore.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.sound.TCSounds;
import net.threetag.threecore.util.PlayerUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MultiversalExtrapolatorItem extends Item {

    private static final List<String> UNIVERSES = Lists.newArrayList();

    public MultiversalExtrapolatorItem(Item.Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getOrCreateTag().contains("Universe") && getUniverses().contains(stack.getOrCreateTag().getString("Universe"))) {
            tooltip.add(new TranslationTextComponent("universe." + stack.getOrCreateTag().getString("Universe")).func_240701_a_(TextFormatting.GOLD));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!stack.getOrCreateTag().contains("Universe") || !getUniverses().contains(stack.getOrCreateTag().getString("Universe"))) {
            stack.getOrCreateTag().putBoolean("Searching", true);
            return ActionResult.resultSuccess(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack != newStack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity && stack.getOrCreateTag().getBoolean("Searching")) {
            int progress = stack.getOrCreateTag().getInt("SearchTimer");

            if (progress < 80) {
                stack.getOrCreateTag().putInt("SearchTimer", progress + 1);

                if (progress < 60) {
                    Random random = new Random(entityIn.ticksExisted / 2);
                    String number = random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
                    ((PlayerEntity) entityIn).sendStatusMessage(new TranslationTextComponent("universe.earth_search", number), true);

                    if (entityIn.ticksExisted % 2 == 0) {
                        PlayerUtil.playSound((PlayerEntity) entityIn, entityIn.getPosX(), entityIn.getPosY() + entityIn.getHeight() / 2D, entityIn.getPosZ(), TCSounds.MULTIVERSE_SEARCH.get(), SoundCategory.PLAYERS);
                    }
                } else {
                    if (progress == 60) {
                        stack.getOrCreateTag().putString("Universe", getUniverses().get(new Random().nextInt(getUniverses().size())));
                    }

                    ((PlayerEntity) entityIn).sendStatusMessage(new TranslationTextComponent("universe." + stack.getOrCreateTag().getString("Universe")).func_240701_a_(TextFormatting.GOLD), true);
                }
            } else {
                stack.getOrCreateTag().remove("Searching");
                stack.getOrCreateTag().remove("SearchTimer");
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group)) {
            for (String s : getUniverses()) {
                ItemStack stack = new ItemStack(this);
                stack.getOrCreateTag().putString("Universe", s);
                items.add(stack);
            }
        }
    }

    public static boolean hasValidUniverse(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrCreateTag().contains("Universe") && getUniverses().contains(stack.getOrCreateTag().getString("Universe"));
    }

    public static String getUniverse(ItemStack stack) {
        return hasValidUniverse(stack) ? stack.getOrCreateTag().getString("Universe") : null;
    }

    public static String registerUniverse(String id) {
        if (!UNIVERSES.contains(id)) {
            UNIVERSES.add(id);
        }

        return id;
    }

    public static List<String> getUniverses() {
        return ImmutableList.copyOf(UNIVERSES);
    }

    static {
        registerUniverse("earth-18515");
    }
}
