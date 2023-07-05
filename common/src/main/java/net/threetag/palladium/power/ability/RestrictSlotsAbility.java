package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PlayerSlotListProperty;
import net.threetag.palladium.util.property.SyncType;

import java.util.Collections;
import java.util.List;

public class RestrictSlotsAbility extends Ability {

    public static final PalladiumProperty<List<PlayerSlot>> SLOTS = new PlayerSlotListProperty("slots").sync(SyncType.NONE).configurable("Slot(s) that will not be able to contain items anymore.");

    public RestrictSlotsAbility() {
        this.withProperty(SLOTS, Collections.singletonList(PlayerSlot.get(EquipmentSlot.CHEST)));
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && !entity.level.isClientSide) {
            for (PlayerSlot slot : entry.getProperty(SLOTS)) {
                for (ItemStack item : slot.getItems(entity)) {
                    if (!item.isEmpty()) {
                        this.drop(entity, item, slot);
                    }
                }
                slot.clear(entity);
            }
        }
    }

    public void drop(LivingEntity entity, ItemStack stack, PlayerSlot slot) {
        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, true);
            }
        } else {
            entity.spawnAtLocation(stack);
        }
    }

    public static boolean isRestricted(LivingEntity entity, EquipmentSlot slot) {
        for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : entry.getProperty(SLOTS)) {
                if (playerSlot.getEquipmentSlot() == slot) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isRestricted(LivingEntity entity, String key) {
        for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : entry.getProperty(SLOTS)) {
                if (playerSlot.toString().equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getDocumentationDescription() {
        return "Let's you restrict the slots that can be used by the player. It will drop the items if disabled while the player has items in the restricted slots.";
    }
}
