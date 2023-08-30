package net.threetag.palladium.util.context;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DataContext {

    private final Map<DataContextType<?>, Object> values = new HashMap<>();

    private DataContext() {
    }

    public static DataContext create() {
        return new DataContext();
    }

    public static DataContext forEntity(Entity entity) {
        return create().with(DataContextType.ENTITY, entity).with(DataContextType.LEVEL, entity.level());
    }

    public static DataContext forArmorInSlot(LivingEntity entity, EquipmentSlot slot) {
        return forEntity(entity).with(DataContextType.SLOT, slot).with(DataContextType.ITEM, entity.getItemBySlot(slot));
    }

    public static DataContext forItem(Entity entity, ItemStack stack) {
        return forEntity(entity).with(DataContextType.ITEM, stack);
    }

    public static DataContext forPower(LivingEntity entity, IPowerHolder powerHolder) {
        var context = forEntity(entity);

        if (powerHolder != null) {
            context.with(DataContextType.POWER_HOLDER, powerHolder);
            context.with(DataContextType.POWER, powerHolder.getPower());
        }

        return context;
    }

    public static DataContext forAbility(LivingEntity entity, AbilityEntry abilityEntry) {
        var context = forEntity(entity);

        if (abilityEntry != null) {
            context.with(DataContextType.ABILITY, abilityEntry);
            context.with(DataContextType.POWER_HOLDER, abilityEntry.getHolder());
            context.with(DataContextType.POWER, abilityEntry.getHolder().getPower());
        }

        return context;
    }

    public <T> DataContext with(DataContextType<T> type, T value) {
        this.values.put(type, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(DataContextType<T> type) {
        return (T) this.values.get(type);
    }

    public boolean has(DataContextType<?> type) {
        return this.values.containsKey(type);
    }

    public DataContext copy() {
        var context = new DataContext();
        context.values.putAll(this.values);
        return context;
    }

    @Nullable
    public Entity getEntity() {
        return this.get(DataContextType.ENTITY);
    }

    @Nullable
    public LivingEntity getLivingEntity() {
        var entity = this.getEntity();
        return entity instanceof LivingEntity living ? living : null;
    }

    @Nullable
    public Player getPlayer() {
        var entity = this.getEntity();
        return entity instanceof Player player ? player : null;
    }

    @Nullable
    public Level getLevel() {
        return this.get(DataContextType.LEVEL);
    }

    @NotNull
    public ItemStack getItem() {
        return this.has(DataContextType.ITEM) ? this.get(DataContextType.ITEM) : ItemStack.EMPTY;
    }

    @Nullable
    public EquipmentSlot getSlot() {
        return this.get(DataContextType.SLOT);
    }

    @Nullable
    public AbilityEntry getAbility() {
        return this.get(DataContextType.ABILITY);
    }

    @Nullable
    public Power getPower() {
        return this.get(DataContextType.POWER);
    }

    @Nullable
    public IPowerHolder getPowerHolder() {
        return this.get(DataContextType.POWER_HOLDER);
    }

}
