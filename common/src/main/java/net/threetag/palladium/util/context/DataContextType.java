package net.threetag.palladium.util.context;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityInstance;

@SuppressWarnings("InstantiationOfUtilityClass")
public class DataContextType<T> {

    public static DataContextType<Entity> ENTITY = new DataContextType<>();
    public static DataContextType<Level> LEVEL = new DataContextType<>();
    public static DataContextType<ItemStack> ITEM = new DataContextType<>();
    public static DataContextType<EquipmentSlot> SLOT = new DataContextType<>();
    public static DataContextType<AbilityInstance> ABILITY = new DataContextType<>();
    public static DataContextType<Power> POWER = new DataContextType<>();
    public static DataContextType<IPowerHolder> POWER_HOLDER = new DataContextType<>();
    public static DataContextType<Integer> ABILITY_WHEEL_SELECTION = new DataContextType<>();

}
