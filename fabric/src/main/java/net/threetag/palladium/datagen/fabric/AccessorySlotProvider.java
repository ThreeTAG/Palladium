package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AccessorySlotProvider extends FabricCodecDataProvider<AccessorySlot> {

    protected AccessorySlotProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PalladiumRegistryKeys.ACCESSORY_SLOT, AccessorySlot.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, AccessorySlot> provider, HolderLookup.Provider lookup) {
        this.add(provider, "hat",
                PoseStackTransformation.create(2.5F, 0, 2.2F, 0, 15, 40, 0));
        this.add(provider, "head",
                PoseStackTransformation.create(2.5F, 0, 2F, 0, 15, 40, 0));
        this.add(provider, "face",
                PoseStackTransformation.create(2.5F, 0, 2F, 0, -15, 0, 0));
        this.add(provider, "chest",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, "back",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 130, 0));
        this.add(provider, "main_arm",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, "off_arm",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, "main_hand",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, "off_hand",
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, "right_leg",
                PoseStackTransformation.create(2.2F, 0, -1F, 0, 15, 40, 0));
        this.add(provider, "left_leg",
                PoseStackTransformation.create(2.2F, 0, -1F, 0, 15, 40, 0));
        this.add(provider, "special");
    }

    private void add(BiConsumer<ResourceLocation, AccessorySlot> provider, String id) {
        this.add(provider, id, AccessorySlot.DEFAULT_PREVIEW);
    }

    private void add(BiConsumer<ResourceLocation, AccessorySlot> provider, String id, PoseStackTransformation transformation) {
        provider.accept(Palladium.id(id), new AccessorySlot(Component.translatable("accessory_slot.palladium." + id),
                transformation));
    }

    @Override
    public String getName() {
        return "Accessory Slots";
    }
}
