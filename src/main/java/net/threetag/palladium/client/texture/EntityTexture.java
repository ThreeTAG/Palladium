package net.threetag.palladium.client.texture;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class EntityTexture extends DynamicTexture {

    public static final EntityTexture INSTANCE = new EntityTexture();
    public static MapCodec<EntityTexture> CODEC = MapCodec.unit(INSTANCE);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Identifier getTexture(DataContext context) {
        var entity = context.getEntity();

        if (entity instanceof LivingEntity living) {
            var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(living);

            if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
                return livingEntityRenderer.getTextureLocation((LivingEntityRenderState) livingEntityRenderer.createRenderState(living, 1F));
            }
        }

        return null;
    }

    @Override
    public DynamicTextureSerializer<?> getSerializer() {
        return DynamicTextureSerializers.ENTITY;
    }

    public static class Serializer extends DynamicTextureSerializer<EntityTexture> {

        @Override
        public MapCodec<EntityTexture> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<DynamicTexture, EntityTexture> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Texture").setDescription("Creates a dynamic texture based on variables and transformers.")
                    .addOptional("variables", TYPE_MAP_VARIABLES, "A map of variables that can be used in the texture path.");
        }
    }
}
