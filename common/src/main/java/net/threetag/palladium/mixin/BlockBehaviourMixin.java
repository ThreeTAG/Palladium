package net.threetag.palladium.mixin;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.threetag.palladium.block.BlockPropertiesCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

    @Inject(method = "propertiesCodec", at = @At("HEAD"), cancellable = true)
    private static <B extends Block> void propertiesCodec(CallbackInfoReturnable<RecordCodecBuilder<B, BlockBehaviour.Properties>> cir) {
        cir.setReturnValue(BlockPropertiesCodec.CODEC.fieldOf("properties").forGetter(BlockBehaviour::properties));
    }

}
