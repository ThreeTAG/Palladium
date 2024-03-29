package net.threetag.palladium.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.compat.curiostinkets.CurioTrinket;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import org.jetbrains.annotations.NotNull;

public class CurioTrinketItem extends Item implements CurioTrinket {

    public CurioTrinketItem(Properties properties) {
        super(properties);
        CuriosTrinketsUtil.getInstance().registerCurioTrinket(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (this.canRightClickEquip() && CuriosTrinketsUtil.getInstance().equipItem(player, stack)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return super.use(level, player, usedHand);
    }
}
