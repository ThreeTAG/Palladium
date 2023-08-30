//package net.threetag.palladium.compat.kubejs;
//
//import dev.latvian.mods.kubejs.event.EventGroup;
//import dev.latvian.mods.kubejs.event.EventHandler;
//
//public interface PalladiumJSEvents {
//
//    EventGroup GROUP = EventGroup.of("PalladiumEvents");
//
//    EventHandler REGISTER_PROPERTIES = GROUP.server("registerProperties", () -> RegisterPalladiumPropertyEventJS.class);
//    EventHandler CLIENT_REGISTER_PROPERTIES = GROUP.client("registerPropertiesClientSided", () -> RegisterPalladiumPropertyEventJS.class);
//
//    EventHandler REGISTER_ANIMATIONS = GROUP.client("registerAnimations", () -> RegisterAnimationsEventJS.class);
//    EventHandler REGISTER_GUI_OVERLAYS = GROUP.client("registerGuiOverlays", () -> RegisterGuiOverlaysEventJS.class);
//
//}
