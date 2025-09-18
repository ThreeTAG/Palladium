package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.logic.context.DataContext;

public class HasMovementInputCondition implements Condition {

    public static final HasMovementInputCondition INSTANCE = new HasMovementInputCondition();

    public static final MapCodec<HasMovementInputCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, HasMovementInputCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var player = context.getPlayer();

        if (player instanceof ServerPlayer serverPlayer) {
            var input = serverPlayer.getLastClientInput();
            return input.forward() || input.backward() || input.left() || input.right() || input.jump();
        }

        if (Platform.getEnv() == EnvType.CLIENT) {
            return this.testClient(player);
        }

        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean testClient(Player player) {
        if (player == Minecraft.getInstance().player) {
            var options = Minecraft.getInstance().options;
            return options.keyUp.isDown() || options.keyDown.isDown() || options.keyRight.isDown() || options.keyLeft.isDown() || options.keyJump.isDown();
        } else {
            return false;
        }
    }

    @Override
    public ConditionSerializer<HasMovementInputCondition> getSerializer() {
        return ConditionSerializers.HAS_MOVEMENT_INPUT.get();
    }

    public static class Serializer extends ConditionSerializer<HasMovementInputCondition> {

        @Override
        public MapCodec<HasMovementInputCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HasMovementInputCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if a player is currently pressing keys to move.";
        }
    }
}
