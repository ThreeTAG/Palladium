package net.threetag.palladium.client.dynamictexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.TransformedTexture;
import net.threetag.palladium.client.renderer.entity.PlayerSkinFetcher;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.EntityPropertyHandler;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerDynamicTexture extends DynamicTexture {

    public static boolean IGNORE_SKIN_CHANGE = false;

    private final String username;
    private final String property;

    public PlayerDynamicTexture(String username, String property) {
        this.username = username;
        this.property = property;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
        AtomicReference<String> nameRef = new AtomicReference<>(this.username);

        if (this.property != null && !this.property.isEmpty()) {
            EntityPropertyHandler.getHandler(context.getEntity()).ifPresent(handler -> {
                var prop = handler.getPropertyByName(this.property);
                if (prop != null && handler.get(prop) instanceof String s) {
                    nameRef.set(s);
                }
            });
        }

        var skin = PlayerSkinFetcher.getOrLoadPlayerSkin(nameRef.get());
        var texture = skin != null ? skin : DefaultPlayerSkin.getDefaultSkin();

        if (this.transformers.isEmpty()) {
            return texture;
        }

        StringBuilder outputPath = new StringBuilder(texture.toString());

        for (String var : this.textureVariableMap.keySet()) {
            outputPath.append("_#").append(var);
        }

        ResourceLocation output = new ResourceLocation(DefaultDynamicTexture.replaceVariables(outputPath.toString(), context, this.textureVariableMap));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            Minecraft.getInstance().getTextureManager().register(output, new TransformedTexture(texture, EntityDynamicTexture.getNativeImageIfPossible(texture), this.transformers, context, transformerPath -> DefaultDynamicTexture.replaceVariables(transformerPath, context, this.textureVariableMap)));
        }

        return output;
    }
}
