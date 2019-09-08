package net.threetag.threecore.sizechanging;

import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.sizechanging.capability.ISizeChanging;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class SizeChangeType extends ForgeRegistryEntry<SizeChangeType> {

    public static IForgeRegistry<SizeChangeType> REGISTRY;
    public static SizeChangeType DEFAULT_TYPE;
    public static final UUID ATTRIBUTE_UUID = UUID.fromString("8412f590-3b43-4526-ac59-e52a1969d0de");

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<SizeChangeType>().setName(new ResourceLocation(ThreeCore.MODID, "size_change_types")).setType(SizeChangeType.class).setIDRange(0, 512).create();
    }

    @SubscribeEvent
    public static void registerSizeChangeTypes(RegistryEvent.Register<SizeChangeType> e) {
        e.getRegistry().register(DEFAULT_TYPE = new DefaultSizeChangeType().setRegistryName("default"));
    }

    public abstract int getSizeChangingTime(Entity entity, ISizeChanging data, float estimatedSize);

    public abstract void onSizeChanged(Entity entity, ISizeChanging data, float size);

    public abstract void onUpdate(Entity entity, ISizeChanging data, float size);

    public abstract boolean start(Entity entity, ISizeChanging data, float size, float estimatedSize);

    public abstract void end(Entity entity, ISizeChanging data, float size);

    @OnlyIn(Dist.CLIENT)
    public void render(Entity entity, EntityRenderer renderer, List<Entity> entities, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    }

    @OnlyIn(Dist.CLIENT)
    public void renderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }

}
