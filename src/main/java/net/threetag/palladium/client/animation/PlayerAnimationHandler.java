package net.threetag.palladium.client.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Avatar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.threetag.palladium.entity.data.PalladiumEntityData;

public class PlayerAnimationHandler extends PalladiumEntityData<Avatar, PlayerAnimationHandler> {

    public static final Codec<PlayerAnimationHandler> CODEC = MapCodec.unit(PlayerAnimationHandler::create).codec();

    public static PlayerAnimationHandler create() {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            return new ClientPlayerAnimationHandler();
        } else {
            return new PlayerAnimationHandler();
        }
    }

    @Override
    public Codec<PlayerAnimationHandler> codec() {
        return CODEC;
    }
}
