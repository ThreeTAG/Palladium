package net.threetag.palladium.customization;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncEntityCustomizationPacket;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;

public class EntityCustomizationHandler extends PalladiumEntityData<LivingEntity> {

    private final Map<Holder<CustomizationCategory>, Holder<Customization>> selected = new HashMap<>();
    private final List<Holder<Customization>> unlocked = new ArrayList<>();

    public EntityCustomizationHandler(LivingEntity entity) {
        super(entity);
    }

    public boolean unlock(Holder<Customization> customizationHolder) {
        if (customizationHolder.value().unlockedBy() == Customization.UnlockedBy.COMMAND) {
            this.unlocked.add(customizationHolder);
            return true;
        } else {
            return false;
        }
    }

    public boolean lock(Holder<Customization> customizationHolder) {
        if (customizationHolder.value().unlockedBy() == Customization.UnlockedBy.COMMAND) {
            this.unlocked.remove(customizationHolder);
            return true;
        } else {
            return false;
        }
    }

    public boolean isUnlocked(Holder<Customization> accessory) {
        if (!(this.getEntity() instanceof Player)) {
            return true;
        }

        var type = accessory.value().unlockedBy();
        if (type == Customization.UnlockedBy.COMMAND) {
            return this.unlocked.contains(accessory);
        } else if (type == Customization.UnlockedBy.REWARD) {
            // TODO
            return true;
        } else {
            return true;
        }
    }

    public boolean select(Holder<Customization> accessory) {
        boolean unlocked = this.isUnlocked(accessory) || this.getEntity().level().isClientSide;

        this.getEntity().registryAccess().get(accessory.value().getSlot()).ifPresent(ref -> {
            if (unlocked) {
                this.selected.put(ref, accessory);

                if (!this.getEntity().level().isClientSide) {
                    PalladiumNetwork.sendToTrackingAndSelf(this.getEntity(),
                            new SyncEntityCustomizationPacket(this.getEntity().getId(), accessory.unwrapKey().orElseThrow()));
                }
            }
        });

        return unlocked;
    }

    public void unselect(Holder<CustomizationCategory> slot) {
        this.selected.remove(slot);
    }

    public Holder<Customization> get(Holder<CustomizationCategory> slot) {
        return this.selected.get(slot);
    }

    public Collection<Holder<Customization>> getSelected() {
        return this.selected.values();
    }

    @Override
    public void load(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        this.selected.clear();
        this.unlocked.clear();

        if (nbt.contains("selected")) {
            var entry = nbt.getCompound("selected");
            for (String slotId : entry.getAllKeys()) {
                registryLookup
                        .get(ResourceKey.create(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY, ResourceLocation.parse(slotId)))
                        .ifPresent(slotRef -> registryLookup
                                .get(ResourceKey.create(PalladiumRegistryKeys.CUSTOMIZATION, ResourceLocation.parse(entry.getString(slotId))))
                                .ifPresent(accRef -> this.selected.put(slotRef, accRef)));
            }
        }

        if (nbt.contains("unlocked")) {
            for (Tag tag : nbt.getList("unlocked", Tag.TAG_STRING)) {
                registryLookup
                        .get(ResourceKey.create(PalladiumRegistryKeys.CUSTOMIZATION, ResourceLocation.parse(tag.getAsString())))
                        .ifPresent(this.unlocked::add);
            }
        }
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registryLookup) {
        var nbt = new CompoundTag();

        var selectedTag = new CompoundTag();
        this.selected.forEach((slot, acc) ->
                selectedTag.putString(slot.getRegisteredName(), acc.getRegisteredName()));
        nbt.put("selected", selectedTag);

        var unlockedTag = new ListTag();
        this.unlocked.forEach(acc -> unlockedTag.add(StringTag.valueOf(acc.getRegisteredName())));
        nbt.put("unlocked", unlockedTag);

        return nbt;
    }

    public static EntityCustomizationHandler get(LivingEntity living) {
        return PalladiumEntityData.get(living, PalladiumEntityDataTypes.CUSTOMIZATION_HANDLER.get());
    }
}
