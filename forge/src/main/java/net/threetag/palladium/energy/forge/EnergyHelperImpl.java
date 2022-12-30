package net.threetag.palladium.energy.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.energy.IEnergyStorage;
import net.threetag.palladium.item.EnergyItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnergyHelperImpl {

    @SubscribeEvent
    public static void initCapability(AttachCapabilitiesEvent<ItemStack> e) {
        if (e.getObject().getItem() instanceof EnergyItem) {
            e.addCapability(Palladium.id("energy"), new ItemCapProvider(e.getObject()));
        }
    }

    public static Optional<IEnergyStorage> getFromItemStack(ItemStack stack) {
        AtomicReference<Optional<IEnergyStorage>> optional = new AtomicReference<>(Optional.empty());
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(en -> optional.set(Optional.of(new Wrapper(en))));
        return optional.get();
    }

    public static Optional<IEnergyStorage> getFromBlockEntity(Level level, BlockPos pos, @Nullable Direction side) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        AtomicReference<Optional<IEnergyStorage>> optional = new AtomicReference<>(Optional.empty());

        if (blockEntity != null) {
            blockEntity.getCapability(ForgeCapabilities.ENERGY, side).ifPresent(storage -> {
                optional.set(Optional.of(new Wrapper(storage)));
            });
        }

        return optional.get();
    }

    public static long moveBetweenBlockEntities(Level level, BlockPos from, Direction fromSide, BlockPos to, Direction toSide, long maxAmount) {
        BlockEntity fromBE = level.getBlockEntity(from);
        BlockEntity toBE = level.getBlockEntity(to);

        if (fromBE == null || toBE == null) {
            return 0;
        }

        AtomicLong result = new AtomicLong();

        fromBE.getCapability(ForgeCapabilities.ENERGY, fromSide).ifPresent(fromStorage -> toBE.getCapability(ForgeCapabilities.ENERGY, toSide).ifPresent(toStorage -> {
            int maxExtracted = fromStorage.extractEnergy((int) maxAmount, true);
            long accepted = toStorage.receiveEnergy(maxExtracted, false);
            result.set(fromStorage.extractEnergy((int) accepted, false));
        }));

        return result.get();
    }

    public record Wrapper(net.minecraftforge.energy.IEnergyStorage forgeStorage) implements IEnergyStorage {

        @Override
        public boolean canInsert() {
            return this.forgeStorage.canReceive();
        }

        @Override
        public int insertEnergy(int maxAmount, boolean simulate) {
            return this.forgeStorage.receiveEnergy(maxAmount, simulate);
        }

        @Override
        public boolean canWithdraw() {
            return this.forgeStorage.canExtract();
        }

        @Override
        public int withdrawEnergy(int maxAmount, boolean simulate) {
            return this.forgeStorage.extractEnergy(maxAmount, simulate);
        }

        @Override
        public int getEnergyAmount() {
            return this.forgeStorage.getEnergyStored();
        }

        @Override
        public int getEnergyCapacity() {
            return this.forgeStorage.getMaxEnergyStored();
        }
    }

    public static class ItemCapProvider implements ICapabilityProvider {

        private final ItemStack stack;
        private final ItemEnergyStorage energyStorage;
        private final LazyOptional<ItemEnergyStorage> energyStorageOptional;

        public ItemCapProvider(ItemStack stack) {
            this.stack = stack;
            var item = (EnergyItem) stack.getItem();
            this.energyStorage = new ItemEnergyStorage(stack, item);
            this.energyStorageOptional = LazyOptional.of(() -> this.energyStorage);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            return capability == ForgeCapabilities.ENERGY ? this.energyStorageOptional.cast() : LazyOptional.empty();
        }
    }

}
