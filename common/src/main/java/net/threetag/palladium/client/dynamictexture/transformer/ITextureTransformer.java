package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.threetag.palladium.util.context.DataContext;

import java.io.IOException;
import java.util.function.Function;

public interface ITextureTransformer {

    NativeImage transform(DataContext context, NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException;

}
