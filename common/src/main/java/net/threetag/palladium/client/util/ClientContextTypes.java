package net.threetag.palladium.client.util;

import net.minecraft.client.model.PlayerModel;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("InstantiationOfUtilityClass")
public class ClientContextTypes {

    public static DataContextType<Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State>> RENDER_LAYERS = new DataContextType<>();
    public static DataContextType<Float> PARTIAL_TICK = new DataContextType<>();
    public static DataContextType<Set<BodyPart>> HIDDEN_BODY_PARTS = new DataContextType<>();
    public static DataContextType<Set<BodyPart>> REMOVED_BODY_PARTS = new DataContextType<>();
    public static DataContextType<PlayerModel> CACHED_MODEL = new DataContextType<>();
    public static DataContextType<Float[]> AIM = new DataContextType<>();
    public static DataContextType<Map<DataContext, PalladiumAnimation>> ANIMATIONS = new DataContextType<>();
    public static DataContextType<Float> IN_FLIGHT = new DataContextType<>();

}
