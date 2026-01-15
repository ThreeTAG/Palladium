package net.threetag.palladium.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DataContext {

    private final Map<ContextKey<?>, Object> values = new HashMap<>();

    private DataContext() {
    }

    public static DataContext create() {
        return new DataContext();
    }

    public static DataContext forEntity(Entity entity) {
        return create()
                .with(DataContextKeys.ENTITY, entity)
                .with(DataContextKeys.LEVEL, entity.level())
                .with(DataContextKeys.BLOCK_POS, entity.blockPosition());
    }

    public static DataContext forItemInEquipmentSlot(LivingEntity entity, EquipmentSlot slot) {
        return forEntity(entity).with(DataContextKeys.SLOT, PlayerSlot.get(slot)).with(DataContextKeys.ITEM, entity.getItemBySlot(slot));
    }

    public static DataContext forItemInSlot(LivingEntity entity, PlayerSlot slot, ItemStack stack) {
        return forItem(entity, stack).with(DataContextKeys.SLOT, slot);
    }

    public static DataContext forItem(Entity entity, ItemStack stack) {
        return forEntity(entity).with(DataContextKeys.ITEM, stack);
    }

    public static DataContext forPower(LivingEntity entity, PowerHolder powerHolder) {
        var context = forEntity(entity);

        if (powerHolder != null) {
            context.with(DataContextKeys.POWER_HOLDER, powerHolder);
            context.with(DataContextKeys.POWER, powerHolder.getPower());
        }

        return context;
    }

    public static DataContext forAbility(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        var context = forEntity(entity);

        if (abilityInstance != null) {
            context.with(DataContextKeys.ABILITY_INSTANCE, abilityInstance);
            context.with(DataContextKeys.POWER_HOLDER, abilityInstance.getHolder());
            context.with(DataContextKeys.POWER, abilityInstance.getHolder().getPower());
        }

        return context;
    }

    public <T> DataContext with(ContextKey<T> type, T value) {
        this.values.put(type, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ContextKey<T> type) {
        return (T) this.values.get(type);
    }

    public boolean has(ContextKey<?> type) {
        return this.values.containsKey(type);
    }

    public DataContext copy() {
        var context = new DataContext();
        context.values.putAll(this.values);
        return context;
    }

    @Nullable
    public Entity getEntity() {
        return this.get(DataContextKeys.ENTITY);
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
    public BlockPos getBlockPos() {
        return this.get(DataContextKeys.BLOCK_POS);
    }

    @Nullable
    public Level getLevel() {
        return this.get(DataContextKeys.LEVEL);
    }

    @NotNull
    public ItemStack getItem() {
        return this.has(DataContextKeys.ITEM) ? this.get(DataContextKeys.ITEM) : ItemStack.EMPTY;
    }

    @Nullable
    public PlayerSlot getSlot() {
        return this.get(DataContextKeys.SLOT);
    }

    @Nullable
    public AbilityInstance<?> getAbility() {
        return this.get(DataContextKeys.ABILITY_INSTANCE);
    }

    @Nullable
    public Holder<Power> getPower() {
        return this.get(DataContextKeys.POWER);
    }

    @Nullable
    public PowerHolder getPowerHolder() {
        return this.get(DataContextKeys.POWER_HOLDER);
    }

}
