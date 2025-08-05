package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.level.storage.ValueInput;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncEntityCustomizationPacket;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;

public class EntityCustomizationHandler extends PalladiumEntityData<LivingEntity, EntityCustomizationHandler> {

    public static final MapCodec<EntityCustomizationHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.unboundedMap(CustomizationCategory.HOLDER_CODEC, Customization.Codecs.HOLDER_CODEC).optionalFieldOf("selected", Collections.emptyMap()).forGetter(h -> h.selected),
            Customization.Codecs.HOLDER_CODEC.listOf().optionalFieldOf("unlocked", Collections.emptyList()).forGetter(h -> h.unlocked)
    ).apply(instance, EntityCustomizationHandler::new));

    private Map<Holder<CustomizationCategory>, Holder<Customization>> selected;
    private List<Holder<Customization>> unlocked;

    public EntityCustomizationHandler(Map<Holder<CustomizationCategory>, Holder<Customization>> selected, List<Holder<Customization>> unlocked) {
        this.selected = new HashMap<>(selected);
        this.unlocked = new ArrayList<>(unlocked);
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
    public MapCodec<EntityCustomizationHandler> codec() {
        return CODEC;
    }

    public static EntityCustomizationHandler get(LivingEntity living) {
        return PalladiumEntityData.get(living, PalladiumEntityDataTypes.CUSTOMIZATION_HANDLER.get());
    }
}
