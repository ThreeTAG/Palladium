package net.threetag.palladium.fabric.platform;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryBuilder;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.platform.RegistryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FabricRegistries implements RegistryService {

    @Override
    public <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        Registry registry = BuiltInRegistries.REGISTRY.getValue(registryResourceKey.location());
        Registry.register(Objects.requireNonNull(registry), id, object);
    }

    @Override
    public <T> Registry<T> createRegistry(RegistryBuilder<T> registryBuilder) {
        var builder = registryBuilder.getDefaultKey() != null ? FabricRegistryBuilder.createDefaulted(registryBuilder.getResourceKey(), registryBuilder.getDefaultKey()) : FabricRegistryBuilder.createSimple(registryBuilder.getResourceKey());

        if (registryBuilder.isSynced()) {
            builder.attribute(RegistryAttribute.SYNCED);
        }

        return builder.buildAndRegister();
    }

    @Override
    public <T> void createDataPackRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> networkCodec) {
        if (networkCodec == null) {
            DynamicRegistries.register(key, dataCodec);
        } else {
            DynamicRegistries.registerSynced(key, dataCodec, networkCodec);
        }
    }

    @Override
    public <T> DeferredRegister<T> createDeferredRegister(String modId, ResourceKey<? extends Registry<T>> resourceKey) {
        return new Impl<>(modId, resourceKey);
    }

    @Override
    public DeferredRegister.Items createDeferredItemRegister(String modId) {
        return new ItemsImpl(modId);
    }

    public static class Impl<T> extends DeferredRegister<T> {

        public final String modid;
        private final Registry<T> registry;
        private final List<RegistryHolder<T>> entries;

        public Impl(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
            this.modid = modid;
            this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(resourceKey.location());
            this.entries = new ArrayList<>();
        }

        @Override
        public void register() {

        }

        @Override
        public <R extends T> RegistryHolder<R> register(String id, Supplier<R> supplier) {
            ResourceKey<R> registeredId = (ResourceKey<R>) ResourceKey.create(this.registry.key(), ResourceLocation.fromNamespaceAndPath(this.modid, id));
            Registry registry1 = this.registry;
            Registry.register(registry1, registeredId, supplier.get());
            RegistryHolder registryHolder = new RegistryHolderImpl(registeredId, this.registry);
            this.entries.add(registryHolder);
            return registryHolder;
        }

        @Override
        public <R extends T> RegistryHolder<R> register(String id, Function<ResourceLocation, R> function) {
            ResourceKey<R> registeredId = (ResourceKey<R>) ResourceKey.create(this.registry.key(), ResourceLocation.fromNamespaceAndPath(this.modid, id));
            Registry registry1 = this.registry;
            Registry.register(registry1, registeredId, function.apply(registeredId.location()));
            RegistryHolder registryHolder = new RegistryHolderImpl(registeredId, this.registry);
            this.entries.add(registryHolder);
            return registryHolder;
        }

        @Override
        public Collection<RegistryHolder<T>> getEntries() {
            return ImmutableList.copyOf(this.entries);
        }
    }

    @SuppressWarnings("unchecked")
    public static class ItemsImpl extends DeferredRegister.Items {

        public final String modId;
        private final List<RegistryHolder<Item>> entries;

        public ItemsImpl(String modId) {
            this.modId = modId;
            this.entries = new ArrayList<>();
        }

        @Override
        public void register() {

        }

        @Override
        public <R extends Item> RegistryHolder<R> register(String id, Supplier<R> supplier) {
            ResourceKey<Item> registeredId = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(this.modId, id));
            Registry.register(BuiltInRegistries.ITEM, registeredId, supplier.get());
            RegistryHolder<Item> registryHolder = new RegistryHolderImpl<Item>(registeredId, BuiltInRegistries.ITEM);
            this.entries.add(registryHolder);
            return (RegistryHolder<R>) registryHolder;
        }

        @Override
        public <R extends Item> RegistryHolder<R> register(String id, Function<ResourceLocation, R> function) {
            ResourceKey<Item> registeredId = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(this.modId, id));
            Registry.register(BuiltInRegistries.ITEM, registeredId, function.apply(registeredId.location()));
            RegistryHolder<Item> registryHolder = new RegistryHolderImpl<Item>(registeredId, BuiltInRegistries.ITEM);
            this.entries.add(registryHolder);
            return (RegistryHolder<R>) registryHolder;
        }

        @Override
        public <I extends Item> RegistryHolder<I> registerItem(String id, Function<Item.Properties, I> function) {
            var props = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(this.modId, id)));
            return this.register(id, () -> function.apply(props));
        }

        @Override
        public Collection<RegistryHolder<Item>> getEntries() {
            return ImmutableList.copyOf(this.entries);
        }

    }

    public static class RegistryHolderImpl<T> extends RegistryHolder<T> {

        private final ResourceKey<T> id;
        private final Holder<T> holder;

        public RegistryHolderImpl(ResourceKey<T> id, Registry<T> registry) {
            this.id = id;
            this.holder = registry.get(id).orElseThrow();
        }

        @Override
        public ResourceLocation getId() {
            return this.id.location();
        }

        @Override
        public @NotNull T value() {
            return this.holder.value();
        }

        @Override
        public boolean isBound() {
            return this.holder.isBound();
        }

        @Override
        public boolean is(ResourceLocation location) {
            return this.holder.is(location);
        }

        @Override
        public boolean is(ResourceKey<T> resourceKey) {
            return this.holder.is(resourceKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicate) {
            return this.holder.is(predicate);
        }

        @Override
        public boolean is(TagKey<T> tagKey) {
            return this.holder.is(tagKey);
        }

        @Override
        public boolean is(Holder<T> holder) {
            return this.holder.is(holder);
        }

        @Override
        public @NotNull Stream<TagKey<T>> tags() {
            return this.holder.tags();
        }

        @Override
        public @NotNull Either<ResourceKey<T>, T> unwrap() {
            return this.holder.unwrap();
        }

        @Override
        public @NotNull Optional<ResourceKey<T>> unwrapKey() {
            return this.holder.unwrapKey();
        }

        @Override
        public @NotNull Holder.Kind kind() {
            return this.holder.kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> owner) {
            return this.holder.canSerializeIn(owner);
        }
    }
}
