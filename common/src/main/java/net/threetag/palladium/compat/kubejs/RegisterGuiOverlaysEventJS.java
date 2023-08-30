//package net.threetag.palladium.compat.kubejs;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import dev.latvian.mods.kubejs.event.EventJS;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import net.threetag.palladiumcore.registry.client.OverlayRegistry;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RegisterGuiOverlaysEventJS extends EventJS {
//
//    public static final Map<String, OverlayRegistry.IIngameOverlay> OVERLAYS = new HashMap<>();
//
//    public RegisterGuiOverlaysEventJS() {
//        OVERLAYS.clear();
//    }
//
//    public void register(String id, OverlayRegistry.IIngameOverlay overlay) {
//        OVERLAYS.put(id, overlay);
//    }
//
//    public static class Overlay implements OverlayRegistry.IIngameOverlay {
//
//        @Override
//        public void render(Minecraft minecraft, Gui gui, PoseStack mStack, float partialTicks, int width, int height) {
//            for (OverlayRegistry.IIngameOverlay overlay : OVERLAYS.values()) {
//                overlay.render(minecraft, gui, mStack, partialTicks, width, height);
//            }
//        }
//    }
//
//}
