package net.threetag.palladium.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.ability.AbilityInstance;

public class DataContextKeys {

    public static ContextKey<Entity> ENTITY = create("entity");
    public static ContextKey<BlockPos> BLOCK_POS = create("block_pos");
    public static ContextKey<Level> LEVEL = create("level");
    public static ContextKey<ItemStack> ITEM = create("item");
    public static ContextKey<PlayerSlot> SLOT = create("slot");
    public static ContextKey<AbilityInstance<?>> ABILITY_INSTANCE = create("ability_instance");
    public static ContextKey<Holder<Power>> POWER = create("power");
    public static ContextKey<PowerInstance> POWER_INSTANCE = create("power_instance");

    private static <T> ContextKey<T> create(String name) {
        return new ContextKey<>(Palladium.id(name));
    }

}
