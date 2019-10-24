package net.threetag.threecore.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.abilities.capability.ItemAbilityContainer;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.*;

public class OpeningNbtTimerAbility extends Ability {

    public static final ThreeData<Integer> MAX = new IntegerThreeData("max_timer").setSyncType(EnumSync.NONE).enableSetting("Sets the maximum value for the timer, there it goes from 0 to this value-1. You define it in ticks (20 ticks = 1 second).");
    public static final ThreeData<String> NBT_TAG = new StringThreeData("nbt_tag").setSyncType(EnumSync.NONE).enableSetting("Determines the nbt tag in the item which will be used.");
    public static final ThreeData<Boolean> OPEN = new BooleanThreeData("open").setSyncType(EnumSync.NONE);

    public OpeningNbtTimerAbility() {
        super(AbilityType.OPENING_NBT_TIMER);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new ItemIcon(new ItemStack(Items.IRON_HELMET)));
        this.dataManager.register(MAX, 10);
        this.dataManager.register(NBT_TAG, "Opening");
        this.dataManager.register(OPEN, false);
    }

    @Override
    public void updateTick(LivingEntity entity) {
        this.dataManager.set(OPEN, !this.dataManager.get(OPEN));
    }

    @Override
    public void tick(LivingEntity entity) {
        super.tick(entity);

        if (!entity.world.isRemote && this.container instanceof ItemAbilityContainer) {
            CompoundNBT nbt = ((ItemAbilityContainer) this.container).stack.getOrCreateTag();
            String tag = this.dataManager.get(NBT_TAG);
            int timer = nbt.getInt(tag);
            int maxTimer = this.dataManager.get(MAX);
            boolean open = this.dataManager.get(OPEN);

            if (open && timer < maxTimer) {
                nbt.putInt(tag, timer + 1);
            } else if (!open && timer > 0) {
                nbt.putInt(tag, timer - 1);
            }
        }
    }
}
