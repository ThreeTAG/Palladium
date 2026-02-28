package net.threetag.palladium.customization;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumHubData;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.network.*;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;

public class EntityCustomizationHandler extends PalladiumEntityData<LivingEntity, EntityCustomizationHandler> {

    public static final Codec<EntityCustomizationHandler> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(CustomizationCategory.HOLDER_CODEC, Customization.Codecs.HOLDER_CODEC).optionalFieldOf("selected", Collections.emptyMap()).forGetter(h -> h.selected),
            Customization.Codecs.HOLDER_CODEC.listOf().optionalFieldOf("unlocked", Collections.emptyList()).forGetter(h -> h.unlocked),
            Codec.LONG.optionalFieldOf("eye_selection", 0L).forGetter(h -> h.eyeSelection)
    ).apply(instance, EntityCustomizationHandler::new));

    private final Map<Holder<CustomizationCategory>, Holder<Customization>> selected;
    private final List<Holder<Customization>> unlocked;
    private long eyeSelection;

    public EntityCustomizationHandler(Map<Holder<CustomizationCategory>, Holder<Customization>> selected, List<Holder<Customization>> unlocked, long eyeSelection) {
        this.selected = new HashMap<>(selected);
        this.unlocked = new ArrayList<>(unlocked);
        this.eyeSelection = eyeSelection;
    }

    @Override
    protected void init() {
        if (this.getEntity() instanceof Player)
            this.validateUnlocked(PalladiumHubData.get(this.getEntity(), PalladiumEntityDataTypes.PALLADIUM_HUB.get()));
        this.validateSelected();
    }

    public boolean unlock(Holder<Customization> customizationHolder) {
        if (this.isUnlocked(customizationHolder)) {
            return false;
        } else if (this.getEntity().level().isClientSide()) {
            this.unlocked.add(customizationHolder);
            if (this.getEntity() instanceof Player player) {
                Palladium.PROXY.showCustomizationToast(player, customizationHolder.value());
            }
            return true;
        } else if (this.getEntity() instanceof ServerPlayer serverPlayer && customizationHolder.value().unlockedBy() == Customization.UnlockedBy.COMMAND) {
            this.unlocked.add(customizationHolder);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncUnlockedCustomizationPacket(customizationHolder));
            return true;
        } else {
            return false;
        }
    }

    public boolean lock(Holder<Customization> customizationHolder) {
        if (this.getEntity() instanceof ServerPlayer serverPlayer && customizationHolder.value().unlockedBy() == Customization.UnlockedBy.COMMAND) {
            this.unlocked.remove(this.unlocked.stream().filter(holder -> holder.value() == customizationHolder.value()).findFirst().orElse(null));
            PacketDistributor.sendToPlayer(serverPlayer, new SyncUnlockedCustomizationsPacket(this.unlocked));

            if (this.isSelected(customizationHolder)) {
                this.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY)
                        .get(customizationHolder.value().getCategoryKey()).ifPresent(holder -> {
                            if (holder.value().requiresSelection()) {
                                this.select(holder.value().getDefaultValue(this.registryAccess()));
                            } else {
                                this.unselect(holder);
                            }
                        });
            }

            return true;
        } else {
            return false;
        }
    }

    public void setUnlocked(List<Holder<Customization>> unlocked) {
        if (this.getEntity().level().isClientSide()) {
            this.unlocked.clear();
            this.unlocked.addAll(unlocked);
        }
    }

    public boolean isUnlocked(Customization customization) {
        if (!(this.getEntity() instanceof Player)) {
            return true;
        }

        var type = customization.unlockedBy();
        if (type == Customization.UnlockedBy.COMMAND || type == Customization.UnlockedBy.REWARD) {
            for (Holder<Customization> holder : this.unlocked) {
                if (holder.value() == customization) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean isUnlocked(Holder<Customization> customizationHolder) {
        return this.isUnlocked(customizationHolder.value());
    }

    public boolean select(Holder<Customization> accessory) {
        boolean unlocked = this.isUnlocked(accessory) || this.getEntity().level().isClientSide();

        this.getEntity().registryAccess().get(accessory.value().getCategoryKey()).ifPresent(ref -> {
            if (unlocked) {
                this.selected.put(ref, accessory);

                if (!this.getEntity().level().isClientSide()) {
                    PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.getEntity(),
                            new SyncEntityCustomizationPacket(this.getEntity().getId(), accessory));
                }
            }
        });

        return unlocked;
    }

    public void unselect(Holder<CustomizationCategory> categoryHolder) {
        if (categoryHolder.value().requiresSelection()) {
            return;
        }

        this.selected.remove(this.selected.keySet().stream().filter(holder -> holder.value() == categoryHolder.value()).findFirst().orElse(null));

        if (!this.getEntity().level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.getEntity(),
                    new SyncEntityUnselectCustomizationPacket(this.getEntity().getId(), categoryHolder));
        }
    }

    public Holder<Customization> get(Holder<CustomizationCategory> categoryHolder) {
        return this.selected.get(categoryHolder);
    }

    public Collection<Holder<Customization>> getSelected() {
        return this.selected.values();
    }

    public boolean isSelected(Holder<Customization> customization) {
        for (Holder<Customization> holder : this.selected.values()) {
            if (holder.value() == customization.value()) {
                return true;
            }
        }
        return false;
    }

    public void validateUnlocked(PalladiumHubData hubData) {
        if (this.getEntity() instanceof ServerPlayer serverPlayer) {
            ArrayList<Holder<Customization>> newUnlocked = new ArrayList<>();
            var registry = serverPlayer.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION);
            for (ResourceKey<Customization> customization : registry.registryKeySet()) {
                var holder = registry.get(customization).orElseThrow();

                if ((holder.value().unlockedBy() == Customization.UnlockedBy.REWARD && hubData.hasCustomizationUnlocked(holder.value()))
                        || (holder.value().unlockedBy() == Customization.UnlockedBy.COMMAND && this.unlocked.contains(holder))) {
                    newUnlocked.add(holder);
                }
            }

            this.unlocked.clear();
            this.unlocked.addAll(newUnlocked);

            PacketDistributor.sendToPlayer(serverPlayer, new SyncUnlockedCustomizationsPacket(this.unlocked));

            for (Holder<Customization> customizationHolder : ImmutableList.copyOf(this.getSelected())) {
                if (!this.isUnlocked(customizationHolder)) {
                    this.unselect(customizationHolder.value().getCategory(this.registryAccess()));
                }
            }
        }
    }

    private void validateSelected() {
        this.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).asHolderIdMap().forEach(holder -> {
            if (holder.value().requiresSelection()) {
                if (!this.selected.containsKey(holder)) {
                    this.select(holder.value().getDefaultValue(this.registryAccess()));
                }
            }
        });
    }

    public long getEyeSelection() {
        return this.eyeSelection;
    }

    public void setEyeSelection(long eyeSelection) {
        this.eyeSelection = eyeSelection;

        if (eyeSelection == 0L) {
            this.eyeSelection = EyeSelectionUtil.DEFAULT_EYES;
        }

        if (!this.getEntity().level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.getEntity(), SyncEntityEyeSelection.create(this.getEntity()));
        }
    }

    @Override
    public Codec<EntityCustomizationHandler> codec() {
        return CODEC;
    }

    public static EntityCustomizationHandler get(LivingEntity living) {
        return PalladiumEntityData.get(living, PalladiumEntityDataTypes.CUSTOMIZATION_HANDLER.get());
    }
}
