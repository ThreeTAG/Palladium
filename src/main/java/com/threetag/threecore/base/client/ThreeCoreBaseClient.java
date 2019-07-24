package com.threetag.threecore.base.client;

import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.base.client.gui.GrinderScreen;
import com.threetag.threecore.base.client.gui.HydraulicPressScreen;
import com.threetag.threecore.base.client.renderer.tileentity.HydraulicPressTileEntityRenderer;
import com.threetag.threecore.base.tileentity.HydraulicPressTileEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ThreeCoreBaseClient
{

	@SubscribeEvent
	public void setup(final FMLClientSetupEvent e)
	{
		// Screens
		ScreenManager.registerFactory(ThreeCoreBase.GRINDER_CONTAINER, GrinderScreen::new);
		ScreenManager.registerFactory(ThreeCoreBase.HYDRAULIC_PRESS_CONTAINER, HydraulicPressScreen::new);

		// TESR
		ClientRegistry.bindTileEntitySpecialRenderer(HydraulicPressTileEntity.class, new HydraulicPressTileEntityRenderer());
	}
}
