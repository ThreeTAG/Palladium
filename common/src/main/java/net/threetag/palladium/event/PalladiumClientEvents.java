package net.threetag.palladium.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface PalladiumClientEvents {

    Event<MovementInputUpdate> MOVEMENT_INPUT_UPDATE = EventFactory.createLoop();
    Event<SetupPlayerModelRotations> SETUP_PLAYER_MODEL_ROTATIONS = EventFactory.createLoop();
    Event<SetupHumanoidModelAnimation> SETUP_HUMANOID_MODEL_ANIMATION = EventFactory.createLoop();

    interface MovementInputUpdate {

        void update(Player player, Input input);

    }

    interface SetupPlayerModelRotations {

        void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks);

    }

    interface SetupHumanoidModelAnimation {

        void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    }

}
