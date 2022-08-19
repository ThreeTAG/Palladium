package net.threetag.palladium.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CurioTrinketItem extends SortedItem implements CurioTrinket {

    public CurioTrinketItem(Properties properties, CreativeModeTabFiller filler) {
        super(properties, filler);
        CurioTrinketRegistry.registerCurioTrinket(this);
    }

    public CurioTrinketItem(Properties properties) {
        super(properties);
        CurioTrinketRegistry.registerCurioTrinket(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(this.canRightClickEquip() && CurioTrinketRegistry.equipItem(player, stack)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return super.use(level, player, usedHand);
    }
}
