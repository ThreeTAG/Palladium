package net.threetag.threecore.util.player;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Map;

public class PlayerUtil {

    public static void sendMessage(PlayerEntity player, String message, boolean hotBar) {
        if (!player.world.isRemote) {
            player.sendStatusMessage(new TranslationTextComponent(message), hotBar);
        }
    }

    public static void sendMessage(PlayerEntity player, TranslationTextComponent translation, boolean hotBar) {
        if (!player.world.isRemote) {
            player.sendStatusMessage(translation, hotBar);
        }
    }

    public static void sendMessageToAll(TranslationTextComponent translation) {
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        players.forEach(playerMP -> sendMessage(playerMP, translation, false));
    }


    public static class Client {

        /**
         * Changes the ResourceLocation of a Players skin
         *
         * @param player  - Player instance involved
         * @param texture - ResourceLocation of intended texture
         */
        public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
            if (player.getLocationSkin() == texture) {
                return;
            }
            NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayerEntity.class, player, 0);
            if (playerInfo == null)
                return;
            Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
            playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
            if (texture == null)
                ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
        }


    }

}
