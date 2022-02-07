package net.threetag.palladium.forge.mixin;

import com.google.common.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.threetag.palladium.power.ability.condition.ConditionSerializer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ConditionSerializer.class)
public class ConditionSerializerMixin implements IForgeRegistryEntry<ConditionSerializer> {

    private final TypeToken<ConditionSerializer> token = new TypeToken<ConditionSerializer>(getClass()){};
    private ResourceLocation registryName = null;

    public final ConditionSerializer setRegistryName(String name)
    {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());

        this.registryName = checkRegistryName(name);
        return (ConditionSerializer)(Object)this;
    }

    public final ConditionSerializer setRegistryName(ResourceLocation name){ return setRegistryName(name.toString()); }

    @Nullable
    public final ResourceLocation getRegistryName()
    {
        return registryName != null ? registryName : null;
    }

    public final Class<ConditionSerializer> getRegistryType() { return (Class<ConditionSerializer>)token.getRawType(); }

    /**
     * This will assert that the registry name is valid and warn about potential registry overrides
     * It is important as it detects cases where modders unintentionally register objects with the "minecraft" namespace, leading to dangerous errors later.
     * @param name The registry name
     * @return A verified "correct" registry name
     */
    ResourceLocation checkRegistryName(String name)
    {
        return GameData.checkPrefix(name, true);
    }

}
