package net.threetag.threecore.abilities.condition;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.client.gui.AbilitiesScreen;
import net.threetag.threecore.abilities.client.gui.BuyAbilityScreen;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ItemStackThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemBuyableAbilityCondition extends BuyableAbilityCondition {

    public static ThreeData<ItemStack> ITEM = new ItemStackThreeData("item").setSyncType(EnumSync.SELF).enableSetting("item", "Determines the item the player must spend to activate this condition.");

    public ItemBuyableAbilityCondition(Ability ability) {
        super(ConditionType.ITEM_BUY, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ITEM, new ItemStack(Items.APPLE));
    }

    @Override
    public void readFromJson(JsonObject json) {
        super.readFromJson(json);
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);

                if (ItemStack.areItemStacksEqual(stack, this.dataManager.get(ITEM))) {
                    b.set(!inv.extractItem(i, this.dataManager.get(ITEM).getCount(), true).isEmpty());
                    break;
                }
            }
        });
        return b.get();
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        // TODO Doesnt work if items are spread over multiple stacks, perhaps look into /clear command
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
            int index = -1;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);

                if (ItemStack.areItemStacksEqual(stack, this.dataManager.get(ITEM))) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                b.set(!inv.extractItem(index, this.dataManager.get(ITEM).getCount(), false).isEmpty());
            }
        });
        return b.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Screen getScreen(AbilitiesScreen screen) {
        return this.dataManager.get(BOUGHT) ? null : new BuyAbilityScreen(this.ability, this,
                new ItemIcon(this.dataManager.get(ITEM)), new TranslationTextComponent("ability.condition.threecore.item_buy.info", this.dataManager.get(ITEM).getCount(), this.dataManager.get(ITEM).getDisplayName()), screen);
    }
}
