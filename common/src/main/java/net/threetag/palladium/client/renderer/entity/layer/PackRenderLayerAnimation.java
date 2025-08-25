package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.data.DataContext;

public class PackRenderLayerAnimation {

    public static final PackRenderLayerAnimation EMPTY = new PackRenderLayerAnimation((PalladiumAnimation) null);

    public static final Codec<PackRenderLayerAnimation> CODEC = Codec.either(ResourceLocation.CODEC, PalladiumAnimation.CODEC)
            .xmap(either ->
                            either.map(PackRenderLayerAnimation::new,
                                    PackRenderLayerAnimation::new),
                    packAnim -> packAnim.id != null ? Either.left(packAnim.id) : Either.right(packAnim.animation)
            );

    private ResourceLocation id;
    private PalladiumAnimation animation;

    public PackRenderLayerAnimation(ResourceLocation id) {
        this.id = id;
    }

    public PackRenderLayerAnimation(PalladiumAnimation animation) {
        this.animation = animation;
    }

    public PalladiumAnimation getAnimation() {
        if (this.animation == null && this.id != null) {
            this.animation = PalladiumAnimationManager.INSTANCE.get(this.id);
        }

        return this.animation;
    }

    public void animate(Model model, DataContext context, float partialTick) {
        var animation = this.getAnimation();

        if (animation != null) {
            animation.animate(model, context, partialTick);
        }
    }
}
