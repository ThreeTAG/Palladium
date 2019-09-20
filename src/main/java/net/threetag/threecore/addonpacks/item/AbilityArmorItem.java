package net.threetag.threecore.addonpacks.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.AbilityMap;
import net.threetag.threecore.abilities.IAbilityProvider;
import net.threetag.threecore.abilities.capability.ItemAbilityContainerProvider;
import net.threetag.threecore.util.client.model.ModelRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbilityArmorItem extends ArmorItem implements IAbilityProvider {

    private List<AbilityGenerator> abilityGenerators;
    public final Map<ResourceLocation, IArmorTexturePropertyGetter> armorTextureProperties = Maps.newLinkedHashMap();
    private ResourceLocation armorTexture;
    private Map<List<Pair<ResourceLocation, Float>>, ResourceLocation> armorTextureOverrides = Maps.newLinkedHashMap();
    @OnlyIn(Dist.CLIENT)
    public LazyLoadBase<BipedModel> model;

    public AbilityArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
        super(materialIn, slot, builder);
        this.addArmorTexturePropertyOverride(new ResourceLocation(ThreeCore.MODID, "sneaking"), (stack, world, entity) -> entity.isSneaking() ? 1F : 0);
        this.addArmorTexturePropertyOverride(new ResourceLocation(ThreeCore.MODID, "damage"), ((stack, world, entity) -> stack.getMaxDamage() == 0 ? 0 : (float) stack.getDamage() / (float) stack.getMaxDamage()));
    }

    public final void addArmorTexturePropertyOverride(ResourceLocation key, IArmorTexturePropertyGetter getter) {
        this.armorTextureProperties.put(key, getter);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomArmorTextureProperties() {
        return !this.armorTextureProperties.isEmpty();
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public IArmorTexturePropertyGetter getArmorTexturePropertyGetter(ResourceLocation key) {
        return this.armorTextureProperties.get(key);
    }

    public AbilityArmorItem setArmorTexture(ResourceLocation texture) {
        this.armorTexture = texture;
        return this;
    }

    public AbilityArmorItem addArmorTextureOverride(ResourceLocation type, float minimum, ResourceLocation armorTexture) {
        this.armorTextureOverrides.put(Arrays.asList(new Pair<>(type, minimum)), armorTexture);
        return this;
    }

    public AbilityArmorItem addArmorTextureOverride(List<Pair<ResourceLocation, Float>> predicates, ResourceLocation armorTexture) {
        this.armorTextureOverrides.put(predicates, armorTexture);
        return this;
    }

    public AbilityArmorItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public AbilityArmorItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public AbilityArmorItem setArmorModel(String key) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            this.model = new LazyLoadBase<BipedModel>(() -> {
                Model model = ModelRegistry.getModel(key);
                if (model instanceof BipedModel)
                    return (BipedModel) model;
                return null;
            });
        });
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (this.hasCustomArmorTextureProperties()) {
            ResourceLocation[] resourceLocation = new ResourceLocation[1];
            this.armorTextureOverrides.forEach((l, r) -> {
                boolean b = true;
                for (Pair<ResourceLocation, Float> pair : l) {
                    IArmorTexturePropertyGetter propertyGetter = getArmorTexturePropertyGetter(pair.getFirst());
                    if (propertyGetter != null && pair.getSecond() > propertyGetter.call(stack, entity.world, entity)) {
                        b = false;
                        break;
                    }
                }
                if (b)
                    resourceLocation[0] = r;
            });
            if (resourceLocation[0] != null)
                return resourceLocation[0].toString();
        }
        return this.armorTexture == null ? null : this.armorTexture.toString();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        if (this.model != null && this.model.getValue() != null) {
            this.model.getValue().setModelAttributes(_default);
            return (A) this.model.getValue();
        }
        return null;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (this.abilityGenerators != null && !this.abilityGenerators.isEmpty())
            return new ItemAbilityContainerProvider(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

    public static AbilityArmorItem parse(JsonObject jsonObject, Item.Properties properties) {
        EquipmentSlotType slot = EquipmentSlotType.fromString(JSONUtils.getString(jsonObject, "slot"));
        if (slot.getSlotType() == EquipmentSlotType.Group.HAND)
            throw new JsonParseException("Slot type must be an armor slot!");
        AbilityArmorItem item = new AbilityArmorItem(ItemParser.parseArmorMaterial(JSONUtils.getJsonObject(jsonObject, "armor_material")), slot, properties);
        if (JSONUtils.hasField(jsonObject, "armor_texture")) {
            JsonElement armorTexture = jsonObject.get("armor_texture");
            if (armorTexture.isJsonPrimitive()) {
                item.setArmorTexture(new ResourceLocation(armorTexture.getAsString()));
            } else {
                JsonObject armorTextureObject = (JsonObject) armorTexture;
                item.setArmorTexture(new ResourceLocation(JSONUtils.getString(armorTextureObject, "base")));

                if (JSONUtils.hasField(armorTextureObject, "overrides")) {
                    JsonArray overrides = JSONUtils.getJsonArray(armorTextureObject, "overrides");
                    for (int i = 0; i < overrides.size(); i++) {
                        JsonObject override = overrides.get(i).getAsJsonObject();
                        JsonObject predicate = JSONUtils.getJsonObject(override, "predicate");
                        List<Pair<ResourceLocation, Float>> predicateList = Lists.newLinkedList();
                        predicate.entrySet().forEach(entry -> predicateList.add(new Pair<>(new ResourceLocation(entry.getKey()), entry.getValue().getAsFloat())));
                        item.addArmorTextureOverride(predicateList, new ResourceLocation(JSONUtils.getString(override, "texture")));
                    }
                }
            }
        }

        item.setArmorModel(JSONUtils.getString(jsonObject, "armor_model", ""));

        return item.setAbilities(JSONUtils.hasField(jsonObject, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(jsonObject, "abilities"), true) : null);
    }

    public interface IArmorTexturePropertyGetter {

        float call(ItemStack stack, @Nullable World world, @Nullable Entity entity);

    }

}
