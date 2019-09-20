package net.threetag.threecore;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.threetag.threecore.abilities.ThreeCoreAbilities;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.karma.ThreeCoreKarma;
import net.threetag.threecore.sizechanging.ThreeCoreSizeChanging;
import net.threetag.threecore.util.SupporterHandler;
import net.threetag.threecore.util.client.RenderUtil;
import net.threetag.threecore.util.client.model.EntityModelManager;
import net.threetag.threecore.util.data.ThreeCoreBlockTagsProvider;
import net.threetag.threecore.util.data.ThreeCoreItemTagsProvider;
import net.threetag.threecore.util.data.ThreeCoreRecipeProvider;
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
        // Basic stuff
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);
        SupporterHandler.load();
        SupporterHandler.enableSupporterCheck();

        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThreeCoreCommonConfig.generateConfig());

        // Modules
        new ThreeCoreAddonPacks();
        new ThreeCoreBase();
        new ThreeCoreAbilities();
        new ThreeCoreKarma();
        new ThreeCoreSizeChanging();

        // Misc
        MinecraftForge.EVENT_BUS.addListener(RenderUtil::onRenderGlobal);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new EntityModelManager()));
    }

    public void gatherData(GatherDataEvent e) {
        e.getGenerator().addProvider(new ThreeCoreBlockTagsProvider(e.getGenerator()));
        e.getGenerator().addProvider(new ThreeCoreItemTagsProvider(e.getGenerator()));
        e.getGenerator().addProvider(new ThreeCoreRecipeProvider(e.getGenerator()));
    }

    public static <MSG> int registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        if (NETWORK_CHANNEL == null)
            NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ThreeCore.MODID, ThreeCore.MODID), () -> "1.0", (s) -> true, (s) -> true);

        int id = networkId++;
        NETWORK_CHANNEL.registerMessage(id, messageType, encoder, decoder, messageConsumer);
        return id;
    }

}
