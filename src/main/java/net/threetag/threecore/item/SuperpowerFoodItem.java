package net.threetag.threecore.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.threetag.threecore.ability.superpower.Superpower;
import net.threetag.threecore.ability.superpower.SuperpowerManager;

public class SuperpowerFoodItem extends Item {

    private final ResourceLocation superpower;
    private final int lifetime;

    public SuperpowerFoodItem(Properties properties, ResourceLocation superpower, int lifetime) {
        super(properties);
        this.superpower = superpower;
        this.lifetime = lifetime;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);

        if (!worldIn.isRemote) {
            Superpower superpower = SuperpowerManager.getInstance().getSuperpower(this.superpower);

            if (superpower != null) {
                SuperpowerManager.addSuperpower(entityLiving, superpower, this.lifetime);
            }
        }

        return itemstack;
    }
}
