package net.threetag.palladium.condition.context;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

@SuppressWarnings("InstantiationOfUtilityClass")
public class ConditionContextType<T> {

    public static ConditionContextType<LivingEntity> ENTITY = new ConditionContextType<>();
    public static ConditionContextType<Level> LEVEL = new ConditionContextType<>();
    public static ConditionContextType<ItemStack> ITEM = new ConditionContextType<>();
    public static ConditionContextType<EquipmentSlot> SLOT = new ConditionContextType<>();
    public static ConditionContextType<AbilityEntry> ABILITY = new ConditionContextType<>();
    public static ConditionContextType<Power> POWER = new ConditionContextType<>();
    public static ConditionContextType<IPowerHolder> POWER_HOLDER = new ConditionContextType<>();

}
