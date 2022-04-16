package net.threetag.palladium.client.renderer.renderlayer;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ModelLookup {

    private static final Map<ResourceLocation, Model> MODELS = new HashMap<>();
    public static final Model HUMANOID;

    static {
        HUMANOID = register(new ResourceLocation("humanoid"), new Model(HumanoidModel::new, (en, model) -> model instanceof HumanoidModel));
        register(new ResourceLocation("player"), new Model(modelPart -> new PlayerModel<>(modelPart, false), (en, model) -> model instanceof HumanoidModel));
        register(new ResourceLocation("pig"), new Model(PigModel::new, (en, model) -> model instanceof PigModel));
        register(new ResourceLocation("wolf"), new Model(WolfModel::new, (en, model) -> model instanceof WolfModel));
        register(new ResourceLocation("cat"), new Model(CatModel::new, (en, model) -> model instanceof CatModel));
        register(new ResourceLocation("horse"), new Model(HorseModel::new, (en, model) -> model instanceof HorseModel));
        register(new ResourceLocation("sheep"), new Model(SheepModel::new, (en, model) -> model instanceof SheepModel));
        register(new ResourceLocation("cow"), new Model(CowModel::new, (en, model) -> model instanceof CowModel));
        register(new ResourceLocation("chicken"), new Model(ChickenModel::new, (en, model) -> model instanceof ChickenModel));
        register(new ResourceLocation("villager"), new Model(VillagerModel::new, (en, model) -> model instanceof VillagerModel));
        register(new ResourceLocation("illager"), new Model(IllagerModel::new, (en, model) -> model instanceof IllagerModel));
        register(new ResourceLocation("axolotl"), new Model(AxolotlModel::new, (en, model) -> model instanceof AxolotlModel));
        register(new ResourceLocation("bat"), new Model(BatModel::new, (en, model) -> model instanceof BatModel));
        register(new ResourceLocation("bee"), new Model(BeeModel::new, (en, model) -> model instanceof BeeModel));
        register(new ResourceLocation("blaze"), new Model(BlazeModel::new, (en, model) -> model instanceof BlazeModel));
        register(new ResourceLocation("spider"), new Model(SpiderModel::new, (en, model) -> model instanceof SpiderModel));
        register(new ResourceLocation("cod"), new Model(CodModel::new, (en, model) -> model instanceof CodModel));
        register(new ResourceLocation("creeper"), new Model(CreeperModel::new, (en, model) -> model instanceof CreeperModel));
        register(new ResourceLocation("dolphin"), new Model(DolphinModel::new, (en, model) -> model instanceof DolphinModel));
        register(new ResourceLocation("guardian"), new Model(GuardianModel::new, (en, model) -> model instanceof GuardianModel));
        register(new ResourceLocation("enderman"), new Model(EndermanModel::new, (en, model) -> model instanceof EndermanModel));
        register(new ResourceLocation("endermite"), new Model(EndermiteModel::new, (en, model) -> model instanceof EndermiteModel));
        register(new ResourceLocation("fox"), new Model(FoxModel::new, (en, model) -> model instanceof FoxModel));
        register(new ResourceLocation("ghast"), new Model(GhastModel::new, (en, model) -> model instanceof GhastModel));
        register(new ResourceLocation("goat"), new Model(GoatModel::new, (en, model) -> model instanceof GoatModel));
        register(new ResourceLocation("hoglin"), new Model(HoglinModel::new, (en, model) -> model instanceof HoglinModel));
        register(new ResourceLocation("iron_golem"), new Model(IronGolemModel::new, (en, model) -> model instanceof IronGolemModel));
        register(new ResourceLocation("llama"), new Model(LlamaModel::new, (en, model) -> model instanceof LlamaModel));
        register(new ResourceLocation("magma_cube"), new Model(LavaSlimeModel::new, (en, model) -> model instanceof LavaSlimeModel));
        register(new ResourceLocation("panda"), new Model(PandaModel::new, (en, model) -> model instanceof PandaModel));
        register(new ResourceLocation("parrot"), new Model(ParrotModel::new, (en, model) -> model instanceof ParrotModel));
        register(new ResourceLocation("phantom"), new Model(PhantomModel::new, (en, model) -> model instanceof PhantomModel));
        register(new ResourceLocation("piglin"), new Model(PiglinModel::new, (en, model) -> model instanceof PiglinModel));
        register(new ResourceLocation("polar_bear"), new Model(PolarBearModel::new, (en, model) -> model instanceof PolarBearModel));
        register(new ResourceLocation("rabbit"), new Model(RabbitModel::new, (en, model) -> model instanceof RabbitModel));
        register(new ResourceLocation("ravager"), new Model(RavagerModel::new, (en, model) -> model instanceof RavagerModel));
        register(new ResourceLocation("salmon"), new Model(SalmonModel::new, (en, model) -> model instanceof SalmonModel));
        register(new ResourceLocation("shulker"), new Model(ShulkerModel::new, (en, model) -> model instanceof ShulkerModel));
        register(new ResourceLocation("silverfish"), new Model(SilverfishModel::new, (en, model) -> model instanceof SilverfishModel));
        register(new ResourceLocation("slime"), new Model(SlimeModel::new, (en, model) -> model instanceof SlimeModel));
        register(new ResourceLocation("snow_golem"), new Model(SnowGolemModel::new, (en, model) -> model instanceof SnowGolemModel));
        register(new ResourceLocation("squid"), new Model(SquidModel::new, (en, model) -> model instanceof SquidModel));
        register(new ResourceLocation("strider"), new Model(StriderModel::new, (en, model) -> model instanceof StriderModel));
        register(new ResourceLocation("turtle"), new Model(TurtleModel::new, (en, model) -> model instanceof TurtleModel));
        register(new ResourceLocation("vex"), new Model(VexModel::new, (en, model) -> model instanceof VexModel));
        register(new ResourceLocation("wither"), new Model(WitherBossModel::new, (en, model) -> model instanceof WitherBossModel));
    }

    public static Model register(ResourceLocation id, Model model) {
        MODELS.put(id, model);
        return model;
    }

    public static Model get(ResourceLocation id) {
        return MODELS.get(id);
    }

    public record Model(
            Function<ModelPart, EntityModel<?>> modelFunction,
            BiPredicate<LivingEntity, EntityModel<?>> predicate) {

        public EntityModel<?> getModel(ModelPart modelPart) {
            return this.modelFunction.apply(modelPart);
        }

        public boolean fitsEntity(LivingEntity entity, EntityModel<?> parentModel) {
            return this.predicate.test(entity, parentModel);
        }
    }

}
