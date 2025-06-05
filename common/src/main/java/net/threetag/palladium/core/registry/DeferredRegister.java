package net.threetag.palladium.core.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class to help with managing registry entries.
 * Maintains a list of all suppliers for entries and registers them automatically.
 * Suppliers should return NEW instances every time.
 * Example Usage:
 * <pre>{@code
 *   private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MODID, Registry.ITEM_REGISTRY);
 *   private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MODID, Registry.BLOCK_REGISTRY);
 *
 *   public static final RegistryHolder<Block> ROCK_BLOCK = BLOCKS.register("rock", () -> new Block(Block.Properties.create(Material.ROCK)));
 *   public static final RegistryHolder<Item> ROCK_ITEM = ITEMS.register("rock", () -> new BlockItem(ROCK_BLOCK.get(), new Item.Properties().group(ItemGroup.MISC)));
 *
 *   public ExampleMod() {
 *       ITEMS.register();
 *       BLOCKS.register();
 *   }
 * }</pre>
 *
 * @param <T> The base registry type
 */
public abstract class DeferredRegister<T> implements Iterable<RegistryHolder<T>> {

    /**
     * This MUST be called during mod-initialization to make sure the {@link DeferredRegister} is registed to the event bus on the Forge side
     */
    public abstract void register();

    /**
     * Adds a new supplier to the list of entries to be registered, and returns a {@link RegistryHolder} that will be populated with the created entry automatically.
     *
     * @param id       ID for the given registered object
     * @param supplier Supplier that returns the object to be registered
     * @return A {@link RegistryHolder} that will contain the registered object
     */
    public abstract <R extends T> RegistryHolder<R> register(String id, Supplier<R> supplier);

    /**
     * Adds a new function to the list of entries to be registered depending on the ID, and returns a {@link RegistryHolder} that will be populated with the created entry automatically.
     *
     * @param id       ID for the given registered object
     * @param function Function that returns the object to be registered
     * @return A {@link RegistryHolder} that will contain the registered object
     */
    public abstract <R extends T> RegistryHolder<R> register(String id, Function<ResourceLocation, R> function);

    /**
     * @return Unmodifiable list of all registered objects
     */
    public abstract Collection<RegistryHolder<T>> getEntries();

    @NotNull
    @Override
    public Iterator<RegistryHolder<T>> iterator() {
        return this.getEntries().iterator();
    }

    /**
     * Creates a new instance of a {@link DeferredRegister} with the given resource key.
     *
     * @param modId       The namespace that will be applied to all registered entries
     * @param resourceKey The resource key for the registry that the entries are registered into
     * @param <T>         Generic type of the registry
     * @return A new instance of a {@link DeferredRegister}
     */
    public static <T> DeferredRegister<T> create(String modId, ResourceKey<? extends Registry<T>> resourceKey) {
        return createInternal(modId, resourceKey);
    }

    @ExpectPlatform
    public static <T> DeferredRegister<T> createInternal(String modId, ResourceKey<? extends Registry<T>> resourceKey) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Items createItems(String modId) {
        throw new AssertionError();
    }

    /**
     * Creates a new instance of a {@link DeferredRegister} with the given {@link RegistryBuilder}
     *
     * @param modId    The namespace that will be applied to all registered entries
     * @param registry Instance of a custom {@link RegistryBuilder} that will be used
     * @param <T>      Generic type of the registry
     * @return A new instance of a {@link DeferredRegister}
     */
    public static <T> DeferredRegister<T> create(String modId, Registry<T> registry) {
        return create(modId, registry.key());
    }

    public static abstract class Items extends DeferredRegister<Item> {

        public abstract <I extends Item> RegistryHolder<I> registerItem(String id, Function<Item.Properties, I> function);

    }

}
