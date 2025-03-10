package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RestrictSlotsAbility extends Ability {

    public static final MapCodec<RestrictSlotsAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(PlayerSlot.CODEC).fieldOf("slots").forGetter(ab -> ab.slots),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, RestrictSlotsAbility::new));

    public final List<PlayerSlot> slots;

    public RestrictSlotsAbility(List<PlayerSlot> slots, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.slots = slots;
    }

    @Override
    public AbilitySerializer<RestrictSlotsAbility> getSerializer() {
        return AbilitySerializers.RESTRICT_SLOTS.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> entry, boolean enabled) {
        if (enabled && !entity.level().isClientSide) {
            for (PlayerSlot slot : this.slots) {
                var items = slot.getItems(entity);
                slot.clear(entity);
                for (ItemStack item : items) {
                    if (!item.isEmpty()) {
                        this.drop(entity, item, slot);
                    }
                }
            }
        }
    }

    public void drop(LivingEntity entity, ItemStack stack, PlayerSlot slot) {
        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, true);
            }
        } else {
            entity.spawnAtLocation((ServerLevel) entity.level(), stack);
        }
    }

    public static boolean isRestricted(LivingEntity entity, EquipmentSlot slot) {
        for (AbilityInstance<RestrictSlotsAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : instance.getAbility().slots) {
                if (playerSlot.getEquipmentSlot() == slot) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isRestricted(LivingEntity entity, String key) {
        for (AbilityInstance<RestrictSlotsAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : instance.getAbility().slots) {
                if (playerSlot.toString().equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class Serializer extends AbilitySerializer<RestrictSlotsAbility> {

        @Override
        public MapCodec<RestrictSlotsAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, RestrictSlotsAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Restricts the slots of the entity.")
                    .add("slots", Documented.typePlayerSlots(), "The slots that should be restricted.")
                    .setExampleObject(new RestrictSlotsAbility(List.of(PlayerSlot.get(EquipmentSlot.CHEST)), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }

    public static class OverridingSlot extends ArmorSlot {

        public OverridingSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, @Nullable ResourceLocation emptyIcon) {
            super(container, owner, slot, slotIndex, x, y, emptyIcon);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return super.mayPlace(stack) && !RestrictSlotsAbility.isRestricted(this.owner, this.slot);
        }
    }
}
