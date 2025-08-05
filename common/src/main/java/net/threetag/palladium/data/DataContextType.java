package net.threetag.palladium.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("InstantiationOfUtilityClass")
public class DataContextType<T> {

    public static DataContextType<Entity> ENTITY = new DataContextType<>();
    public static DataContextType<Level> LEVEL = new DataContextType<>();
    public static DataContextType<ItemStack> ITEM = new DataContextType<>();
    public static DataContextType<PlayerSlot> SLOT = new DataContextType<>();
    public static DataContextType<AbilityInstance<?>> ABILITY_INSTANCE = new DataContextType<>();
    public static DataContextType<Holder<Power>> POWER = new DataContextType<>();
    public static DataContextType<PowerHolder> POWER_HOLDER = new DataContextType<>();

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static DataContextType<Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State>> RENDER_LAYERS = new DataContextType<>();
        public static DataContextType<Float> PARTIAL_TICK = new DataContextType<>();
        public static DataContextType<Set<BodyPart>> HIDDEN_BODY_PARTS = new DataContextType<>();
        public static DataContextType<Set<BodyPart>> REMOVED_BODY_PARTS = new DataContextType<>();
        public static DataContextType<PlayerModel> CACHED_MODEL = new DataContextType<>();
        public static DataContextType<Float[]> AIM = new DataContextType<>();

    }

}
