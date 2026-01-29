package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;

public class PackRenderLayerAnimation {

    public static final PackRenderLayerAnimation EMPTY = new PackRenderLayerAnimation((PalladiumAnimation) null);

    public static final Codec<PackRenderLayerAnimation> CODEC = Codec.either(Identifier.CODEC, PalladiumAnimation.CODEC)
            .xmap(either ->
                            either.map(PackRenderLayerAnimation::new,
                                    PackRenderLayerAnimation::new),
                    packAnim -> packAnim.id != null ? Either.left(packAnim.id) : Either.right(packAnim.animation)
            );

    private Identifier id;
    private PalladiumAnimation animation;

    public PackRenderLayerAnimation(Identifier id) {
        this.id = id;
    }

    public PackRenderLayerAnimation(PalladiumAnimation animation) {
        this.animation = animation;
    }

    public PalladiumAnimation getAnimation() {
        if (this.animation == null && this.id != null) {
//            this.animation = PalladiumAnimationManager.INSTANCE.get(this.id);
        }

        return this.animation;
    }

    public void animate(Model<?> model, DataContext context, @Nullable EntityRenderState renderState, float partialTick) {
        var animation = this.getAnimation();

        if (animation != null) {
            animation.animate(model, context, renderState, partialTick);
        }
    }
}
