package net.threetag.palladium.neoforge.platform;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryBuilder;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.neoforge.PalladiumNeoForge;
import net.threetag.palladium.platform.RegistryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
@EventBusSubscriber(modid = Palladium.MOD_ID)
public class NeoForgeRegistries implements RegistryService {

    private static final List<Entry<?>> ENTRIES = new ArrayList<>();
    private static final List<Registry<?>> REGISTRIES = new ArrayList<>();

    @Override
    public <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        ENTRIES.add(new Entry<>(registryResourceKey, id, object));
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        ENTRIES.forEach(entry -> entry.register(event));
    }

    @Override
    public <T> Registry<T> createRegistry(RegistryBuilder<T> registryBuilder) {
        net.neoforged.neoforge.registries.RegistryBuilder<T> neoBuilder = new net.neoforged.neoforge.registries.RegistryBuilder<>(registryBuilder.getResourceKey()).sync(registryBuilder.isSynced());

        if (registryBuilder.getDefaultKey() != null) {
            neoBuilder.defaultKey(registryBuilder.getDefaultKey());
        }

        var registry = neoBuilder.create();
        REGISTRIES.add(registry);
        return registry;
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        for (Registry<?> registry : REGISTRIES) {
            event.register(registry);
        }
    }

    @Override
    public <T> void createDataPackRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> networkCodec) {
        PalladiumNeoForge.whenModBusAvailable(key.location().getNamespace(), bus -> {
            ResourceKey key1 = key;
            bus.<DataPackRegistryEvent.NewRegistry>addListener(event -> event.dataPackRegistry(key1, dataCodec, networkCodec));
        });
    }

    @Override
    public <T> DeferredRegister<T> createDeferredRegister(String modId, ResourceKey<? extends Registry<T>> resourceKey) {
        return new Impl<>(modId, resourceKey);
    }

    @Override
    public DeferredRegister.Items createDeferredItemRegister(String modId) {
        return new ItemsImpl(modId);
    }

    public record Entry<T>(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) implements Supplier<T> {

        public void register(RegisterEvent event) {
            event.register(this.registryResourceKey, id, this);
        }

        @Override
        public T get() {
            return this.object;
        }
    }

    public static class Impl<T> extends DeferredRegister<T> {

        public final String modId;
        private final net.neoforged.neoforge.registries.DeferredRegister<T> register;
        private final List<RegistryHolder<T>> entries;

        public Impl(String modId, ResourceKey<? extends Registry<T>> resourceKey) {
            this.modId = modId;
            this.register = net.neoforged.neoforge.registries.DeferredRegister.create(resourceKey, modId);
            this.entries = new ArrayList<>();
        }

        @Override
        public void register() {
            this.register.register(PalladiumNeoForge.getModEventBus(this.modId).orElseThrow(() -> new IllegalStateException("Mod '" + this.modId + "' did not register event bus to Palladium!")));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends T> RegistryHolder<R> register(String id, Supplier<R> supplier) {
            DeferredHolder<T, R> orig = this.register.register(id, supplier);
            RegistryHolderImpl<T> registrySupplier = new RegistryHolderImpl<>(orig);
            this.entries.add(registrySupplier);
            return (RegistryHolder<R>) registrySupplier;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends T> RegistryHolder<R> register(String id, Function<ResourceLocation, R> function) {
            DeferredHolder<T, R> orig = this.register.register(id, function);
            RegistryHolderImpl<T> registrySupplier = new RegistryHolderImpl<>(orig);
            this.entries.add(registrySupplier);
            return (RegistryHolder<R>) registrySupplier;
        }

        @Override
        public Collection<RegistryHolder<T>> getEntries() {
            return ImmutableList.copyOf(this.entries);
        }
    }

    public static class ItemsImpl extends DeferredRegister.Items {

        public final String modId;
        private final net.neoforged.neoforge.registries.DeferredRegister<Item> register;
        private final List<RegistryHolder<Item>> entries;

        public ItemsImpl(String modId) {
            this.modId = modId;
            this.register = net.neoforged.neoforge.registries.DeferredRegister.create(Registries.ITEM, modId);
            this.entries = new ArrayList<>();
        }

        @Override
        public void register() {
            this.register.register(PalladiumNeoForge.getModEventBus(this.modId).orElseThrow(() -> new IllegalStateException("Mod '" + this.modId + "' did not register event bus to Palladium!")));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Item> RegistryHolder<R> register(String id, Supplier<R> supplier) {
            DeferredHolder<Item, R> orig = this.register.register(id, supplier);
            RegistryHolderImpl<Item> registrySupplier = new RegistryHolderImpl<Item>(orig);
            this.entries.add(registrySupplier);
            return (RegistryHolder<R>) registrySupplier;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Item> RegistryHolder<R> register(String id, Function<ResourceLocation, R> function) {
            DeferredHolder<Item, R> orig = this.register.register(id, function);
            RegistryHolderImpl<Item> registrySupplier = new RegistryHolderImpl<>(orig);
            this.entries.add(registrySupplier);
            return (RegistryHolder<R>) registrySupplier;
        }

        @Override
        public <I extends Item> RegistryHolder<I> registerItem(String id, Function<Item.Properties, I> function) {
            Item.Properties props = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(this.modId, id)));
            return this.register(id, () -> function.apply(props));
        }

        @Override
        public Collection<RegistryHolder<Item>> getEntries() {
            return ImmutableList.copyOf(this.entries);
        }

    }

    public static class RegistryHolderImpl<T> extends RegistryHolder<T> {

        private final DeferredHolder<T, ? extends T> forgeHolder;

        public RegistryHolderImpl(DeferredHolder<T, ? extends T> forgeHolder) {
            this.forgeHolder = forgeHolder;
        }

        @Override
        public ResourceLocation getId() {
            return this.forgeHolder.getId();
        }

        @Override
        public @NotNull T value() {
            return this.forgeHolder.value();
        }

        @Override
        public boolean isBound() {
            return this.forgeHolder.isBound();
        }

        @Override
        public boolean is(@NotNull ResourceLocation location) {
            return this.forgeHolder.is(location);
        }

        @Override
        public boolean is(@NotNull ResourceKey<T> resourceKey) {
            return this.forgeHolder.is(resourceKey);
        }

        @Override
        public boolean is(@NotNull Predicate<ResourceKey<T>> predicate) {
            return this.forgeHolder.is(predicate);
        }

        @Override
        public boolean is(@NotNull TagKey<T> tagKey) {
            return this.forgeHolder.is(tagKey);
        }

        @Override
        public boolean is(Holder<T> holder) {
            return this.forgeHolder.is(holder);
        }

        @Override
        public @NotNull Stream<TagKey<T>> tags() {
            return this.forgeHolder.tags();
        }

        @Override
        public @NotNull Either<ResourceKey<T>, T> unwrap() {
            return this.forgeHolder.unwrap();
        }

        @Override
        public @NotNull Optional<ResourceKey<T>> unwrapKey() {
            return this.forgeHolder.unwrapKey();
        }

        @Override
        public @NotNull Holder.Kind kind() {
            return this.forgeHolder.kind();
        }

        @Override
        public boolean canSerializeIn(@NotNull HolderOwner<T> owner) {
            return this.forgeHolder.canSerializeIn(owner);
        }
    }
}
