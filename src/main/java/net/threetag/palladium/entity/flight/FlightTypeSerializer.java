package net.threetag.palladium.entity.flight;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;

public abstract class FlightTypeSerializer<T extends FlightType> implements Documented<FlightType, T> {

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<FlightType, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), FlightType.CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<FlightType, T> builder, HolderLookup.Provider provider);

}
