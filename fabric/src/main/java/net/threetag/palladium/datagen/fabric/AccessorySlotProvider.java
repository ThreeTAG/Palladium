package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.AccessorySlot;
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
                AccessorySlot.orientation(3F, 0, 85, 0, 15, 40, 0),
                AccessorySlot.orientation(2.5F, 0, 260, 0, 15, 40, 0));
        this.add(provider, "head",
                AccessorySlot.orientation(3F, 0, 75, 0, 15, 40, 0),
                AccessorySlot.orientation(2.5F, 0, 230, 0, 15, 40, 0));
        this.add(provider, "face",
                AccessorySlot.orientation(3F, 0, 80, 0, -15, 0, 0),
                AccessorySlot.orientation(3F, 0, 300, 0, -15, 0, 0));
        this.add(provider, "chest");
        this.add(provider, "back");
        this.add(provider, "main_arm");
        this.add(provider, "off_arm");
        this.add(provider, "main_hand");
        this.add(provider, "off_hand");
        this.add(provider, "right_leg");
        this.add(provider, "left_leg");
        this.add(provider, "special");
    }

    private void add(BiConsumer<ResourceLocation, AccessorySlot> provider, String id) {
        this.add(provider, id, AccessorySlot.Orientation.DEFAULT);
    }

    private void add(BiConsumer<ResourceLocation, AccessorySlot> provider, String id, AccessorySlot.Orientation orientation) {
        provider.accept(Palladium.id(id), new AccessorySlot(Component.translatable("accessory_slot.palladium." + id),
                new AccessorySlot.Preview(orientation, orientation)));
    }

    private void add(BiConsumer<ResourceLocation, AccessorySlot> provider, String id, AccessorySlot.Orientation button, AccessorySlot.Orientation player) {
        provider.accept(Palladium.id(id), new AccessorySlot(Component.translatable("accessory_slot.palladium." + id),
                new AccessorySlot.Preview(button, player)));
    }

    @Override
    public String getName() {
        return "Accessory Slots";
    }
}
