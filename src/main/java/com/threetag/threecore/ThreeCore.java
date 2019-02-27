package com.threetag.threecore;

import com.threetag.threecore.karma.ThreeCoreKarma;
import com.threetag.threecore.materials.ThreeCoreMaterials;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.command.impl.DefaultGameModeCommand;
import net.minecraft.command.impl.GameModeCommand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(ThreeCore.MODID)
public class ThreeCore {

    public static final String MODID = "threecore";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel NETWORK_CHANNEL;
    private static int networkId = -1;

    public ThreeCore() {
        RecipeUtil.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThreeCoreCommonConfig.generateConfig());

        new ThreeCoreMaterials();
        new ThreeCoreKarma();
    }

    public static <MSG> int registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        if(NETWORK_CHANNEL == null)
            NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ThreeCore.MODID, ThreeCore.MODID), () -> "1.0", (s) -> true, (s) -> true);

        int id = networkId++;
        NETWORK_CHANNEL.registerMessage(id, messageType, encoder, decoder, messageConsumer);
        return id;
    }

    public static ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
        }
    };

}
