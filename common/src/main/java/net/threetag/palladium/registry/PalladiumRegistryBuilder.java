package net.threetag.palladium.registry;

import net.minecraft.resources.ResourceLocation;

public abstract class PalladiumRegistryBuilder<T> {

    private final Class<T> clazz;
    private final ResourceLocation id;

    public PalladiumRegistryBuilder(Class<T> clazz, ResourceLocation id) {
        this.clazz = clazz;
        this.id = id;
    }

    public static <T> PalladiumRegistryBuilder<T> create(Class<T> clazz, ResourceLocation id) {
        throw new AssertionError();
    }

}
