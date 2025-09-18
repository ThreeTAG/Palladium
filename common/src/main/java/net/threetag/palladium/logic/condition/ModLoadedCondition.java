package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

import java.util.Objects;

public final class ModLoadedCondition implements Condition {

    public static final MapCodec<ModLoadedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Codec.STRING.fieldOf("mod_id").forGetter(ModLoadedCondition::modId)
            ).apply(instance, ModLoadedCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ModLoadedCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ModLoadedCondition::modId, ModLoadedCondition::new
    );

    private final String modId;
    private final boolean loaded;

    public ModLoadedCondition(String modId) {
        this.modId = modId;
        this.loaded = Platform.isModLoaded(modId);
    }

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return this.loaded;
    }

    @Override
    public ConditionSerializer<ModLoadedCondition> getSerializer() {
        return ConditionSerializers.MOD_LOADED.get();
    }

    public String modId() {
        return modId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ModLoadedCondition) obj;
        return Objects.equals(this.modId, that.modId);
    }


    public static class Serializer extends ConditionSerializer<ModLoadedCondition> {

        @Override
        public MapCodec<ModLoadedCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ModLoadedCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if a mod is loaded.";
        }
    }
}
