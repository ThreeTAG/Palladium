package net.threetag.palladium.client.texture;

import net.threetag.palladium.Palladium;

public class DynamicTextureSerializers {

    public static final DynamicTextureSerializer<StaticTexture> STATIC = register("static", new StaticTexture.Serializer());
    public static final DynamicTextureSerializer<ConfiguredTexture> CONFIGURED = register("configured", new ConfiguredTexture.Serializer());

    private static <T extends DynamicTexture> DynamicTextureSerializer<T> register(String id, DynamicTextureSerializer<T> serializer) {
        return DynamicTextureSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
