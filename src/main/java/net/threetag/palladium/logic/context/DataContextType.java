package net.threetag.palladium.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;

@SuppressWarnings("InstantiationOfUtilityClass")
public class DataContextType<T> {

    public static DataContextType<Entity> ENTITY = new DataContextType<>();
    public static DataContextType<BlockPos> BLOCK_POS = new DataContextType<>();
    public static DataContextType<Level> LEVEL = new DataContextType<>();
    public static DataContextType<ItemStack> ITEM = new DataContextType<>();
    public static DataContextType<PlayerSlot> SLOT = new DataContextType<>();
    public static DataContextType<AbilityInstance<?>> ABILITY_INSTANCE = new DataContextType<>();
    public static DataContextType<Holder<Power>> POWER = new DataContextType<>();
    public static DataContextType<PowerHolder> POWER_HOLDER = new DataContextType<>();

}
