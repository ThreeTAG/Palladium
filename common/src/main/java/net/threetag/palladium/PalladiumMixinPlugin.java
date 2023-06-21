package net.threetag.palladium;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class PalladiumMixinPlugin implements IMixinConfigPlugin {

    private static final boolean HAS_KUBEJS;
    private static final boolean HAS_TRINKETS;
    private static final boolean HAS_CURIOS;
    private static final boolean HAS_GECKO;

    static {
        HAS_KUBEJS = hasClass("dev.latvian.mods.kubejs.KubeJS");
        HAS_TRINKETS = hasClass("dev.emi.trinkets.api.TrinketsApi");
        HAS_CURIOS = hasClass("top.theillusivec4.curios.api.CuriosApi");
        HAS_GECKO = hasClass("software.bernie.geckolib3.renderers.geo.GeoArmorRenderer");
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            if (mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.ScriptFileInfoMixin") || mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.ScriptManagerMixin")) {
                return HAS_KUBEJS;
            }

            if (mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.fabric.TrinketsApiMixin")) {
                return HAS_TRINKETS;
            }

            if (mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.forge.GeoArmorRendererMixin")
                    || mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.fabric.GeoArmorRendererMixin")
                    || mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.client.GeoArmorRendererInvoker")
                    || mixinClassName.equalsIgnoreCase("net.threetag.palladium.mixin.client.IGeoRendererMixin")
            ) {
                return HAS_GECKO;
            }
        } catch (Exception ignored) {
            return true;
        }
        return true;
    }

    private static boolean hasClass(String name) {
        try {
            // This does *not* load the class!
            MixinService.getService().getBytecodeProvider().getClassNode(name);
            return true;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
