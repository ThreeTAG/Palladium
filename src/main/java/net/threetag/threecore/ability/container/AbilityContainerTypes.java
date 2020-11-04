package net.threetag.threecore.ability.container;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.Map;
import java.util.function.BiFunction;

public class AbilityContainerTypes {

    private static final Map<Class<? extends IAbilityContainer>, ResourceLocation> CLASSES = Maps.newHashMap();
    private static final Map<ResourceLocation, BiFunction<CompoundNBT, Boolean, IAbilityContainer>> DESERIALIZER = Maps.newHashMap();

    public static void registerType(ResourceLocation id, Class<? extends IAbilityContainer> clazz, BiFunction<CompoundNBT, Boolean, IAbilityContainer> deserializer) {
        DESERIALIZER.put(id, deserializer);
        CLASSES.put(clazz, id);
    }

    public static IAbilityContainer deserialize(CompoundNBT nbt, boolean network) {
        if (nbt.contains("Type")) {
            ResourceLocation id = new ResourceLocation(nbt.getString("Type"));
            BiFunction<CompoundNBT, Boolean, IAbilityContainer> deserializer = DESERIALIZER.get(id);

            if (deserializer != null) {
                return deserializer.apply(nbt, network);
            }
        }
        return null;
    }

    public static ResourceLocation getTypeId(Class<? extends IAbilityContainer> clazz) {
        return CLASSES.get(clazz);
    }

    static {
        registerType(new ResourceLocation(ThreeCore.MODID, "default"), DefaultAbilityContainer.class, DefaultAbilityContainer::new);
        registerType(new ResourceLocation(ThreeCore.MODID, "superpower"), SuperpowerAbilityContainer.class, SuperpowerAbilityContainer::new);
    }

}
